package com.nphilip.calendar.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nphilip.calendar.R;
import com.nphilip.calendar.listView.Task;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    Task clickedTask;

    public BottomSheetFragment(Task clickedTask) {
        this.clickedTask = clickedTask;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_dialog_layout, null);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        ViewHolder viewHolder = new ViewHolder(contentView);
        viewHolder.bottomSheetDialogLayout_textView_title.setText(clickedTask.getTitle());
        viewHolder.bottomSheetDialogLayout_textView_description.setText(clickedTask.getDescription());
        viewHolder.bottomSheetDialogLayout_textView_date.setText(clickedTask.getDate());
        viewHolder.bottomSheetDialogLayout_textView_importance.setText(String.valueOf(clickedTask.getImportance()));
        viewHolder.bottomSheetDialogLayout_textView_isDone.setText(clickedTask.isDone() ? "Done" : "Not done");

        viewHolder.bottomSheetDialogLayout_button_edit.setOnClickListener(view -> Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show());
    }

    public static class ViewHolder {

        TextView bottomSheetDialogLayout_textView_title, bottomSheetDialogLayout_textView_description,
        bottomSheetDialogLayout_textView_date, bottomSheetDialogLayout_textView_importance,
        bottomSheetDialogLayout_textView_isDone;

        Button bottomSheetDialogLayout_button_edit;

        public ViewHolder(View view) {
            this.bottomSheetDialogLayout_textView_title = view.findViewById(R.id.bottomSheetDialogLayout_textView_title);
            this.bottomSheetDialogLayout_textView_description = view.findViewById(R.id.bottomSheetDialogLayout_textView_description);
            this.bottomSheetDialogLayout_textView_date = view.findViewById(R.id.bottomSheetDialogLayout_textView_date);
            this.bottomSheetDialogLayout_textView_importance = view.findViewById(R.id.bottomSheetDialogLayout_textView_importance);
            this.bottomSheetDialogLayout_textView_isDone = view.findViewById(R.id.bottomSheetDialogLayout_textView_isDone);
            this.bottomSheetDialogLayout_button_edit = view.findViewById(R.id.bottomSheetDialogLayout_button_edit);
        }
    }
}
