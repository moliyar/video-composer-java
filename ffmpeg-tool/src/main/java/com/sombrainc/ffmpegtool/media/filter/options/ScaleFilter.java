package com.sombrainc.ffmpegtool.media.filter.options;

import com.sombrainc.ffmpegtool.media.filter.FilterChainInput;
import com.sombrainc.ffmpegtool.input.Input;
import com.sombrainc.ffmpegtool.modeling.StreamType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScaleFilter extends BaseFilter {

    private String value;

    private ScaleFilter(String width, String height) {
        this.value = "scale=".concat(width).concat(":").concat(height);
    }

    public static ScaleFilter of(long width, long height) {
        return new ScaleFilter(String.valueOf(width), String.valueOf(height));
    }

    public static ScaleFilter ofWidth(long width) {
        return of(width, -1);
    }

    public static ScaleFilter ofHeight(long height) {
        return of(-1, height);
    }

    public FilterChainInput toFilterChainInput(Input input, String aliasName) {
        return new FilterChainInput(aliasName, this, new FilterChainInput.InputType(input, StreamType.VIDEO));
    }

}
