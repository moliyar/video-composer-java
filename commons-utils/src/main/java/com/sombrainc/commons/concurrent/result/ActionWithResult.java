package com.sombrainc.commons.concurrent.result;

public interface ActionWithResult<H, T> {
    T doAction(H key) throws Exception;
}
