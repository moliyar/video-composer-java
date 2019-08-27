package com.sombrainc.commons.concurrent.executor;

public interface ProcessExecutor {
    void submit(Runnable process);
}
