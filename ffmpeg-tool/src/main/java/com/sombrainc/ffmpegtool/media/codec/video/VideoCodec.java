package com.sombrainc.ffmpegtool.media.codec.video;

import java.util.ArrayList;
import java.util.List;

public class VideoCodec<T extends VideoCodec> {

    public static final VideoCodec CURRENT_CODEC = new VideoCodec("copy");

    private String codecName;

    public VideoCodec(VideoCodecTemplate<T> codecTemplate) {
        this.codecName = codecTemplate.codecName;
    }

    private VideoCodec(String codecName) {
        this.codecName = codecName;
    }

    public List<String> getArgs() {
        List<String> result = new ArrayList<>();
        result.add("-codec:v");
        result.add(codecName);

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
