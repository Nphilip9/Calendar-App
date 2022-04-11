package com.nphilip.calendar.manager;

import com.nphilip.calendar.listView.Task;
import com.nphilip.calendar.util.SortingTypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TaskManager {

    public void deleteTask(String path, String date, int ID) {
        ArrayList<Task> tasks = getTasksFromDate(path, date);
        System.out.println(Arrays.toString(tasks.toArray()));
        tasks.remove(ID);
        System.out.println(Arrays.toString(tasks.toArray()));
        if(new File(path + "-" + date + ".txt").delete()) {
            for (Task task : tasks) {
                addTask(path, task);
            }
        }
    }

    public ArrayList<Task> getTasksFromDate(String path, String date) {
        path = path + "-" + date + ".txt";
        ArrayList<Task> tasks = new ArrayList<>();
        if(new File(path).exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(path);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

                String strLine;

                while ((strLine = bufferedReader.readLine()) != null) {
                    String[] lineSplit = strLine.split("##");
                    tasks.add(new Task(lineSplit[0], lineSplit[1], lineSplit[2], Integer.parseInt(lineSplit[3]), Boolean.parseBoolean(lineSplit[4]), Boolean.parseBoolean(lineSplit[5])));
                }
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }

    public ArrayList<Task> getAllTasks(String path) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (File file : Objects.requireNonNull(new File(path).listFiles())) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

                String strLine;

                while ((strLine = bufferedReader.readLine()) != null) {
                    String[] lineSplit = strLine.split("##");
                    tasks.add(new Task(lineSplit[0], lineSplit[1], lineSplit[2], Integer.parseInt(lineSplit[3]), Boolean.parseBoolean(lineSplit[4]), Boolean.parseBoolean(lineSplit[5])));
                }
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }

    public void addTask(String path, Task task) {
        try {
            FileWriter fileWriter = new FileWriter(path + "-" +  task.getDate() + ".txt", true);
            fileWriter.append(task.getTitle()).append("##").append(task.getDescription()).append("##").append(task.getDate()).append("##").append(String.valueOf(task.getImportance())).append("##").append(String.valueOf(task.isProtected())).append("##").append(String.valueOf(task.isDone())).append("\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
