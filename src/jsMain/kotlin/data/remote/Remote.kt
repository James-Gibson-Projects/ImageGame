package data.remote

import io.ktor.client.*

interface Remote{
    val client: HttpClient
}