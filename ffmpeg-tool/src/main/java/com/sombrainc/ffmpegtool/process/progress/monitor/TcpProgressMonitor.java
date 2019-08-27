package com.sombrainc.ffmpegtool.process.progress.monitor;

import com.sombrainc.ffmpegtool.process.progress.listener.ProgressListener;
import com.sombrainc.ffmpegtool.process.progress.logparser.StreamProgressParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import static com.google.common.base.Preconditions.checkNotNull;

public class TcpProgressMonitor extends ProgressMonitor {

    private final ServerSocket server;
    private final URI address;

    public TcpProgressMonitor(ProgressListener listener) throws IOException, URISyntaxException {
        this(listener, 0, InetAddress.getLoopbackAddress());
    }

    public TcpProgressMonitor(ProgressListener listener, int port, InetAddress addr) throws IOException, URISyntaxException {
        super(listener);
        this.server = new ServerSocket(port, 0, addr);
        this.address = createUri("tcp", server.getInetAddress(), server.getLocalPort());
    }

    @Override
    public synchronized void stop() throws IOException {
        if (server.isClosed()) {
            return;
        }

        server.close();
        super.stop();
    }

    @Override
    protected String getThreadName() {
        return "TcpProgressMonitor";
    }

    @Override
    protected Runnable getRunnable(CountDownLatch startSignal) {
        return new TcpProgressParserRunnable(parser, server, startSignal);
    }

    @Override
    public URI getUri() {
        return address;
    }

    public static class TcpProgressParserRunnable implements Runnable {

        final StreamProgressParser parser;
        final ServerSocket server;
        final CountDownLatch startSignal;

        public TcpProgressParserRunnable(StreamProgressParser parser, ServerSocket server, CountDownLatch startSignal) {
            this.parser = checkNotNull(parser);
            this.server = checkNotNull(server);
            this.startSignal = checkNotNull(startSignal);
        }

        @Override
        public void run() {
            while (!server.isClosed() && !Thread.currentThread().isInterrupted()) {
                try {
                    startSignal.countDown();

                    try (Socket socket = server.accept()) {
                        try (InputStream stream = socket.getInputStream()) {
                            parser.processStream(stream);
                        }
                    }

                } catch (Exception e) {
                    // TODO Report to the user that this failed in some way
                }
            }
        }
    }
}
