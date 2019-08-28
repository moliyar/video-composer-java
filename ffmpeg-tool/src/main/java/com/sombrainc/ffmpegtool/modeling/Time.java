package com.sombrainc.ffmpegtool.modeling;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Time {

    private ChronoUnit unit;
    private long timeCount;

    public Time(ChronoUnit unit, long timeCount) {
        this.unit = unit;
        this.timeCount = timeCount;
    }

    public String fromatValue() {
        LocalTime localTime = LocalTime.of(0, 0, 0);
        localTime = localTime.plus(timeCount, unit);

        return localTime.toString();
    }
}
