package com.tc.bluetoothlock;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        double a = 64 / 100.0;
        System.out.println("" + a);
    }

    @Test
    public void hexStr() {
        String str = "123456";
        int index = 0;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i < str.length() + 1; i++) {
            if (i % 2 == 0) {
                stringBuffer.append(str.substring(index, i));
                stringBuffer.append(",");
                index = i;
            }
        }
        stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        System.out.println(stringBuffer.toString());
    }

}