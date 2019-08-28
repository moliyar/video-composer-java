package com.sombrainc.ffmpegtool.media.filter.options;

import com.sombrainc.ffmpegtool.media.filter.FilterChainInput;
import com.sombrainc.ffmpegtool.media.filter.FilterChainInput.InputType;
import com.sombrainc.ffmpegtool.input.Input;
import com.sombrainc.ffmpegtool.modeling.StreamType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OverlayFilter extends BaseFilter {

    private String value;

    public OverlayFilter(String x, String y) {
        this.value = "overlay=".concat(x).concat(":").concat(y);
    }

    public static OverlayFilter ofCenter() {
        return new OverlayFilter("(W-w)/2", "(H-h)/2");
    }

    public static OverlayFilter ofTopLeft(long x, long y) {
        return new OverlayFilter(String.valueOf(x), String.valueOf(y));
    }

    public static OverlayFilter ofTopRight(long x, long y) {
        return new OverlayFilter("W-w-" + x, String.valueOf(y));
    }

    public static OverlayFilter ofBottomRight(long x, long y) {
        return new OverlayFilter("W-w-" + x, "H-h-" + y);
    }

    public static OverlayFilter ofBottomLeft(long x, long y) {
        return new OverlayFilter(String.valueOf(x), "H-h-" + y);
    }

    public FilterChainInput toFilterChainInput(Input mainInput, Input overlayInput, String aliasName) {
        return new FilterChainInput(aliasName, this, new InputType(mainInput, StreamType.VIDEO), new InputType(overlayInput, StreamType.VIDEO));
    }

}
