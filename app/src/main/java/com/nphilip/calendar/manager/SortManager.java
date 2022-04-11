package com.nphilip.calendar.manager;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import com.nphilip.calendar.listView.Task;
import com.nphilip.calendar.util.SortingTypes;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class SortManager {

    public String[][] startSorting(ArrayList<Task> unsortedTasks, SortingTypes sortingType) {
        String[][] unsortedTasksPrimitiveList = new String[unsortedTasks.size()][7];
        for (int i = 0; i < unsortedTasks.size(); i++) {
            unsortedTasksPrimitiveList[i][0] = unsortedTasks.get(i).getTitle();
            unsortedTasksPrimitiveList[i][1] = unsortedTasks.get(i).getDescription();
            unsortedTasksPrimitiveList[i][2] = unsortedTasks.get(i).getDate();
            unsortedTasksPrimitiveList[i][3] = String.valueOf(unsortedTasks.get(i).getImportance());
            unsortedTasksPrimitiveList[i][4] = String.valueOf(unsortedTasks.get(i).isProtected());
            unsortedTasksPrimitiveList[i][5] = String.valueOf(unsortedTasks.get(i).isDone());
        }

        switch (sortingType) {
            case SORT_BY_NAME:
                return sortByName(unsortedTasksPrimitiveList);
            case SORT_BY_IMPORTANCE:
                return sortByImportance(unsortedTasksPrimitiveList);
            case SORT_BY_DATE:
                return sortByDate(unsortedTasksPrimitiveList);
            case SORT_BY_TIME:
                return sortByTime(unsortedTasksPrimitiveList);
            case SORT_BY_ID:
                return sortByID(unsortedTasksPrimitiveList);
        }
        return unsortedTasksPrimitiveList;
    }

    /**
     * Sort by name
     * @param unsortedTasks String[][]
     * @return String[][]
     */
    private String[][] sortByName(String[][] unsortedTasks) {
        Arrays.sort(unsortedTasks, (first, second) -> second[0].compareTo(first[0]));
        Collections.reverse(Arrays.asList(unsortedTasks));
        return unsortedTasks;
    }

    /**
     * Sort by importance
     * @param unsortedTasks String[][]
     * @return String[][]
     */
    private String[][] sortByImportance(String[][] unsortedTasks) {
        Arrays.sort(unsortedTasks, (first, second) -> second[3].compareTo(first[3]));
        Collections.reverse(Arrays.asList(unsortedTasks));
        return unsortedTasks;
    }

    /**
     * Sort by Time
     * @param unsortedTasks String[][]
     * @return String[][]
     */
    private String[][] sortByDate(String[][] unsortedTasks) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            for (int i = 0; i < unsortedTasks.length; i++) {
                for (int j = 0; j < unsortedTasks.length - 1; j++) {
                    Date date1 = sdf.parse(unsortedTasks[j][2]);
                    Date date2 = sdf.parse(unsortedTasks[j + 1][2]);
                    if (Objects.requireNonNull(date1).compareTo(date2) > 0) {
                        String[] temp = unsortedTasks[j];
                        unsortedTasks[j] = unsortedTasks[j + 1];
                        unsortedTasks[j + 1] = temp;
                    }
                }
            }
            Collections.reverse(Arrays.asList(unsortedTasks));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unsortedTasks;
    }

    private String[][] sortByTime(String[][] unsortedTasksPrimitiveList) {
        return null;
    }

    private String[][] sortByID(String[][] unsortedTasks) {
        Arrays.sort(unsortedTasks, (first, second) -> second[6].compareTo(first[6]));
        Collections.reverse(Arrays.asList(unsortedTasks));
        return unsortedTasks;
    }
}
