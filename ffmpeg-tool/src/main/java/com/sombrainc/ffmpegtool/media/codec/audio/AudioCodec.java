package com.sombrainc.ffmpegtool.media.codec.audio;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AudioCodec<T extends AudioCodec> {

    public static final AudioCodec COPY_CODEC = new AudioCodec("-codec:v", "copy");
    public static final AudioCodec WITH_OUT_CODEC = new AudioCodec("-an");

    protected String bitRate;

    private List<String> args;

    public AudioCodec(AudioCodecTemplate<T> codecTemplate) {
        this("-codec:a", codecTemplate.codecName);
    }

    public AudioCodec(String... args) {
        this.args = Arrays.stream(args).collect(Collectors.toList());
    }

    public T bitRate(String bitRate) {
        this.bitRate = bitRate;
        return (T) this;
    }

    public List<String> getArgs() {
        List<String> args = new ArrayList<>(this.args);

        if (StringUtils.isNoneBlank(bitRate)) {
            args.add("-ab");
            args.add(bitRate);
        }

        return args;
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
