package com.sombrainc.commons.concurrent.result;

import lombok.Getter;

import java.util.Optional;

@Getter
public class ProcessResult<T> {

    private Status status;
    private T data;
    private Exception exception;

    public static <T> ProcessResult<T> of(T data) {
        return new ProcessResult(Status.finished, data, null);
    }


    public static <T> ProcessResult<T> ofError(final Exception exception) {
        return new ProcessResult(Status.error, null, exception);
    }

    public static <T> ProcessResult<T> empty() {
        return new ProcessResult(Status.not_started, null, null);
    }

    public ProcessResult(final Status status, final T data, final Exception e) {
        this.status = status;
        this.data = data;
        this.exception = e;
    }

    private ProcessResult(final Status status) {
        this.status = status;
    }

    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }

    public enum Status {
        not_started, finished, error,
    }
}
