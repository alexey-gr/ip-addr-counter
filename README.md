The program is designed to calculate the number of unique addresses in a given text file.\
It has been optimized for both memory consumption and execution time.\
The input file contains IPv4 addresses in the following format:

```
145.67.23.4
8.34.5.23
89.54.3.124
89.54.3.124
3.45.71.5
...
```

The file can be of unlimited size, potentially occupying tens or hundreds of gigabytes.

To run the program, use:

```shell
./mvnw compile exec:java -Dexec.args="src/test/resources/ip-addresses.txt"
```

See implementation details in the Javadoc in the [OptimizedIpAddressHolder](src/main/java/ru/gryalex/ipaddr/counter/OptimizedIpAddressHolder.java) file.

Areas for improvement:
- Implement input data validation to enhance robustness.
- Consider parallelizing execution to boost throughput.
- Enhance documentation for better understanding and usage.
