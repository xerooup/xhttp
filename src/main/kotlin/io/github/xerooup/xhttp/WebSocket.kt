package io.github.xerooup.xhttp

import io.github.xerooup.xhttp.websocket.WebSocketBuilder
import io.github.xerooup.xhttp.websocket.WebSocketSession

/**
 * Opens a WebSocket connection to the specified URL and configures it via a DSL block
 * @param url the WebSocket endpoint URL, must start with ws:// or wss://
 * @param block lambda with access to WebSocketBuilder where callbacks can be registered
 * @return a WebSocketSession that can be used to send messages or wait for closure
 */
fun websocket(url: String, block: WebSocketBuilder.() -> Unit): WebSocketSession {
    val builder = WebSocketBuilder().apply(block)
    return WebSocketSession(
        url = url,
        headers = builder.headers,
        onOpen = builder.onOpen,
        onMessage = builder.onMessage,
        onBinary = builder.onBinary,
        onClose = builder.onClose,
        onError = builder.onError
    )
}