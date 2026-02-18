package io.github.xerooup.xhttp.websocket

import java.net.URI
import java.net.http.HttpClient
import java.net.http.WebSocket
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

/**
 * Represents an active WebSocket connection, returned from the websocket { } DSL function
 */
class WebSocketSession internal constructor(
    private val url: String,
    private val headers: Map<String, String>,
    private val onOpen: ((WebSocketSender) -> Unit)?,
    private val onMessage: ((String) -> Unit)?,
    private val onBinary: ((ByteArray) -> Unit)?,
    private val onClose: ((Int, String) -> Unit)?,
    private val onError: ((Throwable) -> Unit)?
) {
    private val done = CompletableFuture<Void?>()
    private val textBuffer = StringBuilder()
    private val binaryBuffer = mutableListOf<ByteArray>()
    private lateinit var ws: WebSocket

    private val sender = object : WebSocketSender {
        override fun send(text: String) {
            ws.sendText(text, true)
        }
        override fun send(bytes: ByteArray) {
            ws.sendBinary(ByteBuffer.wrap(bytes), true)
        }
        override fun close() {
            ws.sendClose(WebSocket.NORMAL_CLOSURE, "")
        }
    }

    init {
        val client = HttpClient.newHttpClient()

        val builder = client.newWebSocketBuilder()
        for ((k, v) in headers) builder.header(k, v)

        builder.buildAsync(URI.create(url), object : WebSocket.Listener {

            override fun onOpen(webSocket: WebSocket) {
                ws = webSocket
                onOpen?.invoke(sender)
                webSocket.request(1)
            }

            override fun onText(webSocket: WebSocket, data: CharSequence, last: Boolean): CompletionStage<*>? {
                textBuffer.append(data)
                if (last) {
                    onMessage?.invoke(textBuffer.toString())
                    textBuffer.clear()
                }
                webSocket.request(1)
                return null
            }

            override fun onBinary(webSocket: WebSocket, data: ByteBuffer, last: Boolean): CompletionStage<*>? {
                val bytes = ByteArray(data.remaining())
                data.get(bytes)
                binaryBuffer.add(bytes)
                if (last) {
                    val full = binaryBuffer.fold(ByteArray(0)) { acc, b -> acc + b }
                    onBinary?.invoke(full)
                    binaryBuffer.clear()
                }
                webSocket.request(1)
                return null
            }

            override fun onClose(webSocket: WebSocket, statusCode: Int, reason: String): CompletionStage<*>? {
                onClose?.invoke(statusCode, reason)
                done.complete(null)
                return null
            }

            override fun onError(webSocket: WebSocket, error: Throwable) {
                onError?.invoke(error)
                done.completeExceptionally(error)
            }
        })
    }

    /**
     * Sends a text message to the remote endpoint
     * @param text the message to send as a UTF-8 string
     */
    fun send(text: String) = sender.send(text)

    /**
     * Sends a binary message to the remote endpoint
     * @param bytes the message to send as a raw byte array
     */
    fun send(bytes: ByteArray) = sender.send(bytes)

    /**
     * Closes the WebSocket connection gracefully
     */
    fun close() = sender.close()

    /**
     * Blocks the current thread until the connection is closed
     */
    fun join() = done.join()
}