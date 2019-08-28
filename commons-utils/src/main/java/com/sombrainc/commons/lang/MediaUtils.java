package com.sombrainc.commons.lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.concurrent.TimeUnit.*;

public final class MediaUtils {

  static final Pattern BITRATE_REGEX = Pattern.compile("(\\d+(?:\\.\\d+)?)kbits/s");
  static final Pattern TIME_REGEX = Pattern.compile("(\\d+):(\\d+):(\\d+(?:\\.\\d+)?)");

  public static long fromTimecode(String time) {
    Matcher m = TIME_REGEX.matcher(time);

    if (!m.find()) {
      throw new IllegalArgumentException("invalid time '" + time + "'");
    }

    long hours = Long.parseLong(m.group(1));
    long mins = Long.parseLong(m.group(2));
    double secs = Double.parseDouble(m.group(3));

    return HOURS.toNanos(hours) + MINUTES.toNanos(mins) + (long) (SECONDS.toNanos(1) * secs);
  }


  public static long parseBitrate(String bitrate) {
    if ("N/A".equals(bitrate)) {
      return -1;
    }

    Matcher m = BITRATE_REGEX.matcher(bitrate);

    if (!m.find()) {
      throw new IllegalArgumentException("Invalid bitrate '" + bitrate + "'");
    }

    return (long) (Float.parseFloat(m.group(1)) * 1000);
  }


}
