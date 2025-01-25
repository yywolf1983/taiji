package top.nones.app;

import org.junit.Test;
import static org.junit.Assert.*;

public class HexagramUtilsTest {
    @Test
    public void testGenerateHexagramLines() {
        int[] lines = HexagramUtils.generateHexagramLines(0);
        assertEquals("111111", HexagramUtils.getHexagramBinary(0));
        assertTrue(HexagramUtils.validateHexagram(0, "111111"));
    }

    @Test
    public void testGenerateHexagramLines2() {
        int[] lines = HexagramUtils.generateHexagramLines(1);
        assertEquals("000000", HexagramUtils.getHexagramBinary(1));
        assertTrue(HexagramUtils.validateHexagram(1, "000000"));
    }
}