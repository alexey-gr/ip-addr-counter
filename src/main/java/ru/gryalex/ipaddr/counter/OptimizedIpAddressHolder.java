package ru.gryalex.ipaddr.counter;

import java.util.Arrays;

/**
 * Storage of unique IPv4 addresses:
 * - Optimized for minimal memory consumption.
 * - Occupies approximately ~262k to ~540mb depending on the number of unique IP addresses.
 * - Currently lacks input validation; this feature can be added later if necessary.
 * - All operations are performed in a single thread; potential for parallelization exists to enhance processing speed.
 * <p>
 * Implementation details:
 * - Data is stored in a two-dimensional array of bytes (byte[][]).
 * - Each IP address is encoded as a 32-bit value.
 * - The leftmost 16 bits are used as the first index in the array.
 * - The middle 13 bits are used as the second index in the array.
 * - The rightmost 3 bits are encoded as the position of '1' in the byte value of the array.
 * - Shifting the byte value in the array by Byte.MIN_VALUE is necessary due to the signed nature of byte in Java.
 */
public class OptimizedIpAddressHolder implements IpAddressHolder {

    private static final int LEFT_16_BITS_RANGE = (int) Math.pow(2, 16);
    private static final int MIDDLE_13_BITS_RANGE = (int) Math.pow(2, 13);

    private final byte[][] holder = new byte[LEFT_16_BITS_RANGE][];

    @Override
    public void add(String ipAddress) {
        // encode ip address
        long ipAddressBits32 = encodeIpAddress(ipAddress);
        int rightBits16 = (int) (ipAddressBits32 & 0xFFFF);

        int leftBits16 = (int) (ipAddressBits32 >> 16);
        int middleBits13 = rightBits16 >> 3;
        int rightBits3 = rightBits16 & 0b111;

        // get bucket by left 16 bits of ip address
        var bucket = retrieveBucket(leftBits16);

        // get value by middle 13 bits of ip address
        int value = bucket[middleBits13];
        value -= Byte.MIN_VALUE;

        // right 3 bits of ip address equals 8 in decimal - we encode them as a position of '1' bit in byte value
        value = applyBitmask(rightBits3, value);
        value += Byte.MIN_VALUE;
        bucket[middleBits13] = (byte) (value);
    }

    @Override
    public long calculateUniqueCount() {
        long count = 0;

        for (byte[] bucket : holder) {
            if (bucket != null) {
                for (int value : bucket) {
                    value = value - Byte.MIN_VALUE;
                    count += Integer.bitCount(value);
                }
            }
        }

        return count;
    }

    static long encodeIpAddress(String ipAddress) {
        long encoded = 0;
        int octet = 0;

        for (char currentChar : ipAddress.toCharArray()) {
            if (currentChar != '.') {
                int number = currentChar - '0';
                octet = octet * 10 + number;
            } else {
                encoded <<= 8;
                encoded |= octet;
                octet = 0;
            }
        }

        encoded <<= 8;
        encoded |= octet;
        return encoded;
    }

    private byte[] retrieveBucket(int leftBits16) {
        byte[] bucket = holder[leftBits16];
        if (bucket == null) {
            bucket = new byte[MIDDLE_13_BITS_RANGE];
            Arrays.fill(bucket, Byte.MIN_VALUE);
            holder[leftBits16] = bucket;
        }
        return bucket;
    }

    private static int applyBitmask(int rightBits3, int value) {
        int bitmask = (int) Math.pow(2, rightBits3);
        value |= bitmask;
        return value;
    }
}