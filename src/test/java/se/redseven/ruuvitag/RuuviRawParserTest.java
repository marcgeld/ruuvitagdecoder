package se.redseven.ruuvitag;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuuviRawParserTest {
    /**
     * 26 / 100.0f is 0.25999999046325684 due to floating point precision, use delta to compare floats
     */
    float A_DELTA = 0.005f;

    /**
     * Test transforming temperature raw data to a float.
     * 26 / 100.0f is 0.25999999046325684 due to floating point precision,
     * using delta to compare floats!
     */
    @Test
    void test_that_temp_conversion_gives_reasonable_results() {
        final float temp = RuuviRawParser.convertTemperatureRaw1((byte) 0x1A, (byte) 0x1E);
        assertEquals(26.3, temp, A_DELTA);
    }
}