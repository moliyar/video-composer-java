package com.sombrainc.ffmpegtool.media.filter;

import com.sombrainc.ffmpegtool.media.filter.options.OverlayFilter;
import com.sombrainc.ffmpegtool.media.filter.options.ScaleFilter;
import com.sombrainc.ffmpegtool.input.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterComplex {

    private List<FilterChainInput> filters = new ArrayList<>();

    public FilterChainInput applyOverlay(Input mainInput, Input overlayInput, OverlayFilter overlayFilter) {
        FilterChainInput filterChainInput = overlayFilter.toFilterChainInput(mainInput, overlayInput, randomAlias("overlay"));
        filters.add(filterChainInput);

        return filterChainInput;
    }

    public FilterChainInput applyScale(Input input, ScaleFilter scaleFilter) {
        FilterChainInput filterChainInput = scaleFilter.toFilterChainInput(input, randomAlias("scale"));
        filters.add(filterChainInput);

        return filterChainInput;
    }

    public String getValue() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < filters.size(); i++) {
            boolean latest = (filters.size() - 1) == i;
            list.add(filters.get(i).getValue(!latest));
        }

        return list.stream().collect(Collectors.joining(";"));
    }

    private String randomAlias(String aliasPrefix) {
        return aliasPrefix + filters.size();
    }
}
