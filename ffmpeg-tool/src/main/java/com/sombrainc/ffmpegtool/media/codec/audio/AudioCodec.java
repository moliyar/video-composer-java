package com.sombrainc.ffmpegtool.media.codec.audio;

import java.util.ArrayList;
import java.util.List;

public class AudioCodec<T extends AudioCodec> {

    public static final AudioCodec CURRENT_CODEC = new AudioCodec("copy");

    private String codecName;

    public AudioCodec(AudioCodecTemplate<T> codecTemplate) {
        this.codecName = codecTemplate.codecName;
    }

    private AudioCodec(String codecName) {
        this.codecName = codecName;
    }

    public List<String> getArgs() {
        List<String> result = new ArrayList<>();
        result.add("-codec:a");
        result.add(codecName);

        return result;
    }

    public static class AudioCodecTemplate<T extends AudioCodec> {

        private String codecName;
        private CodecCreator<T> codecCreator;

        public AudioCodecTemplate(String codecName, CodecCreator<T> codecCreator) {
            this.codecName = codecName;
            this.codecCreator = codecCreator;
        }

        public T build() {
            return codecCreator.get(this);
        }
    }

    public interface CodecCreator<T extends AudioCodec> {

        T get(AudioCodecTemplate<T> webPage);
    }

}
