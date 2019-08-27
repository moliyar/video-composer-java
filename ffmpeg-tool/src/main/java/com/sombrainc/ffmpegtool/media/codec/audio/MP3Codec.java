package com.sombrainc.ffmpegtool.media.codec.audio;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MP3Codec extends AudioCodec<MP3Codec>{

    private String bitRate;
    private String samplingRate;

    public MP3Codec(AudioCodecTemplate<MP3Codec> codecTemplate) {
        super(codecTemplate);
    }

    public MP3Codec bitRate(String bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    public MP3Codec samplingRate(String samplingRate) {
        this.samplingRate = samplingRate;
        return this;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = super.getArgs();

        if (StringUtils.isNoneBlank(bitRate)) {
            args.add("-ab");
            args.add(bitRate);
        }

        if (StringUtils.isNoneBlank(samplingRate)) {
            args.add("-ar");
            args.add(samplingRate);
        }

        return args;
    }
}
