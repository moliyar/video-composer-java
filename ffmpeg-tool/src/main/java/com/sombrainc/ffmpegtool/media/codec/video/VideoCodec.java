package com.sombrainc.ffmpegtool.media.codec.video;

import java.util.Arrays;
import java.util.List;

public class VideoCodec<T extends VideoCodec> {

    public static final VideoCodec COPY_CODEC = new VideoCodec("-codec:v", "copy");
    public static final VideoCodec WITH_OUT_CODEC = new VideoCodec("-vn");

    private String[] args;

    public VideoCodec(VideoCodecTemplate<T> codecTemplate) {
       this("-codec:v", codecTemplate.codecName);
    }

    public VideoCodec(String... args) {
        this.args = args;
    }

    public List<String> getArgs() {
        return Arrays.asList(args);
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
