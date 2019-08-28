package com.sombrainc.ffmpegtool.process.progress.model;

import com.google.common.base.MoreObjects;
import com.sombrainc.commons.lang.MediaUtils;
import org.apache.commons.lang3.math.Fraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProgressItem {

    static final Logger LOGGER = LoggerFactory.getLogger(ProgressItem.class);

    public enum Status {
        CONTINUE("continue"),
        END("end");

        private final String status;

        Status(String status) {
            this.status = status;
        }

        public static Status of(String status) {
            for (Status s : Status.values()) {
                if (status.equalsIgnoreCase(s.status)) {
                    return s;
                }
            }

            throw new IllegalArgumentException("invalid progress status '" + status + "'");
        }


        @Override
        public String toString() {
            return status;
        }
    }


    public long frame = 0;
    public Fraction fps = Fraction.ZERO;

    public long bitrate = 0;
    public long totalSize = 0;
    public long outTimeNS = 0;

    public long dupFrames = 0;
    public long dropFrames = 0;
    public float speed = 0;
    public Status status = null;

    public ProgressItem() {
        // Nothing
    }

    public ProgressItem(long frame, float fps, long bitrate, long totalSize, long outTimeNS, long dupFrames, long dropFrames, float speed, Status status) {
        this.frame = frame;
        this.fps = Fraction.getFraction(fps);
        this.bitrate = bitrate;
        this.totalSize = totalSize;
        this.outTimeNS = outTimeNS;
        this.dupFrames = dupFrames;
        this.dropFrames = dropFrames;
        this.speed = speed;
        this.status = status;
    }

    public boolean parseLine(String line) {
        line = checkNotNull(line).trim();
        if (line.isEmpty()) {
            return false; // Skip empty lines
        }

        final String[] args = line.split("=", 2);
        if (args.length != 2) {
            // invalid argument, so skip
            return false;
        }

        final String key = checkNotNull(args[0]);
        final String value = checkNotNull(args[1]);

        switch (key) {
            case "frame":
                frame = Long.parseLong(value);
                return false;

            case "fps":
                fps = Fraction.getFraction(value);
                return false;

            case "bitrate":
                if (value.equals("N/A")) {
                    bitrate = -1;
                } else {
                    bitrate = MediaUtils.parseBitrate(value);
                }
                return false;

            case "total_size":
                if (value.equals("N/A")) {
                    totalSize = -1;
                } else {
                    totalSize = Long.parseLong(value);
                }
                return false;

            case "out_time_ms":
                return false;

            case "out_time":
                outTimeNS = MediaUtils.fromTimecode(value);
                return false;

            case "dup_frames":
                dupFrames = Long.parseLong(value);
                return false;

            case "drop_frames":
                dropFrames = Long.parseLong(value);
                return false;

            case "speed":
                if (value.equals("N/A")) {
                    speed = -1;
                } else {
                    speed = Float.parseFloat(value.replace("x", ""));
                }
                return false;

            case "progress":
                status = Status.of(value);
                return true;

            default:
                if (key.startsWith("stream_")) {
                } else {
                    LOGGER.warn("skipping unhandled key: {} = {}", key, value);
                }

                return false;
        }
    }

    public boolean isEnd() {
        return status == Status.END;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgressItem progressItem1 = (ProgressItem) o;
        return frame == progressItem1.frame
                && bitrate == progressItem1.bitrate
                && totalSize == progressItem1.totalSize
                && outTimeNS == progressItem1.outTimeNS
                && dupFrames == progressItem1.dupFrames
                && dropFrames == progressItem1.dropFrames
                && Float.compare(progressItem1.speed, speed) == 0
                && Objects.equals(fps, progressItem1.fps)
                && Objects.equals(status, progressItem1.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frame, fps, bitrate, totalSize, outTimeNS, dupFrames, dropFrames, speed, status);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("frame", frame)
                .add("fps", fps)
                .add("bitrate", bitrate)
                .add("total_size", totalSize)
                .add("out_time_ns", outTimeNS)
                .add("dup_frames", dupFrames)
                .add("drop_frames", dropFrames)
                .add("speed", speed)
                .add("status", status)
                .toString();
    }
}
