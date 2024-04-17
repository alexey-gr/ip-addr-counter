package ru.gryalex.ipaddr.counter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Avg ~4.7 mln ops on AMD Ryzen 5 5600U
 */
public class IpAddressProcessor {

    private static final int BATCH_LOGGING_INTERVAL = 1_000_000;

    private final IpAddressHolder ipAddressHolder;

    public IpAddressProcessor(IpAddressHolder ipAddressHolder) {
        this.ipAddressHolder = ipAddressHolder;
    }

    long calculateUniqueCount(Path path) throws IOException {
        System.out.printf("Counting unique ip addresses in %s%n", path);

        try (var reader = Files.newBufferedReader(path)) {
            String ipAddress;
            int processedCount = 0;
            long batchStartTime = System.currentTimeMillis();

            while ((ipAddress = reader.readLine()) != null) {
                ipAddressHolder.add(ipAddress);

                processedCount++;
                if (processedCount % BATCH_LOGGING_INTERVAL == 0) {
                    long batchProcessingTime = System.currentTimeMillis() - batchStartTime;
                    long rps = (long) (1f * BATCH_LOGGING_INTERVAL / batchProcessingTime * 1000);
                    System.out.printf("- processed: %s, speed: %s rps%n", processedCount, rps);
                    batchStartTime = System.currentTimeMillis();
                }
            }
        }

        long uniqueCount = ipAddressHolder.calculateUniqueCount();
        System.out.println("Unique ip addresses count " + uniqueCount);
        return uniqueCount;
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java IpAddressProcessor <file>");
            System.exit(1);
        }
        var path = Paths.get(args[0]);

        var ipAddressHolder = new OptimizedIpAddressHolder();
        var ipAddressProcessor = new IpAddressProcessor(ipAddressHolder);
        ipAddressProcessor.calculateUniqueCount(path);
    }
}
