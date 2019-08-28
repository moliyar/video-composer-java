package com.sombrainc.ffmpegtool.output;

import com.sombrainc.ffmpegtool.media.codec.audio.AudioCodec;
import com.sombrainc.ffmpegtool.media.codec.video.VideoCodec;
import com.sombrainc.ffmpegtool.output.options.Clipping;
import com.sombrainc.ffmpegtool.input.Input;
import lombok.Getter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Output {

    private List<String> args = new ArrayList<>();

    private String outputPath;
    private Input input;

    private Clipping clipping;

    private VideoCodec videoCodec;
    private AudioCodec audioCodec;

    private Output(Input input, String outputPath) {
        this.input = input;
        this.outputPath = outputPath;
    }

    public Output audioCodec(AudioCodec audioCodec) {
        this.audioCodec = audioCodec;
        return this;
    }

    public Output disableAudio() {
        return audioCodec(AudioCodec.WITH_OUT_CODEC);
    }

    public Output videoCodec(VideoCodec videoCodec) {
        this.videoCodec = videoCodec;
        return this;
    }

    public Output disableVideo() {
        return videoCodec(VideoCodec.WITH_OUT_CODEC);
    }

    public Output clipping(Clipping clipping) {
        this.clipping = clipping;

        return this;
    }

    public static Output of(Input input, Path output) {
        return new Output(input, output.toAbsolutePath().toString());
    }

    public List<String> getArgs() {
        List<String> result = new ArrayList<>(args);

        if (clipping != null) {
            result.addAll(clipping.getArgs());
        }

        if (videoCodec != null) {
            result.addAll(videoCodec.getArgs());
        }

        if (audioCodec != null) {
            result.addAll(audioCodec.getArgs());
        }

        result.add(outputPath);

        return result;
    }
}
