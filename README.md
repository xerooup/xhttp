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
    .post("URL") // the link to which the web request will be sent
    // or: .get(), put(), delete()
    
    .header("name", "value") // add header
    // or:
    .headers(mapOf(
        "name" to "value"
    )) // add headers using a map
    
    .body("JSON", "application/json") // request body 
    // or: .body("TEXT", "text/plain")
    
    .timeout(2000) // how many milliseconds are given for response
    .send() // sending our request

response.status // get status code
response.text() // get response text (or .bytes())
response.headers // get response headers
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