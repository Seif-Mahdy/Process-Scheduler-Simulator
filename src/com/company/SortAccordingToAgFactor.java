package com.company;

import java.util.Comparator;

public class SortAccordingToAgFactor implements Comparator<AgProcess> {
    @Override
    public int compare(AgProcess o1, AgProcess o2) {
        return o1.getAgFactor() - o2.getAgFactor();
    }
}
