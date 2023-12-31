package se.redseven.ruuvitag;


/**
 * RAWv2 data format
 *
 * @param dataFormat                Offset 0: Data format definition (5 = current sensor readings)
 * @param temperature               Offset 1-2: Temperature in 0.005 degrees. Value: -32767…32767
 * @param humidity                  Offset 3-4: Humidity (16bit unsigned) in 0.0025% (0-163.83% range, though realistically 0-100%). Value: 0…40000
 * @param pressure                  Offset 5-6: Pressure (16bit unsigned) in 1 Pa units, with offset of -50 000 Pa. Value: 0…65534
 * @param accelerationX             Offset 7-8: Acceleration-X (Most Significant Byte first). Value: -32768…32767, Signed
 * @param accelerationY             Offset 9-10: Acceleration-Y (Most Significant Byte first). Value: -32768…32767, Signed
 * @param accelerationZ             Offset 11-12: Acceleration-Z (Most Significant Byte first). Value: -32768…32767, Signed
 * @param powerInfo                 Offset 13-14: Power info (11+5bit unsigned), first 11 bits is the battery voltage above 1.6V, in milli-volts (1.6V to 3.646V range). Last 5 bits unsigned are the TX power above -40dBm, in 2dBm steps. (-40dBm to +20dBm range). Value: 0…2046, 0…30
 * @param movementCounter           Offset 15: Movement counter (8 bit unsigned), incremented by motion detection interrupts from accelerometer. Value: 0…255
 * @param measurementSequenceNumber Offset 16-17: Measurement sequence number (16 bit unsigned), each time a measurement is taken, this is incremented by one, used for measurement de-duplication. Depending on the transmit interval, multiple packets with the same measurements can be sent, and there may be measurements that never were sent. Value: 0…65534
 * @param macAddress                Offset 18-23: MAC address (48bit unsigned), MAC address of the device that sent the data. Value: 4 bytes with 0…256
 *                                  <p>
 *                                  Not available is signified by largest presentable number for unsigned values,
 *                                  smallest presentable number for signed values and all bits set for mac.
 *                                  All fields are MSB first 2-complement, i.e. 0xFC18 is read as -1000 and
 *                                  0x03E8 is read as 1000. If original data overflows the data format,
 *                                  data is clipped to closest value that can be represented.
 *                                  For example temperature 170.00 C becomes 163.835 C and
 *                                  acceleration -40.000 G becomes -32.767 G.
 */
public record RAWv2(int dataFormat, float temperature, float humidity, int pressure, float accelerationX,
                    float accelerationY, float accelerationZ, float powerInfo, int txPower,
                    int movementCounter, int measurementSequenceNumber, byte[] macAddress) {
    /**
     * RAWv2 data format
     */
    static final byte RAW_V2 = 0x05;

}
