package com.company;
/**
 * timer was suggested by wael not seif mostafa
 */

import java.util.ArrayList;

public class Scheduler {
    private ArrayList<Process> processesExecutionOrder;
    private ArrayList<Integer> waitingTimeForEachProcess;
    private ArrayList<Integer> turnaroundTimeForEachProcess;
    private double averageWaitingTime;
    private double averageTurnaroundTime;
    private ArrayList<Process> processes;
    private SortProcessAccordingToArrivalTime sortProcessAccordingToArrivalTime;
    private SortProcessAccordingToBurstTime sortProcessAccordingToBurstTime;
    private SortProcessAccordingToPriority sortProcessAccordingToPriority;
    private SortAccordingToAgFactor sortAccordingToAgFactor;
    private ArrayList<String> quantumOverTime;

    public ArrayList<String> getQuantumOverTime() {
        return quantumOverTime;
    }

    public Scheduler(ArrayList<Process> processes) {
        this.processes = processes;
        processesExecutionOrder = new ArrayList<>();
        waitingTimeForEachProcess = new ArrayList<>();
        turnaroundTimeForEachProcess = new ArrayList<>();
        quantumOverTime = new ArrayList<>();
        averageWaitingTime = 0;
        averageTurnaroundTime = 0;
        sortProcessAccordingToArrivalTime = new SortProcessAccordingToArrivalTime();
        sortProcessAccordingToBurstTime = new SortProcessAccordingToBurstTime();
        sortProcessAccordingToPriority = new SortProcessAccordingToPriority();
        sortAccordingToAgFactor = new SortAccordingToAgFactor();
        this.processes.sort(sortProcessAccordingToArrivalTime);
    }

    public Scheduler() {

    }

    void calculateTurnAroundAndWaiting(int [] timeOfFinishedProcesses) {

        for (int i = 0; i < processes.size(); i++) {
            double turnaround = 0;
            turnaround += timeOfFinishedProcesses[i] - processes.get(i).getProcessArrivalTime();
            turnaroundTimeForEachProcess.add((int)turnaround);
            waitingTimeForEachProcess.add((int)turnaround - processes.get(i).getProcessBurstTime());
        }

    }

    public void setProcesses(ArrayList<Process> processes) {
        this.processes = processes;
        processesExecutionOrder = new ArrayList<>();
        waitingTimeForEachProcess = new ArrayList<>();
        turnaroundTimeForEachProcess = new ArrayList<>();
        quantumOverTime = new ArrayList<>();
        averageWaitingTime = 0;
        averageTurnaroundTime = 0;
        sortProcessAccordingToArrivalTime = new SortProcessAccordingToArrivalTime();
        sortProcessAccordingToBurstTime = new SortProcessAccordingToBurstTime();
        sortProcessAccordingToPriority = new SortProcessAccordingToPriority();
        sortAccordingToAgFactor = new SortAccordingToAgFactor();
        this.processes.sort(sortProcessAccordingToArrivalTime);
    }

    public void shortestRemainingTimeFirst(int contextSwitch) {
        int [] temporaryWaitingTimeForEachProcess = new int[processes.size()];
        int [] temporaryTurnaroundTimeForEachProcess = new int[processes.size()];
        clearHolders();
        ArrayList<Process>sortedProcessAccordingToBurtTime = new ArrayList<>();
        ArrayList<Process>copiedProcesses = deepCopyProcessesArrayList();
        int timer=0;
        int index=0;
        int indexInCopiedProcesses=0;
        int numberOfFinishedProcess = 0;
        int nextValueToIncreaseTimer=0;
        while (true){
            sortedProcessAccordingToBurtTime.clear();
            for(int i=0 ; i<copiedProcesses.size() && copiedProcesses.get(i).getProcessArrivalTime()<=timer ;i++){
                Process process = copiedProcesses.get(i);
                if(process.getProcessBurstTime()>0){
                    index=i;
                    sortedProcessAccordingToBurtTime.add(process);
                    if(i != copiedProcesses.size()-1){
                        nextValueToIncreaseTimer = copiedProcesses.get(i+1).getProcessArrivalTime();
                    }
                }
            }
            sortedProcessAccordingToBurtTime.sort(sortProcessAccordingToBurstTime);
            if(sortedProcessAccordingToBurtTime.size()>0){
                Process theProcessThatWillExecute = sortedProcessAccordingToBurtTime.get(0);
                Process theEarliestProcessToInterrupt = null;
                int minimumBurstTime=0;
                for(int i=index+1 ; i<copiedProcesses.size();i++){
                    Process process = copiedProcesses.get(i);
                    if(process.getProcessBurstTime()>0){
                        int latencyOfEachProcess = (timer/*+contextSwitch+*/+theProcessThatWillExecute.getProcessBurstTime())-process.getProcessArrivalTime()-process.getProcessBurstTime();
                        if(latencyOfEachProcess>0 && minimumBurstTime<latencyOfEachProcess){
                            minimumBurstTime = latencyOfEachProcess;
                            theEarliestProcessToInterrupt = process;
                        }
                    }
                }
                if(processesExecutionOrder.size()==0||!(processesExecutionOrder.get(processesExecutionOrder.size()-1).getProcessName().equals(theProcessThatWillExecute.getProcessName()))){
                    processesExecutionOrder.add(theProcessThatWillExecute);
                }
                indexInCopiedProcesses = copiedProcesses.indexOf(theProcessThatWillExecute);
                temporaryWaitingTimeForEachProcess[indexInCopiedProcesses]=timer-(processes.get(indexInCopiedProcesses).getProcessBurstTime()-theProcessThatWillExecute.getProcessBurstTime())-theProcessThatWillExecute.getProcessArrivalTime();
                if(theEarliestProcessToInterrupt!=null){
                    theProcessThatWillExecute.setProcessBurstTime(theProcessThatWillExecute.getProcessBurstTime()-(theEarliestProcessToInterrupt.getProcessArrivalTime()-timer));
                    timer = contextSwitch+theEarliestProcessToInterrupt.getProcessArrivalTime();
                }
                else {
                    timer = timer+contextSwitch+theProcessThatWillExecute.getProcessBurstTime();
                    temporaryTurnaroundTimeForEachProcess[indexInCopiedProcesses]=timer-theProcessThatWillExecute.getProcessArrivalTime();
                    theProcessThatWillExecute.setProcessBurstTime(0);
                    numberOfFinishedProcess++;
                }
            }
            else {
                timer = nextValueToIncreaseTimer;
            }
            if(numberOfFinishedProcess == copiedProcesses.size()){
                for(int i=0 ; i<temporaryTurnaroundTimeForEachProcess.length ; i++){
                    System.out.println(temporaryWaitingTimeForEachProcess[i]);
                    waitingTimeForEachProcess.add(temporaryWaitingTimeForEachProcess[i]);
                    turnaroundTimeForEachProcess.add(temporaryTurnaroundTimeForEachProcess[i]);
                }
                break;
            }
        }
        calculateAverageTurnAroundTime();
        calculateAverageWaitingTime();
    }

    public void shortestJopFirst() {
        clearHolders();
        int timer = 0;
        int nextValueToIncreaseTimer = 0;
        ArrayList<Process> processesSortedAccordingToBurstTime = new ArrayList<>();
        while (true) {
            processesSortedAccordingToBurstTime.clear();
            for (int i = 0; i < processes.size() && processes.get(i).getProcessArrivalTime() <= timer; i++) {

                if (processesExecutionOrder.indexOf(processes.get(i)) == -1) {   //Lw elprocess m4 running 2bl kda
                    processesSortedAccordingToBurstTime.add(processes.get(i));
                    if (i != processes.size() - 1) {
                        nextValueToIncreaseTimer = processes.get(i + 1).getProcessArrivalTime();
                    }
                }
            }
            //lw fe kza process d5lo fe nfs elw2t ba5d a22l burst
            if (processesSortedAccordingToBurstTime.size() != 0) {
                processesSortedAccordingToBurstTime.sort(sortProcessAccordingToBurstTime);
                Process executedProcess = processesSortedAccordingToBurstTime.get(0);
                processesExecutionOrder.add(executedProcess);
                waitingTimeForEachProcess.add(timer - executedProcess.getProcessArrivalTime());
                timer += executedProcess.getProcessBurstTime();
                turnaroundTimeForEachProcess.add(timer - executedProcess.getProcessArrivalTime());
            } else {
                timer = timer + nextValueToIncreaseTimer;
            }
            if (processesExecutionOrder.size() == processes.size()) {
                break;
            }
        }
        calculateAverageWaitingTime();
        calculateAverageTurnAroundTime();
    }

    public void PriorityScheduling() {
        clearHolders();
        ArrayList<Process> copiedProcesses = deepCopyProcessesArrayList();
        int timer = 0;
        int starvationTime = 3;
        int ageing = 0;
        int nextValueToIncreaseTimer = 0;
        ArrayList<Process> processesSortedAccordingToPriority = new ArrayList<>();
        while (true) {
            processesSortedAccordingToPriority.clear();
            for (int i = 0; i < copiedProcesses.size() && copiedProcesses.get(i).getProcessArrivalTime() <= timer; i++) {
                if (processesExecutionOrder.indexOf(copiedProcesses.get(i)) == -1) {
                    processesSortedAccordingToPriority.add(copiedProcesses.get(i));
                    //solution of starvation
                    ageing = (timer - copiedProcesses.get(i).getProcessArrivalTime()) / starvationTime;
                    copiedProcesses.get(i).setProcessPriorityNumber(copiedProcesses.get(i).getProcessPriorityNumber() - ageing);
                    if (copiedProcesses.get(i).getProcessPriorityNumber() <= 0) {
                        copiedProcesses.get(i).setProcessPriorityNumber(1);
                    }
                    if (i != copiedProcesses.size() - 1) {
                        nextValueToIncreaseTimer = copiedProcesses.get(i + 1).getProcessArrivalTime();
                    }
                }
            }
            if (processesSortedAccordingToPriority.size() != 0) {
                processesSortedAccordingToPriority.sort(sortProcessAccordingToPriority);
                Process executedProcess = processesSortedAccordingToPriority.get(0);
                processesExecutionOrder.add(executedProcess);
                waitingTimeForEachProcess.add(timer - executedProcess.getProcessArrivalTime());
                timer += executedProcess.getProcessBurstTime();
                turnaroundTimeForEachProcess.add(timer - executedProcess.getProcessArrivalTime());
            } else {
                timer = timer + nextValueToIncreaseTimer;
            }
            if (processesExecutionOrder.size() == processes.size()) {
                break;
            }
        }
        calculateAverageWaitingTime();
        calculateAverageTurnAroundTime();
    }

    public int checkIfThereIsBetterAg(AgProcess runningProcess, ArrayList<AgProcess> readyQueue) {
        int minimumAg = runningProcess.getAgFactor();
        int indexOfMinimum = -1;
        for (int i = 0; i < readyQueue.size(); i++) {
            if (readyQueue.get(i).getAgFactor() < minimumAg) {
                minimumAg = readyQueue.get(i).getAgFactor();
                indexOfMinimum = i;
            }
        }
        return indexOfMinimum;
    }

    public void ag(int quantum) {
        clearHolders();
        ArrayList<AgProcess> readyQueue = new ArrayList<>();
        ArrayList<AgProcess> agProcesses = new ArrayList<>();
        int [] timeOfFinishedProcesses = new int[processes.size()];
        ArrayList<AgProcess> similarProcessesAccordingToArrivalTime = new ArrayList<>();
        AgProcess runningProcess = null;
        int timer = 0;
        int nextArrivalTimeForTheNextProcess = 0;
        int numberOfFinishedProcesses = 0;
        int startingIndexOfAg = 0;
        makeAgProcesses(quantum, agProcesses);
        StringBuilder stringBuilder = new StringBuilder();
        do {
            stringBuilder.delete(0, stringBuilder.length());
            for (int i = 0; i < agProcesses.size(); i++) {
                stringBuilder.append(agProcesses.get(i).getQuantum());
                if (i < agProcesses.size() - 1) {
                    stringBuilder.append(",");
                }
            }
//            quantumOverTime.add(stringBuilder.toString());
            similarProcessesAccordingToArrivalTime.clear();
            startingIndexOfAg = getNextStartingIndexOfAgProcess(startingIndexOfAg, readyQueue, agProcesses, timer, similarProcessesAccordingToArrivalTime);
            nextArrivalTimeForTheNextProcess = getNextArrivalTime(startingIndexOfAg, agProcesses);
            similarProcessesAccordingToArrivalTime.sort(sortAccordingToAgFactor);
            readyQueue.addAll(similarProcessesAccordingToArrivalTime);
            if (runningProcess == null) {
                runningProcess = readyQueue.remove(0);
            }
            quantumOverTime.add(runningProcess.getProcessName() + ": " + stringBuilder.toString());
            if (runningProcess.getProcessBurstTime() <= ((int) Math.ceil(runningProcess.getQuantum() / 2.0))) {//checked
                timer += runningProcess.getProcessBurstTime();
                timeOfFinishedProcesses[agProcesses.indexOf(runningProcess)]=timer;
                startingIndexOfAg = getNextStartingIndexOfAgProcessesAndFillTheQueue(startingIndexOfAg, readyQueue, agProcesses, timer);
                nextArrivalTimeForTheNextProcess = getNextArrivalTime(startingIndexOfAg, agProcesses);
                runningProcess.setProcessBurstTime(0);
                runningProcess.setQuantum(0);
                processesExecutionOrder.add(runningProcess);
                if (readyQueue.isEmpty()) {
                    timer = nextArrivalTimeForTheNextProcess;
                    runningProcess = null;
                } else {
                    runningProcess = readyQueue.remove(0);
                }
                numberOfFinishedProcesses++;
            } else if (runningProcess.getProcessBurstTime() > Math.ceil(runningProcess.getQuantum() / 2.0) && runningProcess.getProcessBurstTime() <= runningProcess.getQuantum()) {//checked
                timer += Math.ceil(runningProcess.getQuantum() / 2.0);
                runningProcess.setProcessBurstTime((runningProcess.getProcessBurstTime() - (int) Math.ceil(runningProcess.getQuantum() / 2.0)));
                startingIndexOfAg = getNextStartingIndexOfAgProcessesAndFillTheQueue(startingIndexOfAg, readyQueue, agProcesses, timer);
                nextArrivalTimeForTheNextProcess = getNextArrivalTime(startingIndexOfAg, agProcesses);
                int indexOfBetterAg = checkIfThereIsBetterAg(runningProcess, readyQueue);
                processesExecutionOrder.add(runningProcess);
                if (indexOfBetterAg != -1) {
                    runningProcess.setQuantum((runningProcess.getQuantum() + (int) Math.ceil(runningProcess.getQuantum() / 2.0)));
                    readyQueue.add(runningProcess);
                    runningProcess = readyQueue.remove(indexOfBetterAg);
                } else {
                    if (nextArrivalTimeForTheNextProcess < (timer + runningProcess.getProcessBurstTime()) && startingIndexOfAg < agProcesses.size()) {// will arrive before i finish execution
                        while (true) {
                            runningProcess.setProcessBurstTime(runningProcess.getProcessBurstTime() - (nextArrivalTimeForTheNextProcess - timer));
                            int remainingFromQuantum = (runningProcess.getQuantum() - (int) Math.ceil(runningProcess.getQuantum() / 2.0) - (nextArrivalTimeForTheNextProcess - timer));
                            timer = nextArrivalTimeForTheNextProcess;
                            startingIndexOfAg = getNextStartingIndexOfAgProcessesAndFillTheQueue(startingIndexOfAg, readyQueue, agProcesses, timer);
                            nextArrivalTimeForTheNextProcess = getNextArrivalTime(startingIndexOfAg, agProcesses);
                            indexOfBetterAg = checkIfThereIsBetterAg(runningProcess, readyQueue);
                            if (indexOfBetterAg != -1) {
                                runningProcess.setQuantum(runningProcess.getQuantum() + remainingFromQuantum);
                                readyQueue.add(runningProcess);
                                runningProcess = readyQueue.remove(indexOfBetterAg);
                                break;
                            } else {
                                if (nextArrivalTimeForTheNextProcess < timer + runningProcess.getProcessBurstTime() && startingIndexOfAg < agProcesses.size()) {
                                    continue;
                                } else {
                                    timer += runningProcess.getProcessBurstTime();
                                    runningProcess.setProcessBurstTime(0);
                                    runningProcess.setQuantum(0);
                                    timeOfFinishedProcesses[agProcesses.indexOf(runningProcess)]=timer;
                                    numberOfFinishedProcesses++;
                                    if (readyQueue.size() != 0) {
                                        runningProcess = readyQueue.remove(0);
                                    } else {
                                        runningProcess = null;
                                        timer = nextArrivalTimeForTheNextProcess;
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        timer += runningProcess.getProcessBurstTime();
                        runningProcess.setProcessBurstTime(0);
                        runningProcess.setQuantum(0);
                        timeOfFinishedProcesses[agProcesses.indexOf(runningProcess)]=timer;
                        numberOfFinishedProcesses++;
                        if (readyQueue.size() != 0) {
                            runningProcess = readyQueue.remove(0);
                        } else {
                            runningProcess = null;
                            timer = nextArrivalTimeForTheNextProcess;
                        }

                    }
                }
            } else {
                timer += (int) Math.ceil(runningProcess.getQuantum() / 2.0);
                runningProcess.setProcessBurstTime((runningProcess.getProcessBurstTime() - (int) Math.ceil(runningProcess.getQuantum() / 2.0)));
                startingIndexOfAg = getNextStartingIndexOfAgProcessesAndFillTheQueue(startingIndexOfAg, readyQueue, agProcesses, timer);
                nextArrivalTimeForTheNextProcess = getNextArrivalTime(startingIndexOfAg, agProcesses);
                int indexOfBetterAg = checkIfThereIsBetterAg(runningProcess, readyQueue);
                processesExecutionOrder.add(runningProcess);
                if (indexOfBetterAg != -1) {
                    runningProcess.setQuantum((runningProcess.getQuantum() + (runningProcess.getQuantum() - (int) Math.ceil(runningProcess.getQuantum() / 2.0))));
                    readyQueue.add(runningProcess);
                    runningProcess = readyQueue.remove(indexOfBetterAg);
                } else {
                    if (nextArrivalTimeForTheNextProcess < timer + (runningProcess.getQuantum() - (int) Math.ceil(runningProcess.getQuantum() / 2.0)) && startingIndexOfAg < agProcesses.size()) {
                        while (true) {
                            runningProcess.setProcessBurstTime(runningProcess.getProcessBurstTime() - (nextArrivalTimeForTheNextProcess - timer));
                            int remainingFromQuantum = (runningProcess.getQuantum() - (int) Math.ceil(runningProcess.getQuantum() / 2.0) - (nextArrivalTimeForTheNextProcess - timer));
                            timer = nextArrivalTimeForTheNextProcess;
                            startingIndexOfAg = getNextStartingIndexOfAgProcessesAndFillTheQueue(startingIndexOfAg, readyQueue, agProcesses, timer);
                            nextArrivalTimeForTheNextProcess = getNextArrivalTime(startingIndexOfAg, agProcesses);
                            indexOfBetterAg = checkIfThereIsBetterAg(runningProcess, readyQueue);
                            if (indexOfBetterAg != -1) {
                                runningProcess.setQuantum(runningProcess.getQuantum() + remainingFromQuantum);
                                readyQueue.add(runningProcess);
                                runningProcess = readyQueue.remove(indexOfBetterAg);
                                break;
                            } else {
                                if (nextArrivalTimeForTheNextProcess < timer + (runningProcess.getQuantum() - (int) Math.ceil(runningProcess.getQuantum() / 2.0)) && startingIndexOfAg < agProcesses.size()) {
                                    continue;
                                } else {
                                    timer += remainingFromQuantum;
                                    runningProcess.setProcessBurstTime(runningProcess.getProcessBurstTime() - runningProcess.getQuantum());
                                    runningProcess.setQuantum(runningProcess.getQuantum() + calculateTenPercentOfMeanOfAllQuantum(agProcesses));
                                    readyQueue.add(runningProcess);
                                    if (readyQueue.size() != 0) {
                                        runningProcess = readyQueue.remove(0);
                                    } else {
                                        runningProcess = null;
                                        timer = nextArrivalTimeForTheNextProcess;
                                    }
                                    break;
                                }
                            }
                        }
                    } else {
                        timer += runningProcess.getQuantum() - (int) Math.ceil(runningProcess.getQuantum() / 2.0);
                        runningProcess.setProcessBurstTime(runningProcess.getProcessBurstTime() - (runningProcess.getQuantum() - (int) Math.ceil(runningProcess.getQuantum() / 2.0)));
                        runningProcess.setQuantum(runningProcess.getQuantum() + calculateTenPercentOfMeanOfAllQuantum(agProcesses));
                        readyQueue.add(runningProcess);
                        if (readyQueue.size() != 0) {
                            runningProcess = readyQueue.remove(0);
                        } else {
                            runningProcess = null;
                            timer = nextArrivalTimeForTheNextProcess;
                        }
                    }
                }
            }
            // System.out.println(numberOfFinishedProcesses);
        } while (numberOfFinishedProcesses != processes.size());
        //System.out.println(timer);
        quantumOverTime.add("0,0,0,0");
        calculateTurnAroundAndWaiting(timeOfFinishedProcesses);
        calculateAverageTurnAroundTime();
        calculateAverageWaitingTime();
    }

    private void makeAgProcesses(int quantum, ArrayList<AgProcess> agProcesses) {
        for (int i = 0; i < processes.size(); i++) {
            agProcesses.add(new AgProcess(processes.get(i).getProcessName(), processes.get(i).getProcessColor(), processes.get(i).getProcessArrivalTime(), processes.get(i).getProcessBurstTime(), processes.get(i).getProcessPriorityNumber(), quantum));
            agProcesses.get(i).calculateAgFactor();
        }
    }

    private boolean canEnterReadyQueue(ArrayList<AgProcess> readyQueue, ArrayList<AgProcess> agProcesses, int timer, int i) {
        return i < agProcesses.size() && agProcesses.get(i).getProcessArrivalTime() <= timer && !readyQueue.contains(agProcesses.get(i)) && agProcesses.get(i).getProcessBurstTime() != 0;
    }

    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public ArrayList<Process> getProcessesExecutionOrder() {
        return processesExecutionOrder;
    }

    public ArrayList<Integer> getWaitingTimeForEachProcess() {
        return waitingTimeForEachProcess;
    }

    public ArrayList<Integer> getTurnaroundTimeForEachProcess() {
        return turnaroundTimeForEachProcess;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public double getAverageTurnaroundTime() {
        return averageTurnaroundTime;
    }

    private void clearHolders() {
        processesExecutionOrder.clear();
        waitingTimeForEachProcess.clear();
        turnaroundTimeForEachProcess.clear();
    }

    private void calculateAverageWaitingTime() {
        averageWaitingTime = 0;
        for (int i = 0; i < waitingTimeForEachProcess.size(); i++) {
            averageWaitingTime += waitingTimeForEachProcess.get(i);
        }
        averageWaitingTime = averageWaitingTime / waitingTimeForEachProcess.size();
    }

    private void calculateAverageTurnAroundTime() {
        averageTurnaroundTime = 0;
        for (int i = 0; i < turnaroundTimeForEachProcess.size(); i++) {
            averageTurnaroundTime += turnaroundTimeForEachProcess.get(i);
        }
        averageTurnaroundTime = averageTurnaroundTime / turnaroundTimeForEachProcess.size();
    }

    private int calculateTenPercentOfMeanOfAllQuantum(ArrayList<AgProcess> agProcesses) {
        int sum = 0;
        for (int i = 0; i < agProcesses.size(); i++) {
            sum += agProcesses.get(i).getQuantum();
        }

        return (int) (Math.ceil(0.1 * (sum / (double) agProcesses.size())));
    }

    private ArrayList<Process> deepCopyProcessesArrayList() {
        ArrayList<Process> copiedProcesses = new ArrayList<>();
        Process process;
        for (int i = 0; i < processes.size(); i++) {
            process = processes.get(i);
            copiedProcesses.add(new Process(process.getProcessName(), process.getProcessColor(), process.getProcessArrivalTime(), process.getProcessBurstTime(), process.getProcessPriorityNumber()));
        }
        process = null;
        return copiedProcesses;
    }

    private int getNextStartingIndexOfAgProcess(int startingIndexOfAg, ArrayList<AgProcess> readyQueue, ArrayList<AgProcess> agProcesses, int timer, ArrayList<AgProcess> similarProcessesAccordingToArrivalTime) {
        int temporaryStartingIndexOfAg = startingIndexOfAg;
        for (int i = temporaryStartingIndexOfAg; canEnterReadyQueue(readyQueue, agProcesses, timer, i); i++) {
            similarProcessesAccordingToArrivalTime.add(agProcesses.get(i));
            temporaryStartingIndexOfAg = i + 1;
        }
        return temporaryStartingIndexOfAg;
    }

    private int getNextStartingIndexOfAgProcessesAndFillTheQueue(int startingIndexOfAg, ArrayList<AgProcess> readyQueue, ArrayList<AgProcess> agProcesses, int timer) {
        int temporaryStartingIndexOfAg = startingIndexOfAg;
        for (int i = temporaryStartingIndexOfAg; canEnterReadyQueue(readyQueue, agProcesses, timer, i); i++) {
            readyQueue.add(agProcesses.get(i));
            temporaryStartingIndexOfAg = i + 1;
        }
        return temporaryStartingIndexOfAg;
    }

    private int getNextArrivalTime(int startingIndexOfAg, ArrayList<AgProcess> agProcesses) {
        try {
            return agProcesses.get(startingIndexOfAg).getProcessArrivalTime();
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }


}
