package com.metadata.architecture;

import java.math.BigDecimal;
import java.math.BigInteger;
import hopes.cic.xml.ArchitectureElementCategoryType;
import hopes.cic.xml.ArchitectureElementSlavePortType;
import hopes.cic.xml.ArchitectureElementTypeType;
import hopes.cic.xml.SizeMetricType;

public class UEMArchitectureElementTypeType extends ArchitectureElementTypeType {
    public UEMArchitectureElementTypeType(boolean isProcessor) {
        if (isProcessor) {
            setActivePower(BigInteger.valueOf(80000));
            setSleepPower(BigInteger.valueOf(80000));
            setRelativeCost(BigDecimal.valueOf(1.0));
            setScheduler("RR");
            setOS("LINUX");
            setCategory(ArchitectureElementCategoryType.PROCESSOR);
        } else {
            setCategory(ArchitectureElementCategoryType.MEMORY);
        }
    }

    public void setClock(int clock) {
        super.setClock(BigInteger.valueOf(clock));
    }

    public void setMemory(int size, String metric) {
        if (getCategory().equals(ArchitectureElementCategoryType.MEMORY)) {
            ArchitectureElementSlavePortType port = new ArchitectureElementSlavePortType();
            port.setSize(BigInteger.valueOf(size));
            port.setMetric(SizeMetricType.fromValue(metric));
            port.setName("Slave");
            getSlavePort().add(port);
        }
    }
}
