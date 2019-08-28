package com.sombrainc.commons.concurrent.modeling;

import com.sombrainc.commons.concurrent.result.ActionWithResult;
import com.sombrainc.commons.concurrent.result.ProcessResult;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class Process<I, R> implements Runnable {
    @Getter
    private I processId;
    @Getter
    private ProcessResult<R> result = ProcessResult.empty();
    private ActionWithResult<I, R> action;
    @Setter
    private CountDownLatch latch;

    public Process(final I processId, final ActionWithResult<I, R> action) {
        this.processId = processId;
        this.action = action;
    }

    @Override
    public void run() {
        try {
            final R resultData = action.doAction(processId);

            result = ProcessResult.of(resultData);
        } catch (final Exception e) {
            result = ProcessResult.ofError(e);
        } finally {
            if (Objects.nonNull(latch)) {
                latch.countDown();
            }
        }
    }
}
