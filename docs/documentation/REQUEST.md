## Sending web requests
### GET
```kt
get("https://api.example.com/users")

get("https://api.example.com/users") {
    queryParams {
        "page" to "1"
        "limit" to "50"
    }
}

get("https://api.example.com/users") {
    headers {
        "Authorization" to "Bearer token"
        "Accept" to "application/json"
    }
}
```
### POST
```kt 
post("https://api.example.com/users") {
    json("""{"name": "John", "age": 30}""")
}

post("https://api.example.com/log") {
    text("something happened")
}

post("https://api.example.com/upload") {
    body(byteArrayOf(1, 2, 3), "application/octet-stream")
}
```
### multipart
```kt 
// file from disk
post("https://api.example.com/upload") {
    multipart {
        field("description", "my photo")
        file("photo", "photo.jpg", File("photo.jpg").readBytes(), "image/jpeg")
    }
}

// file from resources
post("https://api.example.com/upload") {
    multipart {
        field("description", "my photo")
        val bytes = object {}.javaClass.getResourceAsStream("/photo.jpg")!!.readBytes()
        file("photo", "photo.jpg", bytes, "image/jpeg")
    }
}
```
### timeout, retry, onError
```kt
get("https://api.example.com/users") {
    timeout(5000)
}

get("https://api.example.com/users") {
    timeout(5.seconds) // kotlin.time.Duration
}

get("https://api.example.com/users") {
    retry(times = 3, delayMs = 1000)
}

get("https://api.example.com/users") {
    onError { error -> 
        println("Error: ${error.status} ${error.text()}")
    }
}
```
### client
```kt 
val client = XHttpClient {
    baseUrl = "https://api.example.com"
    defaultHeader("Authorization", "Bearer token")
    defaultHeader("Accept", "application/json")
}

client.get("/users")
client.post("/users") { json("""{"name": "John"}""") }
client.put("/users/1") { json("""{"name": "John"}""") }
client.delete("/users/1")
```
### response
```kt 
val res = get("https://api.example.com/users")

res.status     // e.g. 200
res.ok         // true if 200-299
res.text()     // body as String
res.bytes()    // body as ByteArray
res.headers    // Map<String, String>
```