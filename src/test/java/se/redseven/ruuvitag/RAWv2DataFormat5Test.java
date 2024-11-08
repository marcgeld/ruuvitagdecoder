package se.redseven.ruuvitag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import se.redseven.ruuvitag.exception.DataFormatException;

import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.redseven.ruuvitag.RuuviRawParser.parseRAWv2;

/**
 * Test class for RAWv1 data format 3.
 */
class RAWv2DataFormat5Test {
    /**
     * 26 / 100.0f is 0.25999999046325684 due to floating point precision, use delta to compare floats
     */
    float A_DELTA = 0.005f;

    /**
     * Test parsing of invalid data format.
     *
     * @throws DataFormatException if data is invalid
     */
    @Test
    void test_invalid_data_format_exception() throws DataFormatException {
        final DataFormatException thrown = Assertions.assertThrows(DataFormatException.class, () -> {
            //Code under test
            final byte[] rawdata = HexFormat.of().parseHex("04990912FC5394C37C0004FFFC040CAC364200CDCBB8334C884F");
            parseRAWv2(rawdata);
        });
        Assertions.assertEquals("Data format code must be 5 for RAWv2", thrown.getMessage());
    }

    /**
     * Test parsing of invalid data format.
     *
     * @throws DataFormatException if data is invalid
     */
    @Test
    void test_invalid_manufacturer_data_exception() throws DataFormatException {
        final DataFormatException thrown = Assertions.assertThrows(DataFormatException.class, () -> {
            //Code under test
            final byte[] rawdata = HexFormat.of().parseHex("04970912FC5394C37C0004FFFC040CAC364200CDCBB8334C884F");
            parseRAWv2(rawdata);
        });
        Assertions.assertEquals("Manufacturer is wrong '0x0497' must be '0x0499'", thrown.getMessage());
    }

    /**
     * Test parsing of valid data.
     *
     * @throws DataFormatException if data is invalid
     */
    @Test
    void test_parse_valid_data() throws DataFormatException {
        final byte[] rawdata = HexFormat.of().parseHex("04990512FC5394C37C0004FFFC040CAC364200CDCBB8334C884F");
        RAWv2 rawv2 = parseRAWv2(rawdata);
        assertEquals(5, rawv2.dataFormat(), "DataFormat");
        assertEquals(24.3, rawv2.temperature(), A_DELTA, "Temperature");
        assertEquals(53.49, rawv2.humidity(), A_DELTA, "Humidity");
        assertEquals(100044, rawv2.pressure(), "Pressure");
        assertEquals(0.004, rawv2.accelerationX(), A_DELTA, "AccelerationX");
        assertEquals(-0.004, rawv2.accelerationY(), A_DELTA, "AccelerationY");
        assertEquals(1.036, rawv2.accelerationZ(), A_DELTA, "AccelerationZ");
        assertEquals(2.977, rawv2.powerInfo(), A_DELTA, "PowerInfo");
        assertEquals(4, rawv2.txPower(), "TxPower");
        assertEquals(66, rawv2.movementCounter(), "MovementCounter");
        assertEquals(205, rawv2.measurementSequenceNumber(), "MeasurementSequenceNumber");
        assertArrayEquals(HexFormat.ofDelimiter(":").parseHex("CB:b8:33:4c:88:4f"),
                rawv2.macAddress(), "MacAddress");
    }

    /**
     * Test parsing data with maximum values.
     *
     * @throws DataFormatException if data is invalid
     */
    @Test
    void test_parse_data_with_maximum_values() throws DataFormatException {
        final byte[] rawdata = HexFormat.of().parseHex("0499057FFFFFFEFFFE7FFF7FFF7FFFFFDEFEFFFECBB8334C884F");
        RAWv2 rawv2 = parseRAWv2(rawdata);
        assertEquals(5, rawv2.dataFormat(), "DataFormat");
        assertEquals(163.835, rawv2.temperature(), A_DELTA, "Temperature");
        assertEquals(163.8350, rawv2.humidity(), A_DELTA, "Humidity");
        assertEquals(115534, rawv2.pressure(), "Pressure");
        assertEquals(32.767, rawv2.accelerationX(), A_DELTA, "AccelerationX");
        assertEquals(32.767, rawv2.accelerationY(), A_DELTA, "AccelerationY");
        assertEquals(32.767, rawv2.accelerationZ(), A_DELTA, "AccelerationZ");
        assertEquals(3.646, rawv2.powerInfo(), A_DELTA, "PowerInfo");
        assertEquals(20, rawv2.txPower(), "TxPower");
        assertEquals(254, rawv2.movementCounter(), "MovementCounter");
        assertEquals(65534, rawv2.measurementSequenceNumber(), "MeasurementSequenceNumber");
        assertArrayEquals(HexFormat.ofDelimiter(":").parseHex("CB:b8:33:4c:88:4f"),
                rawv2.macAddress(), "MacAddress");
    }

    /**
     * Test parsing of data with minimum values.
     *
     * @throws DataFormatException if data is invalid
     */
    @Test
    void test_parse_data_with_minimum_values() throws DataFormatException {
        final byte[] rawdata = HexFormat.of().parseHex("0499058001000000008001800180010000000000CBB8334C884F");
        RAWv2 rawv2 = parseRAWv2(rawdata);
        assertEquals(5, rawv2.dataFormat());
        assertEquals(-163.835, rawv2.temperature(), A_DELTA);
        assertEquals(0.0, rawv2.humidity(), A_DELTA);
        assertEquals(50000, rawv2.pressure());
        assertEquals(-32.767, rawv2.accelerationX(), A_DELTA);
        assertEquals(-32.767, rawv2.accelerationY(), A_DELTA);
        assertEquals(-32.767, rawv2.accelerationZ(), A_DELTA);
        assertEquals(1.6, rawv2.powerInfo(), A_DELTA);
        assertEquals(-40, rawv2.txPower());
        assertEquals(0, rawv2.movementCounter());
        assertEquals(0, rawv2.measurementSequenceNumber());
        assertArrayEquals(HexFormat.ofDelimiter(":").parseHex("CB:b8:33:4c:88:4f"),
                rawv2.macAddress(), "MacAddress");
    }
}