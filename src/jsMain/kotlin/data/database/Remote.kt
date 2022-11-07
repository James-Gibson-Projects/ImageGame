package data.database

import io.ktor.client.*

interface Remote{
    val client: HttpClient
}