package com.sombrainc.ffmpegtool;

import com.sombrainc.commons.environment.SystemEnvironment;
import com.sombrainc.ffmpegtool.media.codec.VideoCodecs;
import com.sombrainc.commons.process.ProcessCommandExecutor;
import com.sombrainc.ffmpegtool.media.filter.options.OverlayFilter;
import com.sombrainc.ffmpegtool.media.filter.options.ScaleFilter;
import com.sombrainc.ffmpegtool.output.options.Clipping;
import com.sombrainc.ffmpegtool.input.FileInput;
import com.sombrainc.ffmpegtool.media.filter.FilterChainInput;
import com.sombrainc.ffmpegtool.output.Output;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import static org.testng.Assert.assertNotNull;

public class FFmpegTest {

    private Path ffmpegPath;

    @BeforeClass
    public void setUp() throws InterruptedException, IOException, TimeoutException {
        //System.setProperty("FFMPEG_HOME" , "/usr/bin/ffmpeg");

        String path = SystemEnvironment.gerProperty(FFmpeg.FFMPEG_HOME).orElse(null);
        assertNotNull(path, "System environment <" + FFmpeg.FFMPEG_HOME + "> variable is not set. ");

        ffmpegPath = Paths.get(path);
        FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);
        assertNotNull(ffmpeg, "FFmpeg cannot be instanced");

        String ffmpegResult = ProcessCommandExecutor.of().runCommand(ffmpegPath, "-version");
    }

    @Test
    public void testExample1() throws InterruptedException, TimeoutException, IOException {
        FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);
        ffmpeg.setProgressListener(progress -> System.out.println(progress.toString()));

        FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_VIDEO1, ffmpeg);
        FileInput watermarkInput = FileInput.fromPath(Resources.PATH_TO_FFMPEG_LOGO, ffmpeg);

        FilterChainInput scaleImage = ffmpeg.filterComplex().applyScale(watermarkInput, ScaleFilter.ofWidth(50));
        FilterChainInput overlayVideo = ffmpeg.filterComplex().applyOverlay(videoInput, scaleImage, OverlayFilter.ofTopRight(10, 10));

        Path tempDir = Files.createTempDirectory("ffmpegtool");
        Path outputFile = tempDir.resolve("output.mp4");

        Output output = Output.of(overlayVideo, outputFile)
                .clipping(Clipping.betweenSeconds(5, 10))
                .videoCodec(VideoCodecs.MPEG4_CODEC.build())
                .disableAudio();

        String ffmpegResult = ffmpeg.toOutput(output).execute();
    }

}
