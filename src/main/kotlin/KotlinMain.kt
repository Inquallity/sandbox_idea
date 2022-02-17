import com.inquallity.sandbox.JavaMain
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

fun main(args: Array<String>) = KotlinMain().launch()

class KotlinMain {
    fun launch() = println(printSteps(100))

    private val iterations = AtomicInteger()
    fun printSteps(howMuch: Int): String {
        iterations.set(howMuch)
        return runBlocking {
            val sb = StringBuffer()
            launch(Dispatchers.IO) {
                while (iterations.decrementAndGet() >= 0) {
                    val left = launch { sb.append(JavaMain.LEFT) }
                    left.join()
                    val right = launch { sb.append(JavaMain.RIGHT) }
                    right.join()

                }
            }.join()
            return@runBlocking sb.toString()
        }
    }

    fun printSteps2(howMuch: Int): String = runBlocking {
        iterations.set(howMuch)
        val sb = StringBuffer()
        launch {
            while (iterations.decrementAndGet() >= 0) {
                val left = async(Dispatchers.Default) { sb.append(JavaMain.LEFT) }
                left.await()
                val right = async(Dispatchers.IO) { sb.append(JavaMain.RIGHT) }
                right.await()
            }
        }.join()
        sb.toString()
    }

    fun printSteps3(howMuch: Int): String = runBlocking {
        iterations.set(howMuch)
        val sb = StringBuffer()
        while (iterations.decrementAndGet() >= 0) {
            withContext(Dispatchers.Default) { sb.append(JavaMain.LEFT) }
            withContext(Dispatchers.IO) { sb.append(JavaMain.RIGHT) }
        }
        sb.toString()
    }

    fun printSteps4(howMuch: Int): String = runBlocking {
        iterations.set(howMuch)
        val sb = StringBuffer()
        newSingleThreadContext("Ctx1").use { ctx1 ->
            newSingleThreadContext("Ctx2").use { ctx2 ->
                while (iterations.decrementAndGet() >= 0) {
                    runBlocking(ctx1) {
                        sb.append(JavaMain.LEFT)
                        withContext(ctx2) {
                            sb.append(JavaMain.RIGHT)
                        }
                    }
                }
            }
        }
        sb.toString()
    }
}