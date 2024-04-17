package ru.gryalex.ipaddr.counter;

import org.junit.jupiter.api.RepeatedTest;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparisonLongRunningTest {

    private static final int TOTAL_IP_ADDRESSES_COUNT = 10_000_000;
    private static final int MAX_DUPLICATE_COUNT = 10;

    private static final int OCTET_COUNT = 4;
    private static final String OCTET_DELIMITER = ".";

    private static final Random RANDOM = new Random();

    @RepeatedTest(10)
    void compareIpAddressHolders() {
        List<String> ipAddresses = generateRandomIpAddresses();

        var simpleIpAddressHolder = new SimpleIpAddressHolder();
        var optimizedIpAddressHolder = new OptimizedIpAddressHolder();

        ipAddresses.forEach(ip -> {
            simpleIpAddressHolder.add(ip);
            optimizedIpAddressHolder.add(ip);
        });

        assertEquals(simpleIpAddressHolder.calculateUniqueCount(), optimizedIpAddressHolder.calculateUniqueCount(),
                "Both IpAddressHolders should produce the same unique IPs count");
    }

    private List<String> generateRandomIpAddresses() {
        List<String> ipAddresses = IntStream.range(0, TOTAL_IP_ADDRESSES_COUNT)
                .mapToObj(i -> generateRandomIpAddress())
                .collect(toList());

        for (int i = 0; i < MAX_DUPLICATE_COUNT; i++) {
            String repeatedIp = ipAddresses.get(RANDOM.nextInt(ipAddresses.size()));
            for (int j = 0; j < MAX_DUPLICATE_COUNT; j++) {
                ipAddresses.add(repeatedIp);
            }
        }

        Collections.shuffle(ipAddresses);
        return ipAddresses;
    }

    private String generateRandomIpAddress() {
        return IntStream.range(0, OCTET_COUNT)
                .boxed()
                .map(ignore -> generateRandomOctet())
                .collect(joining(OCTET_DELIMITER));
    }

    private String generateRandomOctet() {
        return String.valueOf(RANDOM.nextInt(256));
    }
}
