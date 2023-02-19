package exceptions

import io.ktor.client.statement.*

class CustomResponseException(response: HttpResponse, s: String) : Exception(s)
