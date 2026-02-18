package io.github.xerooup.xhttp.websocket

import io.github.xerooup.xhttp.XHttpDsl
import io.github.xerooup.xhttp.request.builder.HeadersBuilder

@XHttpDsl
class WebSocketBuilder internal constructor() {
    internal var onOpen: ((WebSocketSender) -> Unit)? = null
    internal var onMessage: ((String) -> Unit)? = null
    internal var onBinary: ((ByteArray) -> Unit)? = null
    internal var onClose: ((Int, String) -> Unit)? = null
    internal var onError: ((Throwable) -> Unit)? = null
    internal val headers: MutableMap<String, String> = linkedMapOf()

    /**
     * Registers a callback invoked when the connection is successfully established
     * @param block lambda receiving a WebSocketSender to send messages immediately on open
     */
    fun onOpen(block: (WebSocketSender) -> Unit) {
        onOpen = block
    }

    /**
     * Registers a callback invoked when a text message is received
     * @param block lambda receiving the message as a String
     */
    fun onMessage(block: (String) -> Unit) {
        onMessage = block
    }

    /**
     * Registers a callback invoked when a binary message is received
     * @param block lambda receiving the message as a ByteArray
     */
    fun onBinary(block: (ByteArray) -> Unit) {
        onBinary = block
    }

    /**
     * Registers a callback invoked when the connection is closed
     * @param block lambda receiving the close status code and reason string
     */
    fun onClose(block: (code: Int, reason: String) -> Unit) {
        onClose = block
    }

    /**
     * Registers a callback invoked when an error occurs on the connection
     * @param block lambda receiving the thrown Throwable
     */
    fun onError(block: (Throwable) -> Unit) {
        onError = block
    }

    /**
     * Configures handshake headers using a nested DSL block
     * @param block lambda with access to HeadersBuilder where headers can be defined
     */
    fun headers(block: HeadersBuilder.() -> Unit) {
        headers.putAll(HeadersBuilder().apply(block).headers)
    }

    /**
     * Adds a single header to the WebSocket handshake request
     * @param name the header field name
     * @param value the header field value
     */
    fun header(name: String, value: String) {
        headers[name] = value
    }
}