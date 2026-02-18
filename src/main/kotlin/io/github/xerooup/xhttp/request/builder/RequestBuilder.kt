package io.github.xerooup.xhttp.request.builder

import io.github.xerooup.xhttp.XHttpDsl
import io.github.xerooup.xhttp.request.Method
import io.github.xerooup.xhttp.response.Response
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

@XHttpDsl
class RequestBuilder internal constructor(
    private val method: Method,
    private val url: String
) {
    private val headers: MutableMap<String, String> = linkedMapOf()
    private var timeoutMs: Long? = null
    private var bodyBytes: ByteArray? = null
    private var contentType: String? = null
    private val queryParams: MutableList<Pair<String, String>> = mutableListOf()
    private var retryTimes: Int = 0
    private var retryDelayMs: Long = 0
    private var onErrorCallback: ((Response) -> Unit)? = null

    /**
     * Configures request headers using a nested DSL block
     * @param block lambda with access to HeadersBuilder where headers can be defined
     */
    fun headers(block: HeadersBuilder.() -> Unit) {
        headers.putAll(HeadersBuilder().apply(block).headers)
    }

    /**
     * Adds a single header to the request
     * @param name the header field name
     * @param value the header field value
     */
    fun header(name: String, value: String) {
        headers[name] = value
    }

    /**
     * Configures query parameters using a nested DSL block, appended to the URL automatically
     * @param block lambda with access to QueryParamsBuilder where params can be defined
     */
    fun queryParams(block: QueryParamsBuilder.() -> Unit) {
        queryParams.addAll(QueryParamsBuilder().apply(block).params)
    }

    /**
     * Sets the request body from a plain string with a specified content type
     * @param text the body content as a UTF-8 string
     * @param contentType the MIME type to use in the Content-Type header
     */
    fun body(text: String, contentType: String) {
        this.bodyBytes = text.toByteArray(Charsets.UTF_8)
        this.contentType = contentType
    }

    /**
     * Sets the request body from a raw byte array with a specified content type
     * @param bytes the body content as raw bytes
     * @param contentType the MIME type to use in the Content-Type header
     */
    fun body(bytes: ByteArray, contentType: String) {
        this.bodyBytes = bytes
        this.contentType = contentType
    }

    /**
     * Sets the request body as JSON with Content-Type application/json; charset=utf-8
     * @param text the JSON string to send as the body
     */
    fun json(text: String) = body(text, "application/json; charset=utf-8")

    /**
     * Sets the request body as plain text with Content-Type text/plain; charset=utf-8
     * @param text the plain text string to send as the body
     */
    fun text(text: String) = body(text, "text/plain; charset=utf-8")

    /**
     * Configures a multipart/form-data body using a nested DSL block
     * @param block lambda with access to MultipartBuilder where fields and files can be added
     */
    fun multipart(block: MultipartBuilder.() -> Unit) {
        val (bytes, contentType) = MultipartBuilder().apply(block).build()
        body(bytes, contentType)
    }

    /**
     * Sets the request timeout in milliseconds, applied to both connection and read
     * @param ms timeout duration in milliseconds
     */
    fun timeout(ms: Long) {
        timeoutMs = ms
    }

    /**
     * Sets the request timeout using a Kotlin Duration, applied to both connection and read
     * @param duration timeout as a Kotlin Duration value
     */
    fun timeout(duration: kotlin.time.Duration) {
        timeoutMs = duration.inWholeMilliseconds
    }

    /**
     * Retries the request automatically if the response is not ok or an exception is thrown
     * @param times number of additional attempts after the first failure
     * @param delayMs delay in milliseconds between each retry attempt
     */
    fun retry(times: Int, delayMs: Long = 0) {
        retryTimes = times
        retryDelayMs = delayMs
    }

    /**
     * Registers a callback invoked when the response status is not in 200-299
     * @param block lambda receiving the failed Response for logging or error handling
     */
    fun onError(block: (Response) -> Unit) {
        onErrorCallback = block
    }

    // --- send ---

    internal fun send(): Response {
        val finalUrl = if (queryParams.isEmpty()) url else {
            val query = queryParams.joinToString("&") { (k, v) ->
                "${java.net.URLEncoder.encode(k, Charsets.UTF_8)}=${java.net.URLEncoder.encode(v, Charsets.UTF_8)}"
            }
            if (url.contains("?")) "$url&$query" else "$url?$query"
        }

        val client = HttpClient.newBuilder()
            .apply { timeoutMs?.let { connectTimeout(Duration.ofMillis(it)) } }
            .build()

        fun attempt(): Response {
            val builder = HttpRequest.newBuilder().uri(URI.create(finalUrl))
            timeoutMs?.let { builder.timeout(Duration.ofMillis(it)) }
            for ((k, v) in headers) builder.header(k, v)

            val publisher = bodyBytes
                ?.also { if (contentType != null) builder.header("Content-Type", contentType!!) }
                ?.let { HttpRequest.BodyPublishers.ofByteArray(it) }
                ?: HttpRequest.BodyPublishers.noBody()

            builder.method(method.name, publisher)

            val httpResponse = client.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray())
            return Response(
                status = httpResponse.statusCode(),
                headers = httpResponse.headers().map().mapValues { it.value.joinToString(",") },
                ok = httpResponse.statusCode() in 200..299,
                body = httpResponse.body()
            )
        }

        var response = attempt()
        var attemptsLeft = retryTimes

        while (!response.ok && attemptsLeft > 0) {
            if (retryDelayMs > 0) Thread.sleep(retryDelayMs)
            response = attempt()
            attemptsLeft--
        }

        if (!response.ok) onErrorCallback?.invoke(response)

        return response
    }
}