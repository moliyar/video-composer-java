package com.sombrainc.ffmpegtool;

import com.sombrainc.commons.concurrent.ParallelProcessPool;
import com.sombrainc.commons.concurrent.result.ProcessResult;
import com.sombrainc.ffmpegtool.input.FileInput;
import com.sombrainc.ffmpegtool.media.filter.FilterChainInput;
import com.sombrainc.ffmpegtool.media.filter.options.OverlayFilter;
import com.sombrainc.ffmpegtool.output.Output;
import com.sombrainc.ffmpegtool.output.options.Clipping;
import com.sombrainc.ffmpegtool.process.ProcessRunner;
import com.sombrainc.ffmpegtool.process.result.FFmpegResult;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class FFmpegMultiProcessingTest extends BaseFFmpegTest {

    @Test
    public void testConcurent() throws IOException {
        ParallelProcessPool.ParallelProcessPoolBuilder<ProcessRunner, FFmpegResult> builder = ParallelProcessPool.withResulting(ProcessRunner.class, FFmpegResult.class);

        ProcessRunner videoWithWatermarkScenario = videoWithWatermarkScenario();
        builder.putProcess(videoWithWatermarkScenario, ProcessRunner::execute);

        ProcessRunner clippingVideoScenario = clippingVideoScenario();
        builder.putProcess(clippingVideoScenario, ProcessRunner::execute);

        Map<ProcessRunner, ProcessResult<FFmpegResult>> results = builder.build().submitAndWaitResults();

        ProcessResult<FFmpegResult> videoWithWatermarkResult = results.get(videoWithWatermarkScenario);
        Assert.assertNotNull(videoWithWatermarkResult.getData(), "Video with watermark scenario returned an error");

        ProcessResult<FFmpegResult> clippingVideoResult = results.get(clippingVideoScenario);
        Assert.assertNotNull(clippingVideoResult.getData(), "Clipping video scenario returned an error");
    }

    private ProcessRunner videoWithWatermarkScenario() throws IOException {
        FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);

        FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_MP4_VIDEO, ffmpeg);
        FileInput watermarkInput = FileInput.fromPath(Resources.PATH_TO_FFMPEG_LOGO, ffmpeg);

        FilterChainInput overlayVideo = ffmpeg.filterComplex().applyOverlay(videoInput, watermarkInput, OverlayFilter.ofTopRight(10, 10));
        Output output = Output.of(overlayVideo, buildTmpFilePath("videoWithWatermark.mp4"));

        return ffmpeg.toOutput(output);
    }

    private ProcessRunner clippingVideoScenario() throws IOException {
        FFmpeg ffmpeg = FFmpeg.fromPath(ffmpegPath);

        FileInput videoInput = FileInput.fromPath(Resources.PATH_TO_MP4_VIDEO, ffmpeg);
        Output output = Output.of(videoInput, buildTmpFilePath("clippingVideo.mp4"));
        output.clipping(Clipping.betweenSeconds(5, 10));

        return ffmpeg.toOutput(output);
    }
}
