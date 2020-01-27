package com.company;

public class AgProcess extends Process {
    private int AgFactor;
    private int quantum;
    public AgProcess(String processName, String processColor, int processArrivalTime, int processBurstTime, int processPriorityNumber,int quantum) {
        super(processName, processColor, processArrivalTime, processBurstTime, processPriorityNumber);
        this.quantum = quantum;
    }
    public void calculateAgFactor()
    {
        AgFactor=this.getProcessBurstTime()+this.getProcessPriorityNumber()+this.getProcessArrivalTime();
    }

    public int getAgFactor() {
        return AgFactor;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }
}
