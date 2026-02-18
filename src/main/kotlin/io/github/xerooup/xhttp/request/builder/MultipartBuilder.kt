package io.github.xerooup.xhttp.request.builder

import io.github.xerooup.xhttp.XHttpDsl

@XHttpDsl
class MultipartBuilder internal constructor() {
    private val boundary = "XHttpBoundary${System.currentTimeMillis()}"
    private val parts: MutableList<ByteArray> = mutableListOf()

    /**
     * Adds a plain text form field to the multipart body
     * @param name the form field name
     * @param value the form field value as a plain string
     */
    fun field(name: String, value: String) {
        val part = "--$boundary\r\n" +
                "Content-Disposition: form-data; name=\"$name\"\r\n\r\n" +
                "$value\r\n"
        parts.add(part.toByteArray(Charsets.UTF_8))
    }

    /**
     * Adds a binary file part to the multipart body
     * @param name the form field name
     * @param filename the filename to report in the Content-Disposition header
     * @param bytes the raw file content
     * @param contentType the MIME type of the file, e g image/png
     */
    fun file(name: String, filename: String, bytes: ByteArray, contentType: String) {
        val header = "--$boundary\r\n" +
                "Content-Disposition: form-data; name=\"$name\"; filename=\"$filename\"\r\n" +
                "Content-Type: $contentType\r\n\r\n"
        parts.add(header.toByteArray(Charsets.UTF_8) + bytes + "\r\n".toByteArray(Charsets.UTF_8))
    }

    internal fun build(): Pair<ByteArray, String> {
        val closing = "--$boundary--\r\n".toByteArray(Charsets.UTF_8)
        val body = parts.fold(ByteArray(0)) { acc, part -> acc + part } + closing
        return body to "multipart/form-data; boundary=$boundary"
    }
}