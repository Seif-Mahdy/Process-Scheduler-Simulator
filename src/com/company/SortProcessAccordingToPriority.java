package com.company;

import java.util.Comparator;

public class SortProcessAccordingToPriority implements Comparator<Process> {
    @Override
    public int compare(Process o1, Process o2) {
        return o1.getProcessPriorityNumber() - o2.getProcessPriorityNumber();
    }
}
