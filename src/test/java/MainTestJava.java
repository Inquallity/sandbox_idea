import com.inquallity.sandbox.JavaMain;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainTestJava {
    private static final int TEST_STEPS = 10;

    @Test
    public void testRepeatFunction() {
        final String result = repeat("*", 10);
        final String expected = "**********";
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void javaPrintSteps() {
        final String result = new JavaMain().printSteps(TEST_STEPS);
        final String expected = repeat(JavaMain.LEFT + JavaMain.RIGHT, TEST_STEPS);
        Assertions.assertEquals(expected, result, "Result is incorrect");
    }

    @Test
    public void javaPrintSteps2() {
        final String result = new JavaMain().printSteps2(TEST_STEPS);
        final String expected = repeat(JavaMain.LEFT + JavaMain.RIGHT, TEST_STEPS);
        Assertions.assertEquals(expected, result, "Result is incorrect");
    }

    @Test
    public void kotlinPrintSteps() {
        final String result = new KotlinMain().printSteps(TEST_STEPS);
        final String expected = repeat(JavaMain.LEFT + JavaMain.RIGHT, TEST_STEPS);
        Assertions.assertEquals(expected, result, "Result of printSteps is incorrect");
    }

    @Test
    public void kotlinPrintSteps2() {
        final String result = new KotlinMain().printSteps2(TEST_STEPS);
        final String expected = repeat(JavaMain.LEFT + JavaMain.RIGHT, TEST_STEPS);
        Assertions.assertEquals(expected, result, "Result of printSteps2 is incorrect");
    }

    @Test
    public void kotlinPrintSteps3() {
        final String result = new KotlinMain().printSteps3(TEST_STEPS);
        final String expected = repeat(JavaMain.LEFT + JavaMain.RIGHT, TEST_STEPS);
        Assertions.assertEquals(expected, result, "Result of printSteps3 is incorrect");
    }

    @Test
    public void kotlinPrintSteps4() {
        final String result = new KotlinMain().printSteps4(TEST_STEPS);
        final String expected = repeat(JavaMain.LEFT + JavaMain.RIGHT, TEST_STEPS);
        Assertions.assertEquals(expected, result, "Result of printSteps3 is incorrect");
    }

    @NotNull
    private String repeat(String str, int times) {
        final StringBuilder sb = new StringBuilder();
        for (int i = times; i > 0; i--) {
            sb.append(str);
        }
        return sb.toString();
    }
}
