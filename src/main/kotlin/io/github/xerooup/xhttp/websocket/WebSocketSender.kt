package io.github.xerooup.xhttp.websocket

/**
 * Provides methods to send data over an active WebSocket connection
 */
interface WebSocketSender {

    /**
     * Sends a text message to the remote endpoint
     * @param text the message to send as a UTF-8 string
     */
    fun send(text: String)

    /**
     * Sends a binary message to the remote endpoint
     * @param bytes the message to send as a raw byte array
     */
    fun send(bytes: ByteArray)

    /**
     * Closes the WebSocket connection gracefully with a normal closure code
     */
    fun close()
}