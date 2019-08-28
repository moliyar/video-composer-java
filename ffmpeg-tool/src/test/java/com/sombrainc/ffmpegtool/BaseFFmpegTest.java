package com.sombrainc.ffmpegtool;

import com.sombrainc.commons.environment.SystemEnvironment;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.Assert.assertNotNull;

public class BaseFFmpegTest {

    protected Path ffmpegPath;

    @BeforeClass
    public void setUp() {
        //System.setProperty("FFMPEG_HOME" , "/usr/bin/ffmpeg");

        String path = SystemEnvironment.gerProperty(FFmpeg.FFMPEG_HOME).orElse(null);
        assertNotNull(path, "System environment <" + FFmpeg.FFMPEG_HOME + "> variable is not set. ");

        ffmpegPath = Paths.get(path);
        FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);
        assertNotNull(ffmpeg, "FFmpeg cannot be instanced");
    }

    protected Path buildTmpFilePath(String fileName) throws IOException {
        Path tempDir = Files.createTempDirectory("ffmpegtool");
        return tempDir.resolve(fileName);
    }
}
