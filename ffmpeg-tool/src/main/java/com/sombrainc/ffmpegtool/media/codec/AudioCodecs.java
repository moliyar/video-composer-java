package com.sombrainc.ffmpegtool.media.codec;

import com.sombrainc.ffmpegtool.media.codec.audio.AudioCodec;
import com.sombrainc.ffmpegtool.media.codec.audio.AudioCodec.AudioCodecTemplate;
import com.sombrainc.ffmpegtool.media.codec.audio.MP4Codec;

public interface AudioCodecs {

    AudioCodecTemplate<AudioCodec> MP2_CODEC            = new AudioCodecTemplate<>("mp2", AudioCodec::new);
    AudioCodecTemplate<AudioCodec> MP3_CODEC            = new AudioCodecTemplate<>("mp3", AudioCodec::new);
    AudioCodecTemplate<MP4Codec> MP4_CODEC              = new AudioCodecTemplate<>("mp4", MP4Codec::new);
    AudioCodecTemplate<AudioCodec> AAC_CODEC            = new AudioCodecTemplate<>("aac", AudioCodec::new);
}
