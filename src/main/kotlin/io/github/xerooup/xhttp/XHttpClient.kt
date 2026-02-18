package io.github.xerooup.xhttp

import io.github.xerooup.xhttp.request.Method
import io.github.xerooup.xhttp.request.builder.RequestBuilder
import io.github.xerooup.xhttp.response.Response

class XHttpClient internal constructor(
    private val baseUrl: String,
    private val defaultHeaders: Map<String, String>
) {
    private fun request(method: Method, path: String, block: RequestBuilder.() -> Unit): Response {
        val url = if (path.startsWith("http")) path else baseUrl + path
        return RequestBuilder(method, url).apply {
            defaultHeaders.forEach { (k, v) -> header(k, v) }
        }.apply(block).send()
    }

    /**
     * Sends a GET request to the given path or full URL
     * @param path relative path appended to baseUrl, or a full URL if it starts with http
     * @param block optional DSL block to configure headers, timeout, etc
     * @return the HTTP response
     */
    fun get(path: String, block: RequestBuilder.() -> Unit = {}): Response =
        request(Method.GET, path, block)

    /**
     * Sends a POST request to the given path or full URL
     * @param path relative path appended to baseUrl, or a full URL if it starts with http
     * @param block optional DSL block to configure headers, body, timeout, etc
     * @return the HTTP response
     */
    fun post(path: String, block: RequestBuilder.() -> Unit = {}): Response =
        request(Method.POST, path, block)

    /**
     * Sends a PUT request to the given path or full URL
     * @param path relative path appended to baseUrl, or a full URL if it starts with http
     * @param block optional DSL block to configure headers, body, timeout, etc
     * @return the HTTP response
     */
    fun put(path: String, block: RequestBuilder.() -> Unit = {}): Response =
        request(Method.PUT, path, block)

    /**
     * Sends a DELETE request to the given path or full URL
     * @param path relative path appended to baseUrl, or a full URL if it starts with http
     * @param block optional DSL block to configure headers, timeout, etc
     * @return the HTTP response
     */
    fun delete(path: String, block: RequestBuilder.() -> Unit = {}): Response =
        request(Method.DELETE, path, block)

    /**
     * Sends a HEAD request to the given path or full URL
     * @param path relative path appended to baseUrl, or a full URL if it starts with http
     * @param block optional DSL block to configure headers, timeout, etc
     * @return the HTTP response
     */
    fun head(path: String, block: RequestBuilder.() -> Unit = {}): Response =
        request(Method.HEAD, path, block)
}

/**
 * Creates a reusable XHttpClient configured via a DSL block
 * @param block lambda with access to XHttpClientBuilder for setting baseUrl and default headers
 * @return a configured XHttpClient instance ready to send requests
 */
fun XHttpClient(block: XHttpClientBuilder.() -> Unit): XHttpClient {
    val cfg = XHttpClientBuilder().apply(block)
    return XHttpClient(cfg.resolve(""), cfg.buildDefaults())
}