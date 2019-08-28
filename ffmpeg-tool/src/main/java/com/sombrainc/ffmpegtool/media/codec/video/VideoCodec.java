package com.sombrainc.ffmpegtool.media.codec.video;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VideoCodec<T extends VideoCodec> {

    public static final VideoCodec COPY_CODEC = new VideoCodec("-codec:v", "copy");
    public static final VideoCodec WITH_OUT_CODEC = new VideoCodec("-vn");

    protected String bitrate;

    private List<String> args;

    public VideoCodec(VideoCodecTemplate<T> codecTemplate) {
       this("-codec:v", codecTemplate.codecName);
    }

    public VideoCodec(String... args) {
        this.args = Arrays.stream(args).collect(Collectors.toList());
    }

    public T bitrate(String bitrate) {
        this.bitrate = bitrate;

        return (T) this;
    }

    public List<String> getArgs() {
        List<String> result = new ArrayList<>(args);

        if (StringUtils.isNoneBlank(bitrate)) {
            result.add("-vb");
            result.add(bitrate);
        }

        return result;
    }

    public static class VideoCodecTemplate<T extends VideoCodec> {

        private String codecName;
        private CodecCreator<T> codecCreator;

        public VideoCodecTemplate(String codecName, CodecCreator<T> codecCreator) {
            this.codecName = codecName;
            this.codecCreator = codecCreator;
        }

        public T build() {
            return codecCreator.get(this);
        }
    }

    public interface CodecCreator<T extends VideoCodec> {

        T get(VideoCodecTemplate<T> webPage);
    }

}
