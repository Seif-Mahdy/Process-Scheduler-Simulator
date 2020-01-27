package com.company;

import java.awt.Color;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.imageio.ImageIO;

public class GanttDemo2 extends ApplicationFrame {

    private static final long serialVersionUID = 1L;
    public static final TaskSeriesCollection model = new TaskSeriesCollection();
    public static ArrayList<Process>executionOrder;
    public static ArrayList<Process>processes;

    public GanttDemo2(final String title,ArrayList<Process>y,ArrayList<Process>x) {
        super(title);
        executionOrder=x;
        processes=y;

        final IntervalCategoryDataset dataset = createSampleDataset();
        // create the chart...
        final JFreeChart chart = ChartFactory.createGanttChart(
                "", // chart title
                "Processes", // domain axis label
                "Order", // range axis label
                dataset, // data
                false, // include legend
                true, // tooltips
                false // urls
        );
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getRangeAxis();
        axis.setTickLabelsVisible(false);
//        DateAxis range = (DateAxis) plot.getRangeAxis();
//        range.setDateFormatOverride(new SimpleDateFormat("SSS"));
//        range.setMaximumDate(new Date(100));

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(900, 270));


        setContentPane(chartPanel);
        //GanttRenderer personnalisÃ©..
        MyRenderer renderer = new MyRenderer(model);
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.lightGray);
        BufferedImage chartImage =  chart.createBufferedImage(900,270,null);
        File file = new File("F:\\FCI\\CPU_Schedulers_Simulator_A3\\src\\com\\company\\graph.png");
        try {
            ImageIO.write(chartImage,"png",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class MyRenderer extends GanttRenderer {

        private static final int PASS = 2; // assumes two passes
        private final List<Color> clut = new ArrayList<>();
        private final TaskSeriesCollection model;
        private int row;
        private int col;
        private int index;

        public MyRenderer(TaskSeriesCollection model) {
            this.model = model;
        }

        @Override
        public Paint getItemPaint(int row, int col) {

            if (clut.isEmpty() || this.row != row || this.col != col) {
                initClut(row, col);
                this.row = row;
                this.col = col;
                index = 0;
            }
            int clutIndex = index++ / PASS;
            return clut.get(clutIndex);
        }

        private void initClut(int row, int col) {
            Map<String,Color> colorMap = new HashMap<>();
            for(int i=0;i<processes.size();i++)
            {
                String color = processes.get(i).getProcessColor();
                if(color.equals("Red"))
                {
                    colorMap.put(processes.get(i).getProcessName(),Color.RED);
                }
                else if(color.equals("Green"))
                {
                    colorMap.put(processes.get(i).getProcessName(),Color.GREEN);
                }
                else if(color.equals("Blue"))
                {
                    colorMap.put(processes.get(i).getProcessName(),Color.BLUE);
                }
                else if(color.equals("Yellow"))
                {
                    colorMap.put(processes.get(i).getProcessName(),Color.YELLOW);
                }
                else if(color.equals("Pink"))
                {
                    colorMap.put(processes.get(i).getProcessName(),Color.PINK);
                }
                else if(color.equals("Cyan"))
                {
                    colorMap.put(processes.get(i).getProcessName(),Color.CYAN);
                }
            }
            clut.clear();

            TaskSeries series = (TaskSeries) model.getRowKeys().get(row);
            List<Task> tasks = series.getTasks(); // unchecked

            int taskCount = tasks.get(col).getSubtaskCount();
            taskCount = Math.max(1, taskCount);

            String description;

            for (int i = 0; i < taskCount; i++) {
                description = tasks.get(col).getSubtask(i).getDescription();

                Color clr = colorMap.get(description);
                clut.add(clr);

            }
        }
    }

    private IntervalCategoryDataset createSampleDataset() {


        final TaskSeries s1 = new TaskSeries("");
        for(int i=0;i<processes.size();i++)
        {

            final Task t0 = new Task(processes.get(i).getProcessName(), new SimpleTimePeriod(0, 100));
            //System.out.println("parent: "+t0.getDescription());

            s1.add(t0);
        }
        int start=0,end=5;
        for(int j=0;j<executionOrder.size();j++)
        {

            String description = executionOrder.get(j).getProcessName();
            for(int i=0;i<s1.getTasks().size();i++)
            {
                if(description.equals(s1.get(i).getDescription()))
                {
                    final Task t1 = new Task(s1.get(i).getDescription(), new SimpleTimePeriod(start, end));
                    s1.get(i).addSubtask(t1);
                    start=end+1;
                    end+=5+1;
                }
            }

        }
        model.add(s1);
        return model;

    }



}