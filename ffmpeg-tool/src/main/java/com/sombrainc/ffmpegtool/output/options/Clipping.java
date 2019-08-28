package com.sombrainc.ffmpegtool.output.options;

import com.sombrainc.ffmpegtool.modeling.Time;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Clipping {

    private Time fromTime;
    private Time toTime;

    private Clipping(Time fromTime, Time toTime) {
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public static Clipping of(Time fromTime, Time toTime) {
        return new Clipping(fromTime, toTime);
    }

    public static Clipping betweenSeconds(long from, long to) {
        return of(
                new Time(ChronoUnit.SECONDS, from),
                new Time(ChronoUnit.SECONDS, to)
        );
    }

    public static Clipping betweenMinutes(long from, Long to) {
        return of(
                new Time(ChronoUnit.MINUTES, from),
                new Time(ChronoUnit.MINUTES, to)
        );
    }

    public List<String> getArgs() {
        List<String> args = new ArrayList<>();

        if (fromTime != null) {
            args.add("-ss");
            args.add(fromTime.fromatValue());
        }

        if (toTime != null) {
            args.add("-to");
            args.add(toTime.fromatValue());
        }

        return args;
    }
}
