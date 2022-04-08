package com.nphilip.calendar;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricPrompt;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
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
import com.nphilip.calendar.manager.TaskManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Defining View instances for the main activity content view
    /*
     * CalendarView is used to select the date you want and display all tasks from this date
     * Default date is always the date of today
     * CalendarView is initialized in the main function (onCreate)
     */
    CalendarView mainActivity_calendarView_calendar;

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

    /*
     * Constant value(unmodifiable), it defines the date format
     * Format: dd/MM/yyyy --> day/month/year ==> example: 28/03/2022
     */
    private final static String DATE_FORMAT = "dd/MM/yyyy";

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("ResourceType")
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

        /*
         * Creating the date formatter and passing the parameters String dateFormat and the time zone
         * SimpleDateFormat is needed to format the format the getDate() -> return long method to a user friendly string
         * Passing the formatted date to the selectedDate variable
         */
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ITALY);
        selectedDate = simpleDateFormat.format(new Date(mainActivity_calendarView_calendar.getDate()));

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
        mainActivity_calendarView_calendar.setOnDateChangeListener((calendarView, year, month, day) -> {
            if(month < 10) {
                selectedDate = day + "/" + "0" + (month + 1) + "/" + year;
            } else {
                selectedDate = day + "/" + (month + 1) + "/" + year;
            }
            initListView();

        });

        mainActivity_fab_addTask.setOnClickListener(v -> {
            showBottomSheetDialog(1);
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
                System.out.println(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println(s);
                return false;
            }
        });

        new TaskManager().addTask(getFilesDir() + "/Tasks.txt", new Task("Title", "description", "27/03/2022", 1, true, false));
        new TaskManager().addTask(getFilesDir() + "/Tasks.txt", new Task("Title1", "description", "8/02/2020", 1, false, true));
        new TaskManager().addTask(getFilesDir() + "/Tasks.txt", new Task("Title2", "description", "27/03/2022", 1, false, true));

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
        tasks = new TaskManager().getTaskFromDate(getFilesDir() + "/Tasks.txt", selectedDate);
        System.out.println(tasks);
        ListViewAdapter listViewAdapter = new ListViewAdapter(MainActivity.this, tasks);
        mainActivity_listView_tasks.setAdapter(listViewAdapter);
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