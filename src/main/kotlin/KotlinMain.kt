import com.inquallity.sandbox.JavaMain
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger

fun main(args: Array<String>) = KotlinMain().launch()
private const val IS_LOGGING_ENABLED = false
class KotlinMain {
    fun launch() {
        println("steps =========================>")
        println(printSteps(10))
        println("steps2 ========================>")
        println(printSteps2(10))
        println("steps3 ========================>")
        println(printSteps3(10))
        println("steps4 =========================>")
        println(printSteps4(10))
        println("steps5 =========================>")
        println(printSteps5(10))

    }

    private val iterations = AtomicInteger()
    private val thread = { Thread.currentThread().name }

    private fun log(msg: String) = if (IS_LOGGING_ENABLED) println(msg) else Unit

    fun printSteps(howMuch: Int): String {
        iterations.set(howMuch)
        return runBlocking {
            val sb = StringBuffer()
            launch(Dispatchers.IO) {
                while (iterations.decrementAndGet() >= 0) {
                    val left = launch {
                        log("Append left on [${thread()}]")
                        sb.append(JavaMain.LEFT)
                    }
                    left.join()
                    val right = launch {
                        log("Append right on [${thread()}")
                        sb.append(JavaMain.RIGHT)
                    }
                    right.join()

                }
            }.join()
            log(">>>>> return result on [${thread()}]")
            sb.toString()
        }
    }

    fun printSteps2(howMuch: Int): String = runBlocking {
        iterations.set(howMuch)
        val sb = StringBuffer()
        launch {
            while (iterations.decrementAndGet() >= 0) {
                val left = async(Dispatchers.Default) {
                    log("Append left on [${thread()}]")
                    sb.append(JavaMain.LEFT)
                }
                left.await()
                val right = async(Dispatchers.IO) {
                    log("Append right on [${thread()}]")
                    sb.append(JavaMain.RIGHT)
                }
                right.await()
            }
        }.join()
        log(">>>>> return result on [${thread()}]")
        sb.toString()
    }

    fun printSteps3(howMuch: Int): String = runBlocking {
        iterations.set(howMuch)
        val sb = StringBuffer()
        while (iterations.decrementAndGet() >= 0) {
            withContext(Dispatchers.Default) {
                log("Append left on [${thread()}]")
                sb.append(JavaMain.LEFT)
            }
            withContext(Dispatchers.IO) {
                log("Append right on [${thread()}]")
                sb.append(JavaMain.RIGHT)
            }
        }
        log(">>>>> return result on [${thread()}]")
        sb.toString()
    }

    fun printSteps4(howMuch: Int): String = runBlocking {
        iterations.set(howMuch)
        val sb = StringBuffer()
        newSingleThreadContext("Ctx1").use { ctx1 ->
            newSingleThreadContext("Ctx2").use { ctx2 ->
                while (iterations.decrementAndGet() >= 0) {
                    runBlocking(ctx1) {
                        log("Append left on [${thread()}]")
                        sb.append(JavaMain.LEFT)
                        withContext(ctx2) {
                            log("Append right on [${thread()}]")
                            sb.append(JavaMain.RIGHT)
                        }
                    }
                }
            }
        }
        log(">>>>> return result on [${thread()}]")
        sb.toString()
    }

    fun printSteps5(howMuch: Int): String = runBlocking {
        iterations.set(howMuch)
        val sb = StringBuffer()
        newSingleThreadContext("ctx1").use { ctx1 ->
            newSingleThreadContext("ctx2").use { ctx2 ->
                val left: Flow<String> = sequence {
                    var i = howMuch
                    while (i-- > 0) {
                        log("Yield left on [${thread()}]")
                        yield(JavaMain.LEFT)
                    }
                }.asFlow().flowOn(ctx1)
                val right = sequence {
                    var i = howMuch
                    while (i-- > 0) {
                        log("Yield right on [${thread()}]")
                        yield(JavaMain.RIGHT)
                    }
                }.asFlow().flowOn(ctx2)
                left.zip(right) { l, r -> l + r }.collect(sb::append)

                log(">>>>> return result on [${thread()}]")
                sb.toString()
            }
        }
    }
}