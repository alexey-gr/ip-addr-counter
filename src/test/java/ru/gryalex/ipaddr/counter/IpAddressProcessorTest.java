package ru.gryalex.ipaddr.counter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class IpAddressProcessorTest {

    private IpAddressProcessor ipAddressProcessor;

    @BeforeEach
    void setUp() {
        var ipAddressHolder = new OptimizedIpAddressHolder();
        ipAddressProcessor = new IpAddressProcessor(ipAddressHolder);
    }

    @Test
    void testNullPath() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ipAddressProcessor.calculateUniqueCount(null)
        );
    }

    @Test
    void testNotExistingPath() {
        assertThrows(
                FileNotFoundException.class,
                () -> ipAddressProcessor.calculateUniqueCount(Path.of("not-existing"))
        );
    }

    @Test
    void testCalculateUniqueCount() throws IOException {
        var ipAddressesURL = getClass().getClassLoader().getResource("ip-addresses.txt");
        assertNotNull(ipAddressesURL);
        var ipAddressesPath = Path.of(ipAddressesURL.getPath());
        long uniqueCount = ipAddressProcessor.calculateUniqueCount(ipAddressesPath);
        assertEquals(4, uniqueCount, "Wrong unique count");
    }
}