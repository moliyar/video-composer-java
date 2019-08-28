package com.sombrainc.commons.concurrent.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProcessExecutors {
    private ProcessExecutors() {}

    public static ProcessExecutor newFixedProcessPoolExecutor(final int countProcess) {
        return new FixedProcessPoolExecutor(countProcess);
    }

    public static ProcessExecutor currentThreadExecutor() {
        return new CurrentThreadExecutor();
    }

    public static ProcessExecutor cashedProcessExecutor() {
        return new CashedProcessPoolExecutor();
    }

    public static class CurrentThreadExecutor implements ProcessExecutor {

        private CurrentThreadExecutor() {}

        @Override
        public void submit(final Runnable process) {
            process.run();
        }
    }

    public static class FixedProcessPoolExecutor implements ProcessExecutor {
        private ExecutorService executorService;

        private FixedProcessPoolExecutor(final int processCount) {
            executorService = Executors.newFixedThreadPool(processCount);
        }

        @Override
        public void submit(final Runnable process) {
            executorService.submit(process);
        }
    }

    public static class CashedProcessPoolExecutor implements ProcessExecutor {
        private ExecutorService executorService;

        public CashedProcessPoolExecutor() {
            executorService = new ThreadPoolExecutor(0, 10,
                    20L, TimeUnit.SECONDS,
                    new SynchronousQueue<>()
            );
        }

        @Override
        public void submit(final Runnable process) {
            executorService.submit(process);
        }
    }


}
