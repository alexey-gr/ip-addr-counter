package ru.gryalex.ipaddr.counter;

public interface IpAddressHolder {

    void add(String ipAddress);

    long calculateUniqueCount();
}
