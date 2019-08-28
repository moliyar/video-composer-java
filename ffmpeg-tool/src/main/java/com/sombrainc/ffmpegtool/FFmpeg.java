
package com.sombrainc.ffmpegtool;

import com.sombrainc.ffmpegtool.media.filter.FilterComplex;
import com.sombrainc.ffmpegtool.input.Input;
import com.sombrainc.ffmpegtool.modeling.StreamType;
import com.sombrainc.ffmpegtool.output.Output;
import com.sombrainc.ffmpegtool.process.ProcessRunner;
import com.sombrainc.ffmpegtool.process.progress.listener.ProgressListener;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FFmpeg {

    public static final String FFMPEG_HOME = "FFMPEG_HOME";

    @Getter
    private Path ffmpegPath;

    private List<BaseInput> inputs = new ArrayList<>();
    private FilterComplex filterComplex = new FilterComplex();

    private ProgressListener progressListener;

    private FFmpeg(Path ffmpegPath) {
        this.ffmpegPath = ffmpegPath;
    }

    private int addInput(BaseInput input) {
        this.inputs.add(input);
        return this.inputs.size() - 1;
    }

    public FilterComplex filterComplex() {
        return filterComplex;
    }

    public FFmpeg setProgressListener(ProgressListener listener) {
        this.progressListener = listener;
        return this;
    }

    public static FFmpeg fromPath(Path ffmpegPath) {
        return new FFmpeg(ffmpegPath);
    }

    public ProcessRunner toOutput(Output output) {
        return new ProcessRunner(this, output, progressListener);
    }

    public List<String> getArgs() {
        List<String> args = new ArrayList<>();

        for (BaseInput input : inputs) {
            args.add("-i");
            args.add(input.inputPath);
        }

        String filterComplexValue = filterComplex.getValue();
        if (StringUtils.isNoneBlank(filterComplexValue)) {
            args.add("-filter_complex");
            args.add(filterComplexValue);
        }

        args.add("-y");

        return args;
    }

    @Getter
    public abstract static class BaseInput implements Input {

        private String inputPath;
        private int index;

        @Override
        public String getStreamName(StreamType streamType) {
            return index + ":" + streamType.code();
        }

        protected BaseInput(FFmpeg fFmpeg, String inputPath) {
            this.inputPath = inputPath;
            this.index = fFmpeg.addInput(this);
        }
    }
}
