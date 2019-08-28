package com.sombrainc.ffmpegtool.media.codec;

import com.sombrainc.ffmpegtool.media.codec.audio.AudioCodec;
import com.sombrainc.ffmpegtool.media.codec.audio.AudioCodec.AudioCodecTemplate;
import com.sombrainc.ffmpegtool.media.codec.audio.MP3Codec;

public interface AudioCodecs {

    AudioCodecTemplate<MP3Codec> MP3_CODEC              = new AudioCodecTemplate<>("mp3", MP3Codec::new);
    AudioCodecTemplate<AudioCodec> MP4_CODEC            = new AudioCodecTemplate<>("mp4", AudioCodec::new);
    AudioCodecTemplate<AudioCodec> AAC_CODEC            = new AudioCodecTemplate<>("aac", AudioCodec::new);
}
