package com.sombrainc.ffmpegtool.process;

import com.sombrainc.commons.process.ProcessCommandExecutor;
import com.sombrainc.ffmpegtool.FFmpeg;
import com.sombrainc.ffmpegtool.exception.FFmpegResultException;
import com.sombrainc.ffmpegtool.output.Output;
import com.sombrainc.ffmpegtool.process.progress.listener.ProgressListener;
import com.sombrainc.ffmpegtool.process.progress.monitor.ProgressMonitor;
import com.sombrainc.ffmpegtool.process.progress.monitor.TcpProgressMonitor;
import com.sombrainc.ffmpegtool.process.result.FFmpegResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProcessRunner {

    private FFmpeg fFmpeg;

    private Output output;
    private ProgressListener progressListener;

    public ProcessRunner(FFmpeg fFmpeg, Output output, ProgressListener progressListener) {
        this.fFmpeg = fFmpeg;
        this.output = output;
        this.progressListener = progressListener;
    }

    public FFmpegResult execute() throws IOException, TimeoutException, InterruptedException, FFmpegResultException {
        List<String> args = new ArrayList<>(fFmpeg.getArgs());
        args.addAll(output.getArgs());

        if (progressListener == null) {
            return execute(fFmpeg.getFfmpegPath(), args);
        }

        return executeWithProgress(fFmpeg.getFfmpegPath(), args, progressListener);
    }

    public static FFmpegResult execute(Path ffmpegPath, List<String> args) throws IOException, TimeoutException, InterruptedException, FFmpegResultException {
        String commandResult = ProcessCommandExecutor.of().runCommand(ffmpegPath, args.stream().toArray(String[]::new));
        return FFmpegResult.parsResult(commandResult);
    }

    private static FFmpegResult executeWithProgress(Path ffmpegPath, List<String> args, ProgressListener progressListener) throws IOException, FFmpegResultException, TimeoutException, InterruptedException {
        try (ProgressMonitor progressMonitor = createProgressParser(progressListener)) {
            progressMonitor.start();

            args.add("-progress");
            args.add(progressMonitor.getUri().toString());

            String commandResult = ProcessCommandExecutor.of().runCommand(ffmpegPath, args.stream().toArray(String[]::new));
            return FFmpegResult.parsResult(commandResult);
        }
    }

    protected static ProgressMonitor createProgressParser(ProgressListener listener) throws IOException {
        try {
            return new TcpProgressMonitor(checkNotNull(listener));
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
}
