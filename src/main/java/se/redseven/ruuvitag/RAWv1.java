package se.redseven.ruuvitag;

/**
 * Ruuvitag RAWv1 data format.
 *
 * @param dataFormat     Offset 0: Data format definition (3 = current sensor readings)
 * @param humidity       Offset 1: Humidity (one lsb is 0.5%, e.g. 128 is 64%) Values above 100% indicate a fault in
 *                       sensor. Value: 0…200
 * @param temperature    Offset 2,3: Temperature (MSB is sign, next 7 bits are decimal value), Temperature
 *                       (fraction, 1/100.). Value1: -127…127, signed, Value 2: 0…99, unsigned
 * @param pressure       Offset 4-5: Pressure (Most Significant Byte first, value - 50kPa). Value: 0…65535
 * @param accelerationX  Offset 6-7: Acceleration-X (Most Significant Byte first). Value: -32768…32767, Signed
 * @param accelerationY  Offset 8-9: Acceleration-Y (Most Significant Byte first). Value: -32768…32767, Signed
 * @param accelerationZ  Offset 10-11: Acceleration-Z (Most Significant Byte first). Value: -32768…32767, Signed
 * @param batteryVoltage Offset 12-13: Battery voltage (mV). Value: 0…65535
 */
public record RAWv1(int dataFormat, float humidity, float temperature, int pressure, float accelerationX,
                    float accelerationY, float accelerationZ, float batteryVoltage) {
    /**
     * Ruuvitag RAWv1 data format.
     */
    static final byte RAW_V1 = 0x03;
}
