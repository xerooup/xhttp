package io.github.xerooup.xhttp.response

// http response
class Response internal constructor(
    val status: Int,
    val headers: Map<String, String>,
    val ok: Boolean,
    private val body: ByteArray
) {
    /**
     * Returns the response body decoded as a UTF-8 string
     * @return body content as a String
     */
    fun text(): String = body.toString(Charsets.UTF_8)

    /**
     * Returns the raw response body as a byte array
     * @return body content as ByteArray
     */
    fun bytes(): ByteArray = body
}