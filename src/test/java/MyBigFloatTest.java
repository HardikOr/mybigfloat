import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyBigFloatTest {
    @Test
    public void fromintTest(){
        assertEquals("10.0", new MyBigFloat(10).toString());
        assertEquals("12365478.0", new MyBigFloat(12365478).toString());
        assertEquals("0.0", new MyBigFloat(0).toString());
    }

    @Test
    public void fromfloatTest(){
        assertEquals("2.5", new MyBigFloat(2.5F).toString());
        assertEquals("-25.0", new MyBigFloat(-25F).toString());
        assertEquals("30000000000000000000000000.0", new MyBigFloat(3E25F).toString());
    }

    @Test
    public void fromstringTest(){
        assertEquals("-123.123", new MyBigFloat("-123.123").toString());
        assertEquals("0.0", new MyBigFloat("0.0").toString());
        assertEquals("0.0", new MyBigFloat("0").toString());
    }

    @Test
    public void plusTest(){
        assertEquals("3.0", new MyBigFloat("1.11").plus(new MyBigFloat("1.89")).toString());
        assertEquals("0.099", new MyBigFloat("0.09").plus(new MyBigFloat("0.009")).toString());
        assertEquals("1001.0", new MyBigFloat("1000.1").plus(new MyBigFloat("0.9")).toString());
    }

    @Test
    public void minusTest(){
        assertEquals("1.0", new MyBigFloat("1.11").minus(new MyBigFloat("0.11")).toString());
        assertEquals("-11.5", new MyBigFloat("-10").minus(new MyBigFloat("1.5")).toString());
        assertEquals("5000.0", new MyBigFloat("-10000.10987").minus(new MyBigFloat("-15000.10987")).toString());
    }

    @Test
    public void multiplyTest(){
        assertEquals("3.0", new MyBigFloat("2.5").multiply(new MyBigFloat("1.2")).toString());
        assertEquals("0.0", new MyBigFloat("0").multiply(new MyBigFloat("-1.2")).toString());
        assertEquals("0.333295", new MyBigFloat("-66659").multiply(new MyBigFloat("-0.000005")).toString());
        assertEquals("0.314", new MyBigFloat("3.14").multiply(new MyBigFloat("0.1")).toString());
        assertEquals("3140000000.0", new MyBigFloat("3.14").multiply(new MyBigFloat("1000000000")).toString());
    }

    @Test
    public void toIntTest(){
        assertEquals(10, new MyBigFloat("10.1012398487123124577345346").toInt());
        assertEquals(-101123456, new MyBigFloat("-101123456.1012398487123124577345346").toInt());
        assertEquals(0, new MyBigFloat("-0.1012345346").toInt());
    }

    @Test
    public void toFloatTest(){
        assertEquals(10.10F, new MyBigFloat("10.10").toFloat(), 0.000001);
        assertEquals(-1F, new MyBigFloat("-1.0").toFloat(), 0.000001);
        assertEquals(-123.321F, new MyBigFloat("-123.321").toFloat(), 0.000001);
    }

    @Test
    public void roundTest(){
        assertEquals("19.45679", new MyBigFloat("19.4567891").round(5).toString());
        assertEquals("20.0", new MyBigFloat("19.99999").round(2).toString());
        assertEquals("1000.0", new MyBigFloat("999.999").round(2).toString());
    }

    @Test
    public void equalsTest(){
        assertEquals(new MyBigFloat("123"), new MyBigFloat("123"));
        assertEquals(new MyBigFloat(123), new MyBigFloat("123.0"));
        assertEquals(new MyBigFloat(100.0F), new MyBigFloat("100.001").round(0));
    }

    @Test
    public void exceptionsTest(){
        String str = null;

        assertThrows(NumberFormatException.class, () -> new MyBigFloat("1a"));
        assertThrows(IllegalArgumentException.class, () -> new MyBigFloat(str));
        assertThrows(IllegalArgumentException.class, () -> new MyBigFloat(""));

        assertThrows(IllegalArgumentException.class, () -> new MyBigFloat(Integer.MAX_VALUE).plus(new MyBigFloat(1)).toInt());
        assertThrows(IllegalArgumentException.class, () -> new MyBigFloat(Integer.MIN_VALUE).minus(new MyBigFloat(1)).toInt());

        assertThrows(IllegalArgumentException.class, () -> new MyBigFloat(Float.MAX_VALUE).plus(new MyBigFloat(1)).toFloat());
        assertThrows(IllegalArgumentException.class, () -> new MyBigFloat(-Float.MAX_VALUE).minus(new MyBigFloat(1)).toFloat());
    }
}
