package data.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StockFish(poolSize: Int = 3): ChessEngine {
    private val connectionPool = Channel<Process>(poolSize)
    private val scope = CoroutineScope(Dispatchers.IO)
    init {
        scope.launch {
            for (i in 1..poolSize){
                connectionPool.send(withContext(Dispatchers.IO) {
                    ProcessBuilder("stockfish").start()
                })
            }
        }
    }
    override suspend fun getMove(fen: String, allowedTimeMs: Int): String = withContext(Dispatchers.IO) {
        val stockFish = connectionPool.receive()
        val output = stockFish.inputStream.bufferedReader()
        val input = stockFish.outputStream.bufferedWriter()
        input.write("position $fen\n")
        input.write("go depth 10\n")
        input.flush()
        val response = output.readLine()
        connectionPool.send(stockFish)
        response
    }
}