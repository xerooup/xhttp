package org.xeroup.xhttp.response

// http response
class Response internal constructor(
    val status: Int,
    val headers: Map<String, String>,
    private val body: ByteArray
) {
    fun text(): String = body.toString(Charsets.UTF_8)
    fun bytes(): ByteArray = body
}