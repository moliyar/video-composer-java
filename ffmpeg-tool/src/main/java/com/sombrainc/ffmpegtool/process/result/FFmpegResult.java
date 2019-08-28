package com.sombrainc.ffmpegtool.process.result;

import com.sombrainc.ffmpegtool.exception.FFmpegResultException;
import com.sombrainc.ffmpegtool.modeling.SizeUnit;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Getter
public class FFmpegResult {

    private final Long videoSize;
    private final Long audioSize;
    private final Long subtitleSize;
    private final Long otherStreamsSize;
    private final Long globalHeadersSize;
    private final Double muxingOverheadRatio;

    private FFmpegResult(Long videoSize, Long audioSize, Long subtitleSize, Long otherStreamsSize, Long globalHeadersSize, Double muxingOverheadRatio) {
        this.videoSize = videoSize;
        this.audioSize = audioSize;
        this.subtitleSize = subtitleSize;
        this.otherStreamsSize = otherStreamsSize;
        this.globalHeadersSize = globalHeadersSize;
        this.muxingOverheadRatio = muxingOverheadRatio;
    }

    public static FFmpegResult parsResult(String value) throws FFmpegResultException {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            value = value
                    .replaceAll("other streams", "other_streams")
                    .replaceAll("global headers", "global_headers")
                    .replaceAll("muxing overhead", "muxing_overhead")
                    .replaceAll(":\\s+", ":");

            Map<String, String> map = parseKeyValues(value, ":");

            Long videoSize = parseSizeInBytes(map.get("video"));
            Long audioSize = parseSizeInBytes(map.get("audio"));
            Long subtitleSize = parseSizeInBytes(map.get("subtitle"));
            Long otherStreamsSize = parseSizeInBytes(map.get("other_streams"));
            Long globalHeadersSize = parseSizeInBytes(map.get("global_headers"));
            Double muxOverhead = parseRatio(map.get("muxing_overhead"));

            if (hasNonNull(videoSize, audioSize, subtitleSize, otherStreamsSize, globalHeadersSize, muxOverhead)) {
                return new FFmpegResult(videoSize, audioSize, subtitleSize, otherStreamsSize, globalHeadersSize, muxOverhead);
            }
        } catch (Exception e) {
            throw new FFmpegResultException(value, e);
        }

        throw new FFmpegResultException(value, null);
    }

    private static Map<String, String> parseKeyValues(String value, String separator) {
        Map<String, String> result = new HashMap<>();

        for (String pair : value.split("\\s+")) {
            String[] nameAndValue = pair.split(separator);

            if (nameAndValue.length != 2) {
                continue;
            }

            result.put(nameAndValue[0], nameAndValue[1]);
        }

        return result;
    }

    private static <T> Optional<T> parseNumber(String value, Function<String, T> mapper) {
        try {
            if (StringUtils.isNoneBlank(value)) {
                return Optional.ofNullable(mapper.apply(value));
            }
        } catch (Exception e) {
        }

        return Optional.empty();
    }

    private static Long parseSizeInBytes(String value) {
        return parseSize(value, SizeUnit.B);
    }

    private static Long parseSize(String value, SizeUnit unit) {
        String[] sizeAndUnit = splitValueAndUnit(value);

        Long parsedValue = parseNumber(sizeAndUnit[0], Long::valueOf).orElse(null);

        if (parsedValue == null) {
            return null;
        }

        SizeUnit valueUnit = SizeUnit.of(sizeAndUnit[1]).orElse(null);
        if (valueUnit == null) {
            return null;
        }

        return valueUnit.convertTo(parsedValue, unit);
    }

    private static Double parseRatio(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        double multiplier = 1;
        if (value.endsWith("%")) {
            value = value.substring(0, value.length() - 1);
            multiplier = 1. / 100;
        }

        Double valueDouble = parseNumber(value, Double::valueOf).orElse(null);
        if (valueDouble == null) {
            return null;
        }

        return multiplier * valueDouble;
    }


    private static String[] splitValueAndUnit(String string) {
        if (string == null) {
            return new String[]{"", ""};
        }

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if ((c < '0' || c > '9') && c != '.') {
                return new String[]{string.substring(0, i), string.substring(i)};
            }
        }
        return new String[]{string, ""};
    }


    private static boolean hasNonNull(Object... items) {
        for (Object item : items) {
            if (item != null) {
                return true;
            }
        }

        return false;
    }
}
