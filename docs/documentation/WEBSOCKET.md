## Websocket
### connect
```kt 
websocket("wss://echo.websocket.org") {
    onOpen { sender ->
        sender.send("Hello!")
    }
    onMessage { message ->
        println("Text: $message")
    }
    onClose { code, reason ->
        println("Closed: $code $reason")
    }
}
```
### binary
```kt 
websocket("wss://echo.websocket.org") {
    onOpen { sender ->
        sender.send(byteArrayOf(1, 2, 3))
    }
    onBinary { bytes ->
        println("Binary: ${bytes.size} bytes")
    }
}
```
### headers
```kt 
websocket("wss://echo.websocket.org") {
    header {
        "Authorization" to "Bearer TOKEN"
    }
    onMessage { message ->
        println("Text: $message")
    }
}
```
### error
```kt 
websocket("wss://echo.websocket.org") {
    onError { error ->
        println("Error: ${error.message}")
    }
}
```
### session
```kt 
val ws = websocket("wss://echo.websocket.org") {
    onMessage { message -> println("Text: $message") }
    onClose { code, reason -> println("Closed: $code $reason") }
}

ws.send("Hello!")              // send text
ws.send(byteArrayOf(1, 2, 3))  // send binary
ws.close()                     // close connection
ws.join()                      // block until closed
```