package se.redseven.ruuvitag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.redseven.ruuvitag.exception.DataFormatException;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.redseven.ruuvitag.RuuviRawParser.parseRAWv1;

/**
 * Test class for RAWv1 data format 3
 */
class RAWv1DataFormat3Test {
    /**
     * 26 / 100.0f is 0.25999999046325684 due to floating point precision, use delta to compare floats
     */
    float A_DELTA = 0.005f;

    /**
     * Test parsing of invalid data format
     */
    @Test
    void test_invalid_data_format_exception() {
        final DataFormatException thrown = Assertions.assertThrows(DataFormatException.class, () -> {
            //Code under test
            final byte[] rawdata = HexFormat.of().parseHex("04990512FC5394C37C0004FFFC040CAC364200CDCBB8334C884F");
            parseRAWv1(rawdata);
        });
        Assertions.assertEquals("Data format code must be 3 for RAWv1", thrown.getMessage());
    }

    /**
     * Test parsing of valid data
     *
     * @throws DataFormatException
     */
    @Test
    void test_parse_valid_data() throws DataFormatException {
        final byte[] rawdata = HexFormat.of().parseHex("049903291A1ECE1EFC18F94202CA0B53");
        RAWv1 rawv1 = parseRAWv1(rawdata);
        assertEquals(3, rawv1.dataFormat(), "DataFormat");
        assertEquals(20.5, rawv1.humidity(), A_DELTA, "Humidity");
        assertEquals(26.3, rawv1.temperature(), A_DELTA, "Temperature");
        assertEquals(102766, rawv1.pressure(), "Pressure");
        assertEquals(-1.000, rawv1.accelerationX(), A_DELTA, "AccelerationX");
        assertEquals(-1.726, rawv1.accelerationY(), A_DELTA, "AccelerationY");
        assertEquals(0.714, rawv1.accelerationZ(), A_DELTA, "AccelerationZ");
        assertEquals(2.899, rawv1.batteryVoltage(), A_DELTA, "BatteryVoltage");
    }

    /**
     * Test parsing data with maximum values
     */
    @Test
    void test_parse_data_with_maximum_values() throws DataFormatException {
        final byte[] rawdata = HexFormat.of().parseHex("049903FF7F63FFFF7FFF7FFF7FFFFFFF");
        RAWv1 rawv1 = parseRAWv1(rawdata);
        assertEquals(3, rawv1.dataFormat(), "DataFormat");
        assertEquals(127.5, rawv1.humidity(), A_DELTA, "Humidity");
        assertEquals(127.99, rawv1.temperature(), A_DELTA, "Temperature");
        assertEquals(115535, rawv1.pressure(), "Pressure");
        assertEquals(32.767, rawv1.accelerationX(), A_DELTA, "AccelerationX");
        assertEquals(32.767, rawv1.accelerationY(), A_DELTA, "AccelerationY");
        assertEquals(32.767, rawv1.accelerationZ(), A_DELTA, "AccelerationZ");
        assertEquals(65.535, rawv1.batteryVoltage(), A_DELTA, "BatteryVoltage");
    }

    /**
     * Test parsing of data with minimum values
     */
    @Test
    void test_parse_data_with_minimum_values() throws DataFormatException {
        final byte[] rawdata = HexFormat.of().parseHex("04990300FF6300008001800180010000");
        RAWv1 rawv1 = parseRAWv1(rawdata);
        assertEquals(3, rawv1.dataFormat(), "DataFormat");
        assertEquals(0.0, rawv1.humidity(), A_DELTA, "Humidity");
        assertEquals(-127.99, rawv1.temperature(), A_DELTA, "Temperature");
        assertEquals(50000, rawv1.pressure(), "Pressure");
        assertEquals(-32.767, rawv1.accelerationX(), A_DELTA, "AccelerationX");
        assertEquals(-32.767, rawv1.accelerationY(), A_DELTA, "AccelerationY");
        assertEquals(-32.767, rawv1.accelerationZ(), A_DELTA, "AccelerationZ");
        assertEquals(0.000, rawv1.batteryVoltage(), A_DELTA, "BatteryVoltage");
    }
}