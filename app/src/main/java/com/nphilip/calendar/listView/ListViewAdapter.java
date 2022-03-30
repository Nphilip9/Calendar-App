package com.nphilip.calendar.listView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nphilip.calendar.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter  {

    Context context;
    ArrayList<Task> tasks;
    TextView calendarItem_textView_listViewItem;

    /**
     * Main constructor
     * @param context Context
     * @param tasks ArrayList<Task>
     */
    public ListViewAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    /**
     * Returns the count of the Tasks
     * @return Integer
     */
    @Override
    public int getCount() {
        return tasks.size();
    }

    /**
     * Returns the current item
     * @param i Integer
     * @return Object
     */
    @Override
    public Object getItem(int i) {
        return i;
    }

    /**
     * Returns the current item ID
     * @param i Integer
     * @return Long
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Returns the view
     * @param i Integer
     * @param view View
     * @param viewGroup ViewGroup
     * @return View
     */
    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.calendar_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Task task = tasks.get(i);
        String[] dateSplit = task.getDate().split("/");

        viewHolder.calendarItem_textView_itemContent.setText(task.getTitle());

        switch (dateSplit[1]) {
            case "01":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Jan.");
                break;
            case "02":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Feb.");
                break;
            case "03":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Mar.");
                break;
            case "04":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Apr.");
                break;
            case "05":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Mai.");
                break;
            case "06":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Jun.");
                break;
            case "07":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Jul.");
                break;
            case "08":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Aug.");
                break;
            case "09":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Sep.");
                break;
            case "10":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Okt.");
                break;
            case "11":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Nov.");
                break;
            case "12":
                viewHolder.calendarItem_textView_date.setText(dateSplit[0] + "\n\n" + "Dez.");
                break;
        }

        if(task.isProtected) {
            viewHolder.calendarItem_textView_itemContent.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_lock_24, 0);
            viewHolder.calendarItem_textView_itemContent.setText("Anklicken zum anschauen");
        } else {
            if(task.isDone) {
                viewHolder.calendarItem_textView_itemContent.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_done_24, 0);
            } else {
                viewHolder.calendarItem_textView_itemContent.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_access_time_24, 0);
            }
        }
        viewHolder.materialDivider.setBackgroundColor(Color.parseColor("#E60001"));
        return view;
    }

    public static class ViewHolder {
        public final View materialDivider;
        public final TextView calendarItem_textView_date;
        public final TextView calendarItem_textView_itemContent;
        public final RelativeLayout calendarItem_relativeLayout_relativeLayout;

        public ViewHolder(View view) {
            this.materialDivider = view.findViewById(R.id.divider2);
            this.calendarItem_textView_date = view.findViewById(R.id.calendarItem_textView_date);
            this.calendarItem_textView_itemContent = view.findViewById(R.id.calendarItem_textView_itemContent);
            this.calendarItem_relativeLayout_relativeLayout = view.findViewById(R.id.calendarItem_relativeLayout_relativeLayout);
        }
    }
}
