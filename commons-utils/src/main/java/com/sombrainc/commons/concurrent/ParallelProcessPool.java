package com.sombrainc.commons.concurrent;

import com.sombrainc.commons.concurrent.executor.ProcessExecutor;
import com.sombrainc.commons.concurrent.executor.ProcessExecutors;
import com.sombrainc.commons.concurrent.modeling.Process;
import com.sombrainc.commons.concurrent.result.ActionWithResult;
import com.sombrainc.commons.concurrent.result.ProcessResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ParallelProcessPool<I, T> {

    private static Logger LOGGER = LoggerFactory.getLogger(ParallelProcessPool.class);

    private CountDownLatch latch;
    private ProcessExecutor processExecutor;
    private List<Process<I, T>> processes;
    private int countOfProcess = 0;

    private ParallelProcessPool(Map<I, ActionWithResult<I, T>> actionsMap) {
        countOfProcess = actionsMap.size();
        processExecutor = countOfProcess > 1 ? ProcessExecutors.cashedProcessExecutor() : ProcessExecutors.currentThreadExecutor();
//        processExecutor = ProcessExecutors.currentThreadExecutor();

        processes = actionsMap.keySet().stream().map(key -> new Process<>(key, actionsMap.get(key))).collect(Collectors.toList());
    }

    private void submit() {
        latch = new CountDownLatch(countOfProcess);

        processes.stream()
                .peek(p -> p.setLatch(latch))
                .forEach(processExecutor::submit);
    }

    public Map<I, ProcessResult<T>> submitAndWaitResults() {
        submit();

        try {
            latch.await();
        } catch (final InterruptedException e) {
            LOGGER.error("", e);
        }

        return getProcessResults();
    }

    public Map<I, ProcessResult<T>> getProcessResults() {
        final Map<I, ProcessResult<T>> resultMap = new HashMap<>();

        for (final Process<I, T> process : processes) {
            resultMap.put(process.getProcessId(), process.getResult());
        }

        return resultMap;
    }

    public static <I, T> ParallelProcessPoolBuilder<I, T> withResulting(final Class<I> processIdType, final Class<T> resultDataType) {
        return new ParallelProcessPoolBuilder<>();
    }

    public static class ParallelProcessPoolBuilder<I, T> {
        private Map<I, ActionWithResult<I, T>> actionsMap = new HashMap<>();

        private ParallelProcessPoolBuilder() {}

        public ParallelProcessPoolBuilder<I, T> putProcess(final I processId, final ActionWithResult<I, T> action) {
            actionsMap.put(processId, action);
            return this;
        }

        public ParallelProcessPool<I, T> build() {
            return new ParallelProcessPool<>(actionsMap);
        }
    }



}
