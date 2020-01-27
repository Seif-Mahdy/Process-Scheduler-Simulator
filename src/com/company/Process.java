package com.company;

public class Process {
   private  String  processName;
   private String processColor;
   private int processArrivalTime;
   private int processBurstTime;
   private int processPriorityNumber;

    public Process(String processName, String processColor, int processArrivalTime, int processBurstTime, int processPriorityNumber) {
        this.processName = processName;
        this.processColor = processColor;
        this.processArrivalTime = processArrivalTime;
        this.processBurstTime = processBurstTime;
        this.processPriorityNumber = processPriorityNumber;
    }
    public void setProcessBurstTime(int burstTime){
        processBurstTime = burstTime;
    }
    public void setProcessPriorityNumber(int priorityNumber){
        this.processPriorityNumber = priorityNumber;
    }
    public String getProcessName() {
        return processName;
    }

    public String getProcessColor() {
        return processColor;
    }

    public int getProcessArrivalTime() {
        return processArrivalTime;
    }

    public int getProcessBurstTime() {
        return processBurstTime;
    }

    public int getProcessPriorityNumber() {
        return processPriorityNumber;
    }
}


