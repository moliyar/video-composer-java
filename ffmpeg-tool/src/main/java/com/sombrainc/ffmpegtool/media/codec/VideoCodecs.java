package com.sombrainc.ffmpegtool.media.codec;

import com.sombrainc.ffmpegtool.media.codec.video.MSMPEG4v2Codec;
import com.sombrainc.ffmpegtool.media.codec.video.VideoCodec;
import com.sombrainc.ffmpegtool.media.codec.video.VideoCodec.VideoCodecTemplate;

public interface VideoCodecs {

    VideoCodecTemplate<VideoCodec> H_264_CODEC              = new VideoCodecTemplate<>("h264", VideoCodec::new);
    VideoCodecTemplate<VideoCodec> MPEG4_CODEC              = new VideoCodecTemplate<>("mpeg4", VideoCodec::new);
    VideoCodecTemplate<MSMPEG4v2Codec> MSMPEG4v2_CODEC      = new VideoCodecTemplate<>("msmpeg4v2", MSMPEG4v2Codec::new);
}
