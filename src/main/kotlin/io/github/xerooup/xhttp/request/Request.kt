package io.github.xerooup.xhttp.request

import io.github.xerooup.xhttp.response.Response
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import kotlin.collections.iterator

// request builder
class Request internal constructor(
    private val method: Method,
    private val url: String
) {
    private val headers: MutableMap<String, String> = linkedMapOf()
    private var timeoutMs: Long? = null
    private var bodyBytes: ByteArray? = null
    private var contentType: String? = null

    fun header(name: String, value: String): Request = apply {
        headers[name] = value
    }

    fun headers(map: Map<String, String>): Request = apply {
        headers.putAll(map)
    }

    fun timeout(ms: Long): Request = apply {
        timeoutMs = ms
    }

    fun timeout(duration: kotlin.time.Duration): Request = apply {
        timeoutMs = duration.inWholeMilliseconds.toLong()
    }

    fun body(text: String, contentType: String): Request = apply {
        this.bodyBytes = text.toByteArray(Charsets.UTF_8)
        this.contentType = contentType
    }

    fun body(bytes: ByteArray, contentType: String): Request = apply {
        this.bodyBytes = bytes
        this.contentType = contentType
    }

    // send request
    fun send(): Response {
        val client = HttpClient.newBuilder()
            .apply {
                timeoutMs?.let { connectTimeout(Duration.ofMillis(it)) }
            }
            .build()

        val builder = HttpRequest.newBuilder()
            .uri(URI.create(url))

        timeoutMs?.let { builder.timeout(Duration.ofMillis(it)) }

        for ((k, v) in headers) {
            builder.header(k, v)
        }

        if (bodyBytes != null) {
            if (contentType != null) {
                builder.header("Content-Type", contentType!!)
            }
            builder.method(method.name, HttpRequest.BodyPublishers.ofByteArray(bodyBytes))
        } else {
            builder.method(method.name, HttpRequest.BodyPublishers.noBody())
        }

        val httpResponse = client.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray())

        return Response(
            status = httpResponse.statusCode(),
            headers = httpResponse.headers().map().mapValues { it.value.joinToString(",") },
            ok = httpResponse.statusCode() in 200..299,
            body = httpResponse.body()
        )
    }
}