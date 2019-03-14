import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class MyBigFloatTest {
    @Test
    public void myTest(){
        assertEquals("10.0", new MyBigFloat(10).toString());
        assertEquals("12365478.0", new MyBigFloat(12365478).toString());
        assertEquals("0.0", new MyBigFloat(0).toString());

        assertEquals("2.5", new MyBigFloat(2.5F).toString());
        assertEquals("-25.0", new MyBigFloat(-25F).toString());
        assertEquals("0.25", new MyBigFloat(0.25F).toString());

        assertEquals("-123.123", new MyBigFloat("-123.123").toString());
        assertEquals("0.0", new MyBigFloat("0.0").toString());
        assertEquals("0.0", new MyBigFloat("0").toString());

        assertEquals("3.0", new MyBigFloat("1.11").plus(new MyBigFloat("1.89")).toString());
        assertEquals("0.099", new MyBigFloat("0.09").plus(new MyBigFloat("0.009")).toString());
        assertEquals("1001.0", new MyBigFloat("1000.1").plus(new MyBigFloat("0.9")).toString());

        assertEquals("1.0", new MyBigFloat("1.11").minus(new MyBigFloat("0.11")).toString());
        assertEquals("-11.5", new MyBigFloat("-10").minus(new MyBigFloat("1.5")).toString());
        assertEquals("5000.0", new MyBigFloat("-10000.10987").minus(new MyBigFloat("-15000.10987")).toString());

        assertEquals("3.0", new MyBigFloat("2.5").multiply(new MyBigFloat("1.2")).toString());
        assertEquals("0.0", new MyBigFloat("0").multiply(new MyBigFloat("-1.2")).toString());
        assertEquals("0.333295", new MyBigFloat("-66659").multiply(new MyBigFloat("-0.000005")).toString());

        assertEquals(10, new MyBigFloat("10.1012398487123124577345346").toInt());
        assertEquals(-101123456, new MyBigFloat("-101123456.1012398487123124577345346").toInt());

        assertEquals(10.10F, new MyBigFloat("10.10").toFloat(), 0.00001);
        assertEquals(-1F, new MyBigFloat("-1.0").toFloat(), 0.00001);

        assertEquals("19.45679", new MyBigFloat("19.4567891").round(5).toString());
        assertEquals("20.0", new MyBigFloat("19.99999").round(2).toString());
    }
}
