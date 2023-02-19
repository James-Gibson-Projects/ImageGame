package data

import io.ktor.client.*
import io.ktor.client.engine.js.*

actual fun getClient(): HttpClient = defaultClient