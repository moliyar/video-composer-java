package com.sombrainc.ffmpegtool;

import com.sombrainc.ffmpegtool.exception.FFmpegResultException;
import com.sombrainc.ffmpegtool.input.FileInput;
import com.sombrainc.ffmpegtool.media.codec.AudioCodecs;
import com.sombrainc.ffmpegtool.media.codec.VideoCodecs;
import com.sombrainc.ffmpegtool.media.codec.audio.MP3Codec;
import com.sombrainc.ffmpegtool.media.codec.video.MSMPEG4v2Codec;
import com.sombrainc.ffmpegtool.media.filter.FilterChainInput;
import com.sombrainc.ffmpegtool.media.filter.options.OverlayFilter;
import com.sombrainc.ffmpegtool.media.filter.options.ScaleFilter;
import com.sombrainc.ffmpegtool.output.Output;
import com.sombrainc.ffmpegtool.output.options.Clipping;
import com.sombrainc.ffmpegtool.process.result.FFmpegResult;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

public class FFmpegTest extends BaseFFmpegTest {

    @Test
    public void testClipVideo() throws InterruptedException, TimeoutException, IOException, FFmpegResultException {
        FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);
        FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_MP4_VIDEO, ffmpeg);

        Output output = Output.of(videoInput, buildTmpFilePath("videoClipping.mp4"))
                .clipping(Clipping.betweenSeconds(5, 10));

        FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
        Assert.assertNotNull(ffmpegResult);

        Assert.assertEquals((long) ffmpegResult.getVideoSize(), 230000L, "Video size mismatch");
        Assert.assertEquals((long) ffmpegResult.getAudioSize(), 78000L, "Audio size mismatch");
    }

    @Test
    public void testVideoWithWatermark() throws InterruptedException, TimeoutException, IOException, FFmpegResultException {
        FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);

        FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_MP4_VIDEO, ffmpeg);
        FileInput watermarkInput = FileInput.fromPath(Resources.PATH_TO_FFMPEG_LOGO, ffmpeg);

        FilterChainInput overlayVideo = ffmpeg.filterComplex().applyOverlay(videoInput, watermarkInput, OverlayFilter.ofTopRight(10, 10));
        Output output = Output.of(overlayVideo, buildTmpFilePath("videoWithWatermark.mp4"));

        FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
        Assert.assertNotNull(ffmpegResult);

        Assert.assertEquals((long) ffmpegResult.getVideoSize(), 1344000L, "Video size mismatch");
        Assert.assertEquals((long) ffmpegResult.getAudioSize(), 519000L, "Audio size mismatch");
    }

    @Test
    public void testExtractExtractSoundFromVideo() throws InterruptedException, TimeoutException, IOException, FFmpegResultException {
        FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);
        FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_AVI_VIDEO, ffmpeg);

        MP3Codec mp3Codec = AudioCodecs.MP3_CODEC.build()
                .samplingRate("44100")
                .bitRate("192k");

        Output output = Output.of(videoInput, buildTmpFilePath("extractAudioStream.mp3"))
                .disableVideo()
                .audioCodec(mp3Codec);

        FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
        Assert.assertNotNull(ffmpegResult);

        Assert.assertEquals((long) ffmpegResult.getVideoSize(), 0L, "Video stream exist");
        Assert.assertEquals((long) ffmpegResult.getAudioSize(), 1220000L, "Audio size mismatch");
    }

    @Test
    public void testCompressAviVideo() throws InterruptedException, TimeoutException, IOException, FFmpegResultException {
        FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);

        MSMPEG4v2Codec videoCodec = VideoCodecs.MSMPEG4v2_CODEC.build()
                .size(320, 240);

        FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_AVI_VIDEO, ffmpeg);
        Output output = Output.of(videoInput, buildTmpFilePath("compressVideo.avi"))
                .videoCodec(videoCodec);

        FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
        Assert.assertNotNull(ffmpegResult);

        Assert.assertEquals((long) ffmpegResult.getVideoSize(), 1289000L, "Video size mismatch");
        Assert.assertEquals((long) ffmpegResult.getAudioSize(), 813000L, "Audio size mismatch");
    }

    @Test
    public void testScaleWatermarkForOverlayAndClippingResult() throws InterruptedException, TimeoutException, IOException, FFmpegResultException {
        FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);

        FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_MP4_VIDEO, ffmpeg);
        FileInput watermarkInput = FileInput.fromPath(Resources.PATH_TO_FFMPEG_LOGO, ffmpeg);

        FilterChainInput scaleImage = ffmpeg.filterComplex().applyScale(watermarkInput, ScaleFilter.ofWidth(50));
        FilterChainInput overlayVideo = ffmpeg.filterComplex().applyOverlay(videoInput, scaleImage, OverlayFilter.ofTopRight(10, 10));

        Output output = Output.of(overlayVideo, buildTmpFilePath("scaleWatermarkForOverlayAndClipping.mp4"))
                .clipping(Clipping.betweenSeconds(5, 10))
                .videoCodec(VideoCodecs.MPEG4_CODEC.build());

        FFmpegResult ffmpegResult = ffmpeg.toOutput(output).execute();
        Assert.assertNotNull(ffmpegResult);

        Assert.assertEquals((long) ffmpegResult.getVideoSize(), 291000L, "Video size mismatch");
        Assert.assertEquals((long) ffmpegResult.getAudioSize(), 78000L, "Audio size mismatch");
    }


}
