package ru.gryalex.ipaddr.counter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptimizedIpAddressHolderTest {

    private static final List<String> IP_ADDRESSES = List.of("145.67.23.4", "8.34.5.23", "89.54.3.124", "89.54.3.124", "3.45.71.5");

    private final IpAddressHolder ipAddressHolder = new OptimizedIpAddressHolder();

    @Test
    void testAddNullIp() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ipAddressHolder.add(null)
        );
    }

    @Test
    void testAddInvalidIp() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ipAddressHolder.add("invalid-ip")
        );
    }

    @Test
    void testCalculateScenario() {
        IP_ADDRESSES.forEach(ipAddressHolder::add);
        assertEquals(4, ipAddressHolder.calculateUniqueCount(), "Calculate unique count failed");
    }

    @Test
    void testCalculateEmpty() {
        assertEquals(0, ipAddressHolder.calculateUniqueCount(), "Calculate unique count failed");
    }

    @ParameterizedTest
    @MethodSource("provideIpAddressParameters")
    void testCalculateSingleCount(String ipAddress) {
        ipAddressHolder.add(ipAddress);
        ipAddressHolder.add(ipAddress);
        assertEquals(1, ipAddressHolder.calculateUniqueCount(), ipAddress + " should be unique");
    }

    @Test
    void testCalculateContinuousRange() {
        for (int i = 0; i < 255; i++) {
            String ipAddress = "0.0.0." + i;
            ipAddressHolder.add(ipAddress);
            assertEquals(i + 1, ipAddressHolder.calculateUniqueCount(), "Calculate unique count failed");
        }
    }

    @Test
    void testEncodeIpAddress() {
        long encodedIpAddress = OptimizedIpAddressHolder.encodeIpAddress("188.127.79.23");
        String binaryString = Long.toBinaryString(encodedIpAddress);
        assertEquals("10111100011111110100111100010111", binaryString);
        assertEquals(3162459927L, encodedIpAddress);
    }

    @ParameterizedTest
    @MethodSource("provideSetBitInByteWithMaskParameters")
    void testSetBitInByteWithMask(int rightBits3, int expected) {
        int result = OptimizedIpAddressHolder.setBitInByteWithMask(0, rightBits3);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideIpAddressParameters() {
        return IP_ADDRESSES.stream().map(Arguments::of);
    }

    private static Stream<Arguments> provideSetBitInByteWithMaskParameters() {
        return Stream.of(
                Arguments.of(0b000, 0b00000001),
                Arguments.of(0b001, 0b00000010),
                Arguments.of(0b110, 0b01000000),
                Arguments.of(0b111, 0b10000000)
        );
    }
}
