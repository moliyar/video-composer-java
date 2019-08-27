package com.sombrainc.ffmpegtool.process;

import com.sombrainc.commons.process.ProcessCommandExecutor;
import com.sombrainc.ffmpegtool.FFmpeg;
import com.sombrainc.ffmpegtool.output.Output;
import com.sombrainc.ffmpegtool.process.progress.listener.ProgressListener;
import com.sombrainc.ffmpegtool.process.progress.monitor.ProgressMonitor;
import com.sombrainc.ffmpegtool.process.progress.monitor.TcpProgressMonitor;

import java.io.IOException;
import java.net.URISyntaxException;
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

    public String execute() throws IOException, TimeoutException, InterruptedException {
        try (ProgressMonitor progressMonitor = createProgressParser(progressListener)) {
            progressMonitor.start();

            List<String> args = fFmpeg.getArgs();

            args.addAll(output.getArgs());

            args.add("-progress");
            args.add(progressMonitor.getUri().toString());

            return ProcessCommandExecutor.of().runCommand(fFmpeg.getFfmpegPath(), args.stream().toArray(String[]::new));
        }
    }

    protected ProgressMonitor createProgressParser(ProgressListener listener) throws IOException {
        try {
            return new TcpProgressMonitor(checkNotNull(listener));
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
}
