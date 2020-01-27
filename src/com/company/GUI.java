package com.company;


import javafx.application.Application;
import javafx.geometry.Insets;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class GUI extends Application {
    Stage window;
    Scene scene1;
    Scene scene2;
    GridPane grid;
    GridPane grid2 = new GridPane();
    int numberOfProcesses;
    ArrayList<Process> processes = new ArrayList<>();
    ArrayList<Process> processesExecution = new ArrayList<>();
    Map<String, Integer> processFrequency = new HashMap<>();
    Scheduler schedulerObj = new Scheduler();
    int quantum;
    int technique;
    int contextSwitching;
    double averageWaitingTime;
    double averageTurnaroundTime;
    TextField numberOfProcessesTextField;
    TextField processesQuantum;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;
    TextField processNameTextField;
    ComboBox processColorComboBox;
    TextField processArrivalTimeTextField;
    TextField processBurstTimeTextField;
    TextField processPriorityTextField;
    TextField processesContextSwitchingTextField;
    Label processesAverageWaitingTimeLabel;
    Label ProcessesAverageTurnAroundTimeLabel;
    Label imageLabel;
    int option=0;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid2.setPadding(new Insets(10, 10, 10, 10));
        grid2.setHgap(10);
        grid2.setVgap(10);

        //label for the number of processes
        Label label = new Label("Enter the number of processes");
        GridPane.setConstraints(label, 0, 0);
        grid.getChildren().add(label);

        //text field for the number of processes
        numberOfProcessesTextField = new TextField();
        GridPane.setConstraints(numberOfProcessesTextField, 0, 1);
        grid.getChildren().add(numberOfProcessesTextField);

        //label for the quantum
        Label processesQuantumLabel = new Label("Enter the quantum for the processes: ");
        GridPane.setConstraints(processesQuantumLabel, 0, 2);
        grid.getChildren().add(processesQuantumLabel);

        //text field for the quantum
        processesQuantum = new TextField();
        GridPane.setConstraints(processesQuantum, 0, 3);
        grid.getChildren().add(processesQuantum);

        //label for the context switching
        Label processesContextSwitchingLabel = new Label("Enter the context switching: ");
        GridPane.setConstraints(processesContextSwitchingLabel, 0, 4);
        grid.getChildren().add(processesContextSwitchingLabel);

        //text field for the context switching
        processesContextSwitchingTextField = new TextField();
        GridPane.setConstraints(processesContextSwitchingTextField, 0, 5);
        grid.getChildren().add(processesContextSwitchingTextField);

        //label for choosing the scheduling technique
        Label techniqueLabel = new Label("Select the scheduling technique : ");
        GridPane.setConstraints(techniqueLabel, 0, 6);
        grid.getChildren().add(techniqueLabel);

        //radio buttons for the techniques
        final ToggleGroup group = new ToggleGroup();

        rb1 = new RadioButton("SJF ");
        rb1.setToggleGroup(group);
        GridPane.setConstraints(rb1, 0, 7);
        grid.getChildren().add(rb1);

        rb2 = new RadioButton("SRTF ");
        rb2.setToggleGroup(group);
        GridPane.setConstraints(rb2, 0, 8);
        grid.getChildren().add(rb2);

        rb3 = new RadioButton("Priority ");
        rb3.setToggleGroup(group);
        GridPane.setConstraints(rb3, 0, 9);
        grid.getChildren().add(rb3);

        rb4 = new RadioButton("AG ");
        rb4.setToggleGroup(group);
        GridPane.setConstraints(rb4, 0, 10);
        grid.getChildren().add(rb4);

        //label for the process name
        Label processNameLabel = new Label("Enter the process name: ");
        GridPane.setConstraints(processNameLabel, 0, 4);
        grid2.getChildren().add(processNameLabel);

        //text field for the process name
        processNameTextField = new TextField();
        //processNameTextField.setPadding(new Insets(10,0,0,0));
        GridPane.setConstraints(processNameTextField, 0, 5);
        grid2.getChildren().add(processNameTextField);
        //label for the process color
        Label processColorLabel = new Label("Enter the process color: ");
        GridPane.setConstraints(processColorLabel, 1, 4);
        grid2.getChildren().add(processColorLabel);

        //drop box for the process color
        processColorComboBox = new ComboBox();
        processColorComboBox.getItems().addAll("Red", "Green", "Blue", "Yellow", "Pink", "Cyan");
        GridPane.setConstraints(processColorComboBox, 1, 5);
        grid2.getChildren().add(processColorComboBox);

        //label for process arrival time
        Label processArrivalTimeLabel = new Label("Enter the process arrival time: ");
        GridPane.setConstraints(processArrivalTimeLabel, 2, 4);
        grid2.getChildren().add(processArrivalTimeLabel);

        //text field for the process arrival time
        processArrivalTimeTextField = new TextField();
        GridPane.setConstraints(processArrivalTimeTextField, 2, 5);
        grid2.getChildren().add(processArrivalTimeTextField);

        //label for the process burst time
        Label processBurstTimeLabel = new Label("Enter the process burst time: ");

        GridPane.setConstraints(processBurstTimeLabel, 3, 4);
        grid2.getChildren().add(processBurstTimeLabel);

        //text field for the process burst time
        processBurstTimeTextField = new TextField();
        GridPane.setConstraints(processBurstTimeTextField, 3, 5);
        grid2.getChildren().add(processBurstTimeTextField);

        //label for the process priority
        Label processPriorityLabel = new Label("Enter the priority for the process: ");
        GridPane.setConstraints(processPriorityLabel, 4, 4);
        grid2.getChildren().add(processPriorityLabel);

        //text field for the process priority
        processPriorityTextField = new TextField();
        GridPane.setConstraints(processPriorityTextField, 4, 5);
        grid2.getChildren().add(processPriorityTextField);

        //graph image
        imageLabel = new Label("");
        FileInputStream inputstream = new FileInputStream("/home/seif/Desktop/projects/OS/CPU_Schedulers_Simulator_A3/src/com/company/graph2.png");
        Image image = new Image(inputstream);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(270);
        imageView.setFitWidth(850);
        imageLabel.setGraphic(imageView);
        GridPane r = new GridPane();
        Label newLabel = new Label("                ");
        Label newLabel2 = new Label("      \n \n \n \n \n ");
        GridPane.setConstraints(newLabel, 1, 0);
        GridPane.setConstraints(newLabel2, 0, 0);
        GridPane.setConstraints(imageLabel, 0, 5);
        r.getChildren().add(newLabel);

        r.getChildren().add(imageLabel);
        r.getChildren().add(newLabel2);
        //label for the average waiting time
        processesAverageWaitingTimeLabel = new Label("");
        processesAverageWaitingTimeLabel.setStyle("-fx-font-weight: bold;");
        GridPane.setConstraints(processesAverageWaitingTimeLabel, 2, 0);
        grid2.getChildren().add(processesAverageWaitingTimeLabel);

        //label for the average turnaround time
        ProcessesAverageTurnAroundTimeLabel = new Label("");
        ProcessesAverageTurnAroundTimeLabel.setStyle("-fx-font-weight: bold;");
        GridPane.setConstraints(ProcessesAverageTurnAroundTimeLabel, 4, 0);
        grid2.getChildren().add(ProcessesAverageTurnAroundTimeLabel);

        //button for loading data from file
        Button readDataFromFile = new Button("Load From File");
        GridPane.setConstraints(readDataFromFile, 2, 6);
        grid2.getChildren().add(readDataFromFile);
        readDataFromFile.setOnAction(e -> {
            readFromFile();
            option=1;
        });

        //button for adding a process
        Button addProcessButton = new Button("Add Process");
        GridPane.setConstraints(addProcessButton, 3, 6);
        grid2.getChildren().add(addProcessButton);
        addProcessButton.setOnAction(e -> {
            addProcess();
            System.out.println("Process added successfully");
            processNameTextField.clear();
            processArrivalTimeTextField.clear();
            processPriorityTextField.clear();
            processBurstTimeTextField.clear();
        });

        //button for scheduling
        Button button = new Button("Schedule");
        GridPane.setConstraints(button, 4, 6);
        grid2.getChildren().add(button);
        button.setOnAction(e -> {
            if(option==0)
            {
                getInput();
            }
            schedule(technique);
            prepareDataForGraph();
            GanttDemo2 obj = new GanttDemo2("CPU Scheduler", processes, processesExecution);
            overWriteImage();
        });

        grid.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        grid2.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        r.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane dummy = new GridPane();
        dummy.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        BorderPane pane = new BorderPane();
        pane.setLeft(grid);
        pane.setRight(r);
        pane.setBottom(grid2);
        pane.setCenter(dummy);
        scene1 = new Scene(pane, 1200, 500);
        window.setScene(scene1);
        window.setTitle("CPU scheduler");
        window.show();
    }

    public void addProcess() {
        String processName = processNameTextField.getText();
        String processColor = processColorComboBox.getValue().toString();
        int processArrivalTime = Integer.parseInt(processArrivalTimeTextField.getText());
        int processBurstTime = Integer.parseInt(processBurstTimeTextField.getText());
        int processPriority = Integer.parseInt(processPriorityTextField.getText());
        processes.add(new Process(processName, processColor, processArrivalTime, processBurstTime, processPriority));
    }

    public void getInput() {
        numberOfProcesses = Integer.parseInt(numberOfProcessesTextField.getText());
        quantum = Integer.parseInt(processesQuantum.getText());
        contextSwitching = Integer.parseInt(processesContextSwitchingTextField.getText());
        if (rb1.isSelected()) {
            technique = 1;
        } else if (rb2.isSelected()) {
            technique = 2;
        } else if (rb3.isSelected()) {
            technique = 3;
        } else if (rb4.isSelected()) {
            technique = 4;
        }
    }

    //TODO(1) calling scheduling object
    public void schedule(int technique) {
        schedulerObj.setProcesses(processes);
        if (technique == 1) {
            schedulerObj.shortestJopFirst();
        } else if (technique == 2) {
            schedulerObj.shortestRemainingTimeFirst(contextSwitching);
        } else if (technique == 3) {
            schedulerObj.PriorityScheduling();
        } else if (technique == 4) {
            schedulerObj.ag(quantum);
            ArrayList<String> temp = schedulerObj.getQuantumOverTime();
            ArrayList<Process> execution = schedulerObj.getProcessesExecutionOrder();

            int y = 0;
            GridPane gridPane = new GridPane();
            gridPane.setVgap(10);
            gridPane.setHgap(10);
            for (int i = 0; i < temp.size(); i++) {
                Label lbl = new Label();
                lbl.setText(temp.get(i));
                lbl.setStyle("-fx-font-weight: bold;");
                GridPane.setConstraints(lbl, 0, y += 2);
                gridPane.getChildren().add(lbl);
            }
            Scene scene = new Scene(gridPane, 300, 450);
            Stage window = new Stage();
            window.setScene(scene);
            window.setX(100);
            window.setTitle("AG Quantum");
            window.show();
        }

    }

    public void prepareDataForGraph() {
        processesExecution = schedulerObj.getProcessesExecutionOrder();
        averageWaitingTime = schedulerObj.getAverageWaitingTime();
        averageTurnaroundTime = schedulerObj.getAverageTurnaroundTime();
//        for(int i=0;i<processesExecution.size();i++)
//        {
//            System.out.println(processesExecution.get(i).getProcessName());
//        }
        for (int i = 0; i < numberOfProcesses; i++) {
            processFrequency.put(processes.get(i).getProcessName(), 0);
        }
        for (int i = 0; i < processesExecution.size(); i++) {
            processFrequency.put(processesExecution.get(i).getProcessName(), processFrequency.get(processesExecution.get(i).getProcessName()) + 1);
        }
        System.out.println(processFrequency);
    }

    public void overWriteImage() {

        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("/home/seif/Desktop/projects/OS/CPU_Schedulers_Simulator_A3/src/com/company/graph.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(inputstream);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(270);
        imageView.setFitWidth(850);
        imageLabel.setGraphic(imageView);

        DecimalFormat numberFormat = new DecimalFormat("#.00");
        //System.out.println(numberFormat.format(number));
        processesAverageWaitingTimeLabel.setText("Average waiting time: " + numberFormat.format(averageWaitingTime));
        ProcessesAverageTurnAroundTimeLabel.setText("Average turnaround time: " + numberFormat.format(averageTurnaroundTime));
    }

    public void readFromFile() {
        File file = new File("/home/seif/Desktop/projects/OS/CPU_Schedulers_Simulator_A3/src/com/company/input.txt");
        Scanner readFile = null;
        try {
            readFile = new Scanner(file);
            numberOfProcesses = readFile.nextInt();
            quantum = readFile.nextInt();
            contextSwitching = readFile.nextInt();
            String teq = readFile.next();
            if (teq.equals("SJF")) {
                technique = 1;
            } else if (teq.equals("SRTF")) {
                technique = 2;
            } else if (teq.equals("Priority")) {
                technique = 3;
            } else if (teq.equals("AG")) {
                technique = 4;
            }
            for (int i = 0; i < numberOfProcesses; i++) {
                Process p = new Process(readFile.next(),readFile.next() , Integer.parseInt(readFile.next()), Integer.parseInt(readFile.next()), Integer.parseInt(readFile.next()));
                processes.add(p);
            }
            System.out.println("Data loaded successfully");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
