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
import io.github.xerooup.xhttp.XHttp
import kotlin.time.Duration.Companion.seconds

val response = XHttp
    // available methods: get(), post(), put(), delete()
    .post("URL")
    
    // add a single HTTP header
    .header("name", "value")
    
    // or add multiple headers at once
    .headers(
        mapOf(
            "name" to "value"
        )
    )
    
    .body("JSON", "application/json")
    
    .timeout(2000)
    // or: 
    .timeout(2.seconds)
    
    .send()

// access response data
response.status
response.headers

response.ok // if request is successfully
response.text() // get a response in the text 
response.bytes() // or in the bytes
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