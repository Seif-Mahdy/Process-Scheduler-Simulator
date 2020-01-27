package com.company;

import java.util.Comparator;

public class SortProcessAccordingToBurstTime implements Comparator<Process> {
    @Override
    public int compare(Process o1, Process o2) {
        return o1.getProcessBurstTime() - o2.getProcessBurstTime();
    }
}
