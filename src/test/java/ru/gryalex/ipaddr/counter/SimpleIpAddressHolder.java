package ru.gryalex.ipaddr.counter;

import java.util.HashSet;
import java.util.Set;

class SimpleIpAddressHolder implements IpAddressHolder {

    private final Set<String> holder = new HashSet<>();

    @Override
    public void add(String ipAddress) {
        holder.add(ipAddress);
    }

    @Override
    public long calculateUniqueCount() {
        return holder.size();
    }
}
