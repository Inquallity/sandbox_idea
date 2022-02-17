import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MainTest {
    private val main = KotlinMain()

    @Test
    fun testOutput() {
        val howMuch = 8_000_000
        val result = main.printSteps3(howMuch)
        val expected = "[--]".repeat(howMuch)
        Assertions.assertEquals(expected, result, "Result is wrong")
    }
}