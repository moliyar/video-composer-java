package com.sombrainc.ffmpegtool.process.progress.monitor;

import com.google.common.net.InetAddresses;
import com.sombrainc.ffmpegtool.process.progress.listener.ProgressListener;
import com.sombrainc.ffmpegtool.process.progress.logparser.StreamProgressParser;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ProgressMonitor implements Closeable {

    protected final StreamProgressParser parser;
    private Thread thread;

    public ProgressMonitor(ProgressListener listener) {
        this.parser = new StreamProgressParser(listener);
    }

    protected abstract String getThreadName();

    protected abstract Runnable getRunnable(CountDownLatch startSignal);

    public abstract URI getUri();

    public synchronized void start() {
        if (thread != null) {
            throw new IllegalThreadStateException("Parser already started");
        }

        String name = getThreadName() + "(" + getUri().toString() + ")";

        CountDownLatch startSignal = new CountDownLatch(1);
        Runnable runnable = getRunnable(startSignal);

        thread = new Thread(runnable, name);
        thread.start();

        try {
            startSignal.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void stop() throws IOException {
        if (thread != null) {
            thread.interrupt();

            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void close() throws IOException {
        stop();
    }

    static URI createUri(String scheme, InetAddress address, int port) throws URISyntaxException {
        checkNotNull(address);
        return new URI(scheme, null, InetAddresses.toUriString(address), port, null, null, null);
    }
}
