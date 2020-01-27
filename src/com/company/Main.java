package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<Process>processes = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        int numberOfProcess;
        System.out.println("Enter Number of processes ");
        numberOfProcess = input.nextInt();
        for(int i=0 ; i<numberOfProcess ; i++){
            processes.add(new Process(input.next(), input.next() , input.nextInt() , input.nextInt() , input.nextInt()));
        }
        System.out.println("-----------------------------------------------");
        Scheduler scheduler = new Scheduler(processes);
        scheduler.ag(4);
        for(int i=0 ; i<scheduler.getProcessesExecutionOrder().size();i++){
            System.out.print(scheduler.getProcessesExecutionOrder().get(i).getProcessName()+" , ");
            /*System.out.println("waiting time : "+scheduler.getWaitingTimeForEachProcess().get(i));
            System.out.println("turn Around : "+scheduler.getTurnaroundTimeForEachProcess().get(i));*/

        }
       /* for(int i=0 ; i<scheduler.getWaitingTimeForEachProcess().size();i++){
            System.out.println("waiting time for "+scheduler.getProcesses().get(i).getProcessName()+" : "+scheduler.getWaitingTimeForEachProcess().get(i));
            System.out.println("turn Around for "+scheduler.getProcesses().get(i).getProcessName()+" : "+scheduler.getTurnaroundTimeForEachProcess().get(i));

        }*/
        System.out.println("Average waiting Time : "+scheduler.getAverageWaitingTime());
        System.out.println("Average turn around time : "+scheduler.getAverageTurnaroundTime());
        for (int i=0 ; i<scheduler.getQuantumOverTime().size() ; i++){
            System.out.println(scheduler.getQuantumOverTime().get(i));
        }
        /*4
p1 red 0 17 4
p2 red 3 6 9
p3 red 4 10 3
p4 red 29 4 8*/
    }
}
