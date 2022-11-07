package data.database

import data.defaultClient

class RemoteImpl: Remote {
    override val client = defaultClient
}