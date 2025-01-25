public class HexagramUtilsTest {
    @Test
    public void testQianHexagram() {
        // 测试乾卦
        int[] lines = HexagramUtils.generateHexagramLines(0);
        assertEquals("111111", HexagramUtils.getHexagramBinary(0));
        assertTrue(HexagramUtils.validateHexagram(0, "111111"));
    }

    @Test
    public void testKunHexagram() {
        // 测试坤卦
        int[] lines = HexagramUtils.generateHexagramLines(1);
        assertEquals("000000", HexagramUtils.getHexagramBinary(1));
        assertTrue(HexagramUtils.validateHexagram(1, "000000"));
    }
} 