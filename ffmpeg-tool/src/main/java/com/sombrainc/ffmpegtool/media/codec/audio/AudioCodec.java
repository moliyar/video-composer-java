package com.sombrainc.ffmpegtool.media.codec.audio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AudioCodec<T extends AudioCodec> {

    public static final AudioCodec COPY_CODEC = new AudioCodec("-codec:v", "copy");
    public static final AudioCodec WITH_OUT_CODEC = new AudioCodec("-an");

    private String[] args;

    public AudioCodec(AudioCodecTemplate<T> codecTemplate) {
        this("-codec:a", codecTemplate.codecName);
    }

    public AudioCodec(String... args) {
        this.args = args;
    }

    public List<String> getArgs() {
        return Arrays.stream(args).collect(Collectors.toList());
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
