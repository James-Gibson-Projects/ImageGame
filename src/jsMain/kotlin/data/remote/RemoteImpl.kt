package data.remote

import data.defaultClient

class RemoteImpl: Remote {
    override val client = defaultClient
}