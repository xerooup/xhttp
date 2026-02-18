package io.github.xerooup.xhttp.request.builder

import io.github.xerooup.xhttp.XHttpDsl

@XHttpDsl
class HeadersBuilder internal constructor() {
    internal val headers: MutableMap<String, String> = linkedMapOf()

    /**
     * Adds a header using infix notation
     * @param value the header value to associate with this header name
     */
    infix fun String.to(value: String) {
        headers[this] = value
    }

    /**
     * Adds a header using invoke operator notation
     * @param value the header value to associate with this header name
     */
    operator fun String.invoke(value: String) {
        headers[this] = value
    }
}