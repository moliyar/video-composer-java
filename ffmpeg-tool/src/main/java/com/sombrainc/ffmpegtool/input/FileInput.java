package com.sombrainc.ffmpegtool.input;

import com.sombrainc.ffmpegtool.FFmpeg;

import java.nio.file.Path;

public class FileInput extends FFmpeg.BaseInput {

    protected FileInput(FFmpeg ffMpeg, String input) {
        super(ffMpeg, input);
    }

    public static FileInput fromPath(Path filePath, FFmpeg ffMpeg) {
        return new FileInput(ffMpeg, filePath.toString());
    }
}
