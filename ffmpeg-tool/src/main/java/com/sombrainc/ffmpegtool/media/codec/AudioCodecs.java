package com.sombrainc.ffmpegtool.media.codec;

import com.sombrainc.ffmpegtool.media.codec.audio.AudioCodec;
import com.sombrainc.ffmpegtool.media.codec.audio.AudioCodec.AudioCodecTemplate;
import com.sombrainc.ffmpegtool.media.codec.audio.MP3Codec;
import com.sombrainc.ffmpegtool.media.codec.audio.MP4Codec;

public interface AudioCodecs {

    AudioCodecTemplate<MP3Codec> MP3_CODEC              = new AudioCodecTemplate<>("mp3", MP3Codec::new);
    AudioCodecTemplate<MP4Codec> MP4_CODEC              = new AudioCodecTemplate<>("mp4", MP4Codec::new);
    AudioCodecTemplate<AudioCodec> AAC_CODEC            = new AudioCodecTemplate<>("aac", AudioCodec::new);
}
