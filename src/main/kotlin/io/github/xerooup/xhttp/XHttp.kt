package io.github.xerooup.xhttp

import io.github.xerooup.xhttp.request.Method
import io.github.xerooup.xhttp.request.builder.RequestBuilder
import io.github.xerooup.xhttp.response.Response

// --- top-level DSL shortcuts ---

/**
 * Sends a GET request to the specified URL
 * @param url the full URL to send the request to
 * @param block optional DSL block to configure headers, timeout, etc
 * @return the HTTP response
 */
fun get(url: String, block: RequestBuilder.() -> Unit = {}): Response =
    RequestBuilder(Method.GET, url).apply(block).send()

/**
 * Sends a POST request to the specified URL
 * @param url the full URL to send the request to
 * @param block optional DSL block to configure headers, body, timeout, etc
 * @return the HTTP response
 */
fun post(url: String, block: RequestBuilder.() -> Unit = {}): Response =
    RequestBuilder(Method.POST, url).apply(block).send()

/**
 * Sends a PUT request to the specified URL
 * @param url the full URL to send the request to
 * @param block optional DSL block to configure headers, body, timeout, etc
 * @return the HTTP response
 */
fun put(url: String, block: RequestBuilder.() -> Unit = {}): Response =
    RequestBuilder(Method.PUT, url).apply(block).send()

/**
 * Sends a DELETE request to the specified URL
 * @param url the full URL to send the request to
 * @param block optional DSL block to configure headers, timeout, etc
 * @return the HTTP response
 */
fun delete(url: String, block: RequestBuilder.() -> Unit = {}): Response =
    RequestBuilder(Method.DELETE, url).apply(block).send()

/**
 * Sends a HEAD request to the specified URL
 * @param url the full URL to send the request to
 * @param block optional DSL block to configure headers, timeout, etc
 * @return the HTTP response
 */
fun head(url: String, block: RequestBuilder.() -> Unit = {}): Response =
    RequestBuilder(Method.HEAD, url).apply(block).send()

/**
 * Sends a PATCH request to the given path or full URL
 * @param path relative path appended to baseUrl, or a full URL if it starts with http
 * @param block optional DSL block to configure headers, body, timeout, etc
 * @return the HTTP response
 */
fun patch(path: String, block: RequestBuilder.() -> Unit = {}): Response =
    RequestBuilder(Method.PATCH, path).apply(block).send()

// --- keep old object API for backwards compat ---

@Deprecated(
    message = "Prefer top-level DSL functions: get(...) { }, post(...) { }",
    level = DeprecationLevel.WARNING
)
object XHttp {
    fun get(url: String) = RequestBuilder(Method.GET, url)
    fun post(url: String) = RequestBuilder(Method.POST, url)
    fun put(url: String) = RequestBuilder(Method.PUT, url)
    fun delete(url: String) = RequestBuilder(Method.DELETE, url)
    fun head(url: String) = RequestBuilder(Method.HEAD, url)
}