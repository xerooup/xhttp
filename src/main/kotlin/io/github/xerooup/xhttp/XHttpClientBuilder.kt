package io.github.xerooup.xhttp

@XHttpDsl
class XHttpClientBuilder internal constructor() {
    var baseUrl: String = ""
    private val defaultHeaders: MutableMap<String, String> = linkedMapOf()

    /**
     * Adds a default header that will be included in every request sent by this client
     * @param name the header field name
     * @param value the header field value
     */
    fun defaultHeader(name: String, value: String) {
        defaultHeaders[name] = value
    }

    internal fun buildDefaults() = defaultHeaders.toMap()
    internal fun resolve(path: String) = if (path.startsWith("http")) path else baseUrl + path
}