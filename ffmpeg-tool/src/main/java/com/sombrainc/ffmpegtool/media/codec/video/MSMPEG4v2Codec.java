package com.sombrainc.ffmpegtool.media.codec.video;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MSMPEG4v2Codec extends VideoCodec {

    private String videoSize;

    public MSMPEG4v2Codec(VideoCodecTemplate codecTemplate) {
        super(codecTemplate);
    }

    public MSMPEG4v2Codec size(long width, long height) {
        this.videoSize = String.valueOf(width).concat("x").concat(String.valueOf(height));

        return this;
    }

    @Override
    public List<String> getArgs() {
        List<String> args = new ArrayList<>(super.getArgs());

        if (StringUtils.isNoneBlank(videoSize)) {
            args.add("-s");
            args.add(videoSize);
        }

        return args;
    }
}
