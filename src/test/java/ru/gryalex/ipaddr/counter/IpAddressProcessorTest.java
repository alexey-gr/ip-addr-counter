package ru.gryalex.ipaddr.counter;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IpAddressProcessorTest {

    @Test
    void testCalculateUniqueCount() throws IOException {
        var ipAddressesURL = getClass().getClassLoader().getResource("ip-addresses.txt");
        assertNotNull(ipAddressesURL);
        var ipAddressesPath = Path.of(ipAddressesURL.getPath());
        long uniqueCount = IpAddressProcessor.calculateUniqueCount(ipAddressesPath);
        assertEquals(4, uniqueCount, "Wrong unique count");
    }
}