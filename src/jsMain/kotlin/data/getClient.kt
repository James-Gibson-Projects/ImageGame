package data

import defaultClient
import io.ktor.client.*

actual fun getClient(): HttpClient {
    return defaultClient
}