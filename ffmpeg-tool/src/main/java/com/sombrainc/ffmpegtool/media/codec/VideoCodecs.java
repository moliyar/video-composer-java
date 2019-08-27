package com.sombrainc.ffmpegtool.media.codec;

import com.sombrainc.ffmpegtool.media.codec.video.H264Codec;
import com.sombrainc.ffmpegtool.media.codec.video.MPEG4Codec;
import com.sombrainc.ffmpegtool.media.codec.video.VideoCodec.VideoCodecTemplate;

public interface VideoCodecs {

    VideoCodecTemplate<H264Codec> H_264_CODEC           = new VideoCodecTemplate<>("h264", H264Codec::new);
    VideoCodecTemplate<MPEG4Codec> MPEG4_CODEC          = new VideoCodecTemplate<>("mpeg4", MPEG4Codec::new);
}
