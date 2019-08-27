package com.sombrainc.ffmpegtool.input;

import com.sombrainc.ffmpegtool.modeling.StreamType;

public interface Input {

    String getStreamName(StreamType streamType);
}
