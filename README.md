# xhttp

![Kotlin](https://img.shields.io/badge/Kotlin-2.2.21-blue)
![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)
![Java](https://img.shields.io/badge/Java-21-red)
<br><br>

a small and minimal http api for creating and handling web requests.

if you just need to send a simple http request and are tired of overloaded APIs,<br>
xhttp focuses on simplicity and predictability.

### getting started
you don't need documentation or long guides.<br>
just look at the example below - everything is straightforward:
```kt
import org.xeroup.xhttp.XHttp

val response = XHttp
    .post("URL")
    // HTTP method and target URL
    // available methods: get(), post(), put(), delete()

    .header("name", "value")
    // add a single HTTP header

    // or add multiple headers at once
    .headers(
        mapOf(
            "name" to "value"
        )
    )

    .body("JSON", "application/json")
    // request body and its MIME type
    // examples: JSON -> application/json, plain text -> text/plain

    .timeout(2000)
    // request timeout in milliseconds

    .send()
// send the request and receive the response

// access response data
response.status // HTTP status code (e.g. 200, 404, 500)
response.ok // if the request is successful returns true
response.text() // response body as a String (or use response.bytes())
response.headers // response headers
```

### how to install:
`build.gradle.kts` example:
```kt 
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.xerooup:xhttp:ACTUAL_VERSION")
}
```