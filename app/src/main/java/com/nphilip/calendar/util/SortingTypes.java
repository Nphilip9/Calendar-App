package com.nphilip.calendar.util;

public enum SortingTypes {
    /*
     * Enum of sorting types, all constant but no initialized with any value
     * Sorting can be selected on the main activity in the options menu
     *
     * SORT_MY_NAME sorts the tasks by the ABC beginning with A-Z
     * Sorting Algorithm is defined in the /manager/SortingManager.java class, function: sortByName()
     */
    SORT_BY_NAME,

    /*
     * SORT_BY_IMPORTANCE sorts the tasks by the importance which goes from 1 -10
     * Sorting Algorithm is defined in the /manager/SortingManager.java class, function: sortByImportance()
     * Not recommended on a large list of tasks because there are too many same values
     */
    SORT_BY_IMPORTANCE,

    /*
     * SORT_MY_NAME sorts the task by the time (milliseconds are not considered)
     * Format of the time is: hh:mm:ss the format is not going to change
     * It's also the default sorting algorithm
     */
    SORT_BY_TIME,

    SORT_BY_DATE,

    SORT_BY_ID
}
