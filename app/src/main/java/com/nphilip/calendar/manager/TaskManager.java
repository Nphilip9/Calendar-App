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

public class TaskManager {

    /**
     * Sorts the list of tasks by the given sorting type
     * @param path String
     * @param sortingType Integer
     * @return ArrayList<Task>
     */
    public ArrayList<Task> sort(String path, SortingTypes sortingType) {
        ArrayList<Task> tasks = getTasks(path);
        String[][] sortedTasks = new String[tasks.size()][6];
        switch(sortingType) {
            case SORT_BY_NAME:
                sortedTasks = new SortManager().startSorting(getTasks(path), SortingTypes.SORT_BY_NAME);
                break;
            case SORT_BY_IMPORTANCE:
                sortedTasks = new SortManager().startSorting(getTasks(path), SortingTypes.SORT_BY_IMPORTANCE);
                break;
            case SORT_BY_TIME:
                sortedTasks = new SortManager().startSorting(getTasks(path), SortingTypes.SORT_BY_TIME);
                break;
            default:
                break;
        }

        tasks.clear();
        for (String[] task : sortedTasks) {
            tasks.add(new Task(task[0],task[1], task[2], Integer.parseInt(task[3]), Boolean.parseBoolean(task[4]), Boolean.parseBoolean(task[5])));
        }
        return tasks;
    }

    /**
      Returns a list of the Tasks
     * @param path String
     * @return ArrayList<Task>
     */
    public ArrayList<Task> getTasks(String path) {
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

    /**
     * Sorts the tasks by date and returns all tasks from a specific date
     * @param path String
     * @param date String
     * @return ArrayList<Task>
     */
    public ArrayList<Task> getTaskFromDate(String path, String date) {
        ArrayList<Task> tasks;
        ArrayList<Task> tasksFromDate = new ArrayList<>();
        tasks = sort(path, SortingTypes.SORT_BY_TIME);

        for (int i = 0; i < tasks.size(); i++) {
            if(tasks.get(i).getDate().equals(date)) {
                tasksFromDate.add(tasks.get(i));
            }
        }
        return tasksFromDate;
    }

    /**
     * Adds a task
     * @param path String
     * @param task String
     */
    public void addTask(String path, Task task) {
        try {
            FileWriter fileWriter = new FileWriter(path, true);
            fileWriter.append(task.getTitle()).append("##").append(task.getDescription()).append("##").append(task.getDate()).append("##").append(String.valueOf(task.getImportance())).append("##").append(String.valueOf(task.isProtected())).append("##").append(String.valueOf(task.isDone())).append("\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
