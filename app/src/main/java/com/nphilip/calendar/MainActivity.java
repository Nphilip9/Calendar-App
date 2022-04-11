package com.nphilip.calendar;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nphilip.calendar.fragment.BottomSheetFragment;
import com.nphilip.calendar.listView.ListViewAdapter;
import com.nphilip.calendar.listView.Task;
import com.nphilip.calendar.listeners.SwipeListViewTouchListener;
import com.nphilip.calendar.manager.TaskManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Defining View instances for the main activity content view
    /*
     * CalendarView is used to select the date you want and display all tasks from this date
     * Default date is always the date of today
     * CalendarView is initialized in the main function (onCreate)
     */
    com.prolificinteractive.materialcalendarview.MaterialCalendarView mainActivity_calendarView_calendar;

    /*
     * FloatingActionButton is used to start the add activity on click
     * Position of FloatingActionButton is bottom right corner
     * FloatingActionButton is initialized in the main function (onCreate)
     */
    FloatingActionButton mainActivity_fab_addTask;

    /*
     * ListView is used to display the tasks of the selected date (selected date from CalendarView)
     * No limit for the size of the listView
     * ListView is initialized in the main function (onCreate)
     * ListAdapter is set in the method "initListView()"
     */
    ListView mainActivity_listView_tasks;

    /*
    * Not implemented yet
    */
    SearchView mainActivity_searchView_searchTask;

    /*
     * ArrayList contains all tasks (from Object "Task") from the selected date (selected date from CalendarView)
     * No limit set on the ArrayList
     * This ArrayList is initialized in the main function (onCreate)
     */
    ArrayList<Task> tasks = new ArrayList<>();

    /*
     * Instance to save selected sortingType and clickedItemID from the ListView
     * Data returned by calling Context.getSharedPreferences(String sharedPreferencesName, int defaultValue), default value is needed on an error
     * Objects that are returned must be treated as immutable(unmodifiable)
     * For setting or modifying the saved values call SharedPreferences.Editor
     * SharedPreferences initialized in the main function (onCreate)
     */
    SharedPreferences sharedPreferences;

    /*
     * Variable for the CalendarView, it saves the selected date. Needed for getting the tasks for the selected date
     * Default value is the current date of the day
     */
    private String selectedDate;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint({"ResourceType", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Initializing the View instances
         * Associate the id from the design layout to the View instances
         * If an item is not assigned or assigned with null it throws a NullPointerException and the app crashes
         */
        mainActivity_calendarView_calendar = findViewById(R.id.mainActivity_calendarView_calendar);
        mainActivity_fab_addTask = findViewById(R.id.mainActivity_fab_addTask);
        mainActivity_listView_tasks = findViewById(R.id.mainActivity_listView_tasks);
        mainActivity_searchView_searchTask = findViewById(R.id.mainActivity_searchView_searchTask);

        mainActivity_calendarView_calendar.setSelectedDate(CalendarDay.today());
        mainActivity_calendarView_calendar.setAllowClickDaysOutsideCurrentMonth(false);

        /*
         * All months from 0 - 9 are written without a zero (January: 1, February: 2, ...) but the code expects it with zero (January: 01, February: 02, ...)
         * With the ? operator we can check if the month is less then 10, if yes we assign a zero to the month, if no we don't assign a zero to the month
         */
        selectedDate = Integer.parseInt(String.valueOf(mainActivity_calendarView_calendar.getCurrentDate().getMonth())) < 10
                ? mainActivity_calendarView_calendar.getCurrentDate().getDay() + "." + "0" + mainActivity_calendarView_calendar.getCurrentDate().getMonth() + "." + mainActivity_calendarView_calendar.getCurrentDate().getYear()
                : mainActivity_calendarView_calendar.getCurrentDate().getDay() + "." + mainActivity_calendarView_calendar.getCurrentDate().getMonth() + "." + mainActivity_calendarView_calendar.getCurrentDate().getYear();

        calendarViewDecorator();

        /*
         * Setting darkMode as default design
         * Light mode / dark mode depends on the system settings
         * If the user has selected dark mode as the default theme obviously the app is also in dark mode, same thing for the light mode
         * Here we are setting the default design to dark mode
         * If we want to select the light mode as the default design then we use AppCompatDelegate.MODE_NIGHT_NO
         */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        /*
         * Initializing the sharedPreferences
         * For each initialization with a different name, another file is created with the given name as it's file name
         * MODE_PRIVATE = 0 (0x00000000 <-- Integer value): The default mode, where the created file can only be accessed by the calling application (or all applications sharing the same user ID)
         * MODE_PRIVATE: Operating mode. Value is either 0 or a combination of MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITEABLE, and MODE_MULTI_PROCESS
         * Sorting Type value range: {0, 1, 2}
         * Clicked item ID value range: [0; ꝏ[
         */
        sharedPreferences = getSharedPreferences("SortingType", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("ClickedItem", MODE_PRIVATE);

        /*
         *
         */

        mainActivity_calendarView_calendar.setOnDateChangedListener((widget, date, selected) -> {
            selectedDate = Integer.parseInt(String.valueOf(date.getMonth())) < 10
                    ? date.getDay() + "." + "0" + date.getMonth() + "." + date.getYear()
                    : date.getDay() + "." + date.getMonth() + "." + date.getYear();
            initListView();
        });

        mainActivity_fab_addTask.setOnClickListener(v -> {

        });

        mainActivity_listView_tasks.setOnItemClickListener((adapterView, view, i, l) -> {
            if(tasks.get(i).isProtected()) {
                final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(MainActivity.this)
                        .setTitle("Authentifizieren zum ansehen")
                        .setSubtitle("Biometrische Authentifiezerung")
                        .setNegativeButton("Abbrechen", getMainExecutor(), (dialogInterface, i1) ->  { /* Do nothing */ }).build();

                biometricPrompt.authenticate(new CancellationSignal(), getMainExecutor(), new BiometricPrompt.AuthenticationCallback() {
                    /**
                     * Called when an unrecoverable error has been encountered and authentication has stopped
                     * After this function is called, no further events will be sent for the current authentication session
                     * @param errorCode Integer
                     * @param errString CharSequence
                     */
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        Toast.makeText(MainActivity.this, errString, Toast.LENGTH_SHORT).show();
                        super.onAuthenticationError(errorCode, errString);
                    }

                    /**
                     * Called when a biometric (e.g. fingerprint, face, etc.) is recognized, indicating that the user has successfully authenticated
                     * After this function is called, no further events will be sent for the current authentication session
                     * @param result BiometricPrompt.AuthenticationResult
                     */
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        Toast.makeText(MainActivity.this, "Auth", Toast.LENGTH_SHORT).show();
                        showBottomSheetDialog(i);
                        super.onAuthenticationSucceeded(result);
                    }

                    /**
                     * Called when a biometric (e.g. fingerprint, face, etc.) is presented but not recognized as belonging to the user
                     */
                    @Override
                    public void onAuthenticationFailed() {
                        Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        super.onAuthenticationFailed();
                    }
                });
            } else {
                showBottomSheetDialog(i);
            }
            saveClickedItemID(i);
        });

        mainActivity_searchView_searchTask.setOnClickListener(view -> mainActivity_searchView_searchTask.setIconified(false));

        mainActivity_searchView_searchTask.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println(s);
                return false;
            }
        });

        SwipeListViewTouchListener swipeListViewTouchListener = new SwipeListViewTouchListener(mainActivity_listView_tasks, new SwipeListViewTouchListener.OnSwipeCallback() {
            @Override
            public void onSwipeLeft(ListView listView, int[] reverseSortedPositions) {
                new TaskManager().deleteTask(getFilesDir() + "/Tasks", tasks.get(reverseSortedPositions[0]).getDate(), reverseSortedPositions[0]);
                initListView();
                System.out.println(Arrays.toString(reverseSortedPositions));
            }

            @Override
            public void onSwipeRight(ListView listView, int[] reverseSortedPositions) {
                new TaskManager().deleteTask(getFilesDir() + "/Tasks", tasks.get(reverseSortedPositions[0]).getDate(), reverseSortedPositions[0]);
                initListView();
                System.out.println(Arrays.toString(reverseSortedPositions));
            }
        }, true, true);

        mainActivity_listView_tasks.setOnTouchListener(swipeListViewTouchListener);
        mainActivity_listView_tasks.setOnScrollListener(swipeListViewTouchListener.makeScrollListener());

        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title", "description", "27.03.2022", 1, true, false));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title2", "description", "27.03.2022", 1, false, true));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title", "description", "27.03.2022", 1, true, false));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title2", "description", "27.03.2022", 1, false, true));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title", "description", "27.03.2022", 1, true, false));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title2", "description", "27.03.2022", 1, false, true));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title", "description", "27.03.2022", 1, true, false));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title2", "description", "27.03.2022", 1, false, true));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title", "description", "27.03.2022", 1, true, false));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title2", "description", "27.03.2022", 1, false, true));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title2", "description", "27.03.2022", 1, false, true));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title", "description", "27.03.2022", 1, true, false));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title2", "description", "27.03.2022", 1, false, true));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title", "description", "27.03.2022", 1, true, false));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title2", "description", "27.03.2022", 1, false, true));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title", "description", "27.03.2022", 1, true, false));
        new TaskManager().addTask(getFilesDir() + "/Tasks", new Task("Title2", "description", "27.03.2022", 1, false, true));

        initListView();
    }

    /**
     * Showing bottom sheet dialog with the information of the clicked item
     * @param itemID Integer
     */
    private void showBottomSheetDialog(int itemID) {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(tasks.get(itemID));
        bottomSheetFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog");
    }

    /**
     * Draw the menu
     * @param menu Menu
     * @return Boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sorting_type_selection_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Function called on menu item click
     * @param item MenuItem
     * @return Boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sortingTypeSelectionMenu_item_sortByName) {
            saveSortingType(0);
        } else if (item.getItemId() == R.id.sortingTypeSelectionMenu_item_sortByImportance) {
            saveSortingType(1);
        } else if(item.getItemId() == R.id.sortingTypeSelectionMenu_item_sortByTime) {
            saveSortingType(2);
        } else if(item.getItemId() == R.id.sortingTypeSelectionMenu_item_settings)  {
            // TODO: Implement
        }
        return true;
    }

    //Initializes the ListView
    private void initListView() {
        tasks = new TaskManager().getTasksFromDate(getFilesDir() + "/Tasks", selectedDate);
        System.out.println(tasks);
        ListViewAdapter listViewAdapter = new ListViewAdapter(MainActivity.this, tasks);
        mainActivity_listView_tasks.setAdapter(listViewAdapter);
    }

    private void calendarViewDecorator() {
        Handler handler = new Handler();
        handler.postDelayed(() -> runOnUiThread(() -> mainActivity_calendarView_calendar.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                ArrayList<Task> allTasks = new TaskManager().getAllTasks(getFilesDir().toString());
                for (Task task : allTasks) {
                    if(Integer.parseInt(task.getDate().split("\\.")[0]) == day.getDay() && Integer.parseInt(task.getDate().split("\\.")[1]) == day.getMonth() && Integer.parseInt(task.getDate().split("\\.")[2]) == day.getYear()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new DotSpan(10, getColor(R.color.purple_700)));
            }
        })), 1000);
    }

    /**
     * Saves the selected sortingType
     * @param sortingType Integer
     */
    public void saveSortingType(int sortingType) {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putInt("sortingType", sortingType);
        sharedPreferencesEditor.apply();
    }

    /**
     * saves the clicked item ID
     * @param itemID Integer
     */
    public void saveClickedItemID(int itemID) {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putInt("itemID", itemID);
        sharedPreferencesEditor.apply();
    }
}