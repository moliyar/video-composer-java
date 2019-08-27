package com.sombrainc.ffmpegtool.process.progress.logparser;

import com.google.common.base.Charsets;
import com.sombrainc.ffmpegtool.process.progress.listener.ProgressListener;
import com.sombrainc.ffmpegtool.process.progress.model.ProgressItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static com.google.common.base.Preconditions.checkNotNull;

public class StreamProgressParser {

    final ProgressListener listener;

    public StreamProgressParser(ProgressListener listener) {
        this.listener = checkNotNull(listener);
    }

    private static BufferedReader wrapInBufferedReader(Reader reader) {
        checkNotNull(reader);

        if (reader instanceof BufferedReader) {
            return (BufferedReader) reader;
        }

        return new BufferedReader(reader);
    }

    public void processStream(InputStream stream) throws IOException {
        checkNotNull(stream);
        processReader(new InputStreamReader(stream, Charsets.UTF_8));
    }

    public void processReader(Reader reader) throws IOException {
        final BufferedReader in = wrapInBufferedReader(reader);

        String line;
        ProgressItem p = new ProgressItem();
        while ((line = in.readLine()) != null) {
            if (p.parseLine(line)) {
                listener.progress(p);
                p = new ProgressItem();
            }
        }
    }
}
