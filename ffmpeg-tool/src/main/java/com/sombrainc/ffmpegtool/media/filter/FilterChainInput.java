package com.sombrainc.ffmpegtool.media.filter;

import com.sombrainc.ffmpegtool.media.filter.options.BaseFilter;
import com.sombrainc.ffmpegtool.input.Input;
import com.sombrainc.ffmpegtool.modeling.StreamType;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FilterChainInput implements Input {

    protected String aliasName;

    private BaseFilter filter;
    private InputType[] inputs;

    public FilterChainInput(String aliasName, BaseFilter filter, InputType... inputs) {
        this.aliasName = aliasName;
        this.filter = filter;
        this.inputs = inputs;
    }

    @Override
    public String getStreamName(StreamType streamType) {
        return aliasName;
    }

    public String getValue(boolean withAlias) {
        String value = Arrays.stream(inputs).map(InputType::getSteamName).collect(Collectors.joining());
        value = value.concat(filter.getValue());

        if (withAlias) {
            value = value.concat("[").concat(aliasName).concat("]");
        }

        return value;
    }

    @AllArgsConstructor
    public static class InputType {

        private Input input;
        private StreamType streamType;

        public String getSteamName() {
            return "[".concat(input.getStreamName(streamType)).concat("]");
        }
    }

}
