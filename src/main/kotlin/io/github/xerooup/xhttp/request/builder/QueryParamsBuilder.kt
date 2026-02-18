package io.github.xerooup.xhttp.request.builder

import io.github.xerooup.xhttp.XHttpDsl

@XHttpDsl
class QueryParamsBuilder internal constructor() {
    internal val params: MutableList<Pair<String, String>> = mutableListOf()

    /**
     * Adds a query parameter using infix notation
     * @param value the value to associate with this parameter name
     */
    infix fun String.to(value: String) {
        params.add(Pair(this, value))
    }

    /**
     * Adds a query parameter using invoke operator notation
     * @param value the value to associate with this parameter name
     */
    operator fun String.invoke(value: String) {
        params.add(Pair(this, value))
    }
}