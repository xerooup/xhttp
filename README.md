# xhttp
A small and minimalistic API for communicating with the internet.<br>
If you just need to send a request or open a websocket, but are tired of monsters - xhttp does exactly what you expect and nothing extra.

## Installation
`build.gradle.kts` example:
```kt 
repositories {
    mavenCentral()
}
dependencies {
    implementation("io.github.xerooup:xhttp:ACTUAL_VERSION")
}
```
or `pom.xml`:
```xml 
<dependencies>
    <dependency>
        <groupId>io.github.xerooup</groupId>
        <artifactId>xhttp</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

### Getting started
To get started with **xhttp**, go to the [documentation](https://xerooup.github.io/xhttp).

### Minimal code
```kt 
fun main() {
    val response = post("https://discord.com/api/webhooks/...") {
        json("""{"content": "Hello from xhttp!"}""")
        onError { error ->
            println("Error: ${error.status}") 
        }
    }

    if (response.ok) {
        println("Success!")
    }
}
```