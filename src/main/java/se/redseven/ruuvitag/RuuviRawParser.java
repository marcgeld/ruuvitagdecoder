package se.redseven.ruuvitag;

import se.redseven.ruuvitag.exception.DataFormatException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static java.lang.String.format;

/**
 * Ruuvitag RAW parser, parses the manufacturer data from RuuviTag.
 */
public final class RuuviRawParser {

    /**
     * Ruuvi manufacturer id.
     */
    static final int MANUFACTURER_ID = 0x0499;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private RuuviRawParser() {
    }

    /**
     * Parse RAWv1 data format 3.
     *
     * @param rawData the raw data
     * @return the RAWv1 object
     * @throws DataFormatException if the data is not valid
     */
    public static RAWv1 parseRAWv1(byte[] rawData) throws DataFormatException {
        final ByteBuffer buf = ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN);
        checkManufacturerData(buf);

        byte dataFormat = buf.get();
        if (dataFormat != 3) {
            throw new DataFormatException("Data format code must be 3 for RAWv1");
        }
        float humidity = (buf.get() & 0xFF) * 0.5f;
        float temperature = convertTemperatureRaw1(buf.get(), buf.get());
        buf.order(ByteOrder.BIG_ENDIAN);
        int pressure = (buf.getShort() & 0xFFFF) + 50000;
        float accelerationX = buf.getShort() / 1000.0f;
        float accelerationY = buf.getShort() / 1000.0f;
        float accelerationZ = buf.getShort() / 1000.0f;
        float batteryVoltage = (0xFFFF & buf.getShort()) / 1000.0f;
        return new RAWv1(dataFormat, humidity, temperature, pressure, accelerationX, accelerationY, accelerationZ, batteryVoltage);
    }

    /**
     * Parse RAWv2 data format 5.
     *
     * @param rawData the raw data
     * @return the RAWv2 object
     * @throws DataFormatException if the data is not valid
     */
    public static RAWv2 parseRAWv2(byte[] rawData) throws DataFormatException {
        final ByteBuffer buf = ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN);
        checkManufacturerData(buf);

        byte dataFormat = buf.get();
        if (dataFormat != 5) {
            throw new DataFormatException("Data format code must be 5 for RAWv2");
        }
        buf.order(ByteOrder.BIG_ENDIAN);
        float temperature = buf.getShort() * 0.005f;
        float humidity = (buf.getShort() & 0xFFFF) * 0.0025f;
        int pressure = (buf.getShort() & 0xFFFF) + 50000;
        float accelerationX = buf.getShort() / 1000.0f;
        float accelerationY = buf.getShort() / 1000.0f;
        float accelerationZ = buf.getShort() / 1000.0f;

        final int pwr = buf.getShort();
        float powerInfo = (((pwr >>> 5) & 0x7FF) + 1600f) / 1000f;
        int txPower = ((pwr & 0x1F) * 2) - 40;
        int movementCounter = buf.get() & 0xFF;
        int measurementSequenceNumber = buf.getShort() & 0xFFFF;
        byte[] macAddress = new byte[6];
        buf.get(macAddress);
        return new RAWv2(dataFormat, temperature, humidity, pressure, accelerationX, accelerationY, accelerationZ, powerInfo, txPower,
                movementCounter, measurementSequenceNumber, macAddress);
    }

    /**
     * Check that the manufacturer data is correct.
     *
     * @param buf the byte buffer
     * @throws DataFormatException if the manufacturer data is not correct
     */
    private static void checkManufacturerData(ByteBuffer buf) throws DataFormatException {
        int manufacturerId = Short.reverseBytes(buf.getShort()); // manufacturerId
        if (manufacturerId != MANUFACTURER_ID) {
            throw new DataFormatException(format("Manufacturer is wrong '0x%04X' must be '0x%04X'",
                    manufacturerId, MANUFACTURER_ID));
        }
    }

    /**
     * Convert temperature raw value to float.
     *
     * @param value   the raw value
     * @param decimal the decimal value
     * @return the temperature
     */
    public static float convertTemperatureRaw1(byte value, byte decimal) {
        float val = (value & 0x7F) + ((decimal & 0xff) / 100.0f);
        // If the sign bit is set, the number is negative
        if ((value & 0x80) != 0) {
            return -val;
        }
        return val;
    }
}
