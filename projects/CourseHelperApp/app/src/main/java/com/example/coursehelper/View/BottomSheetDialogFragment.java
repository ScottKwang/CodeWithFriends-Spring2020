package com.example.coursehelper.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coursehelper.Model.Course;
import com.example.coursehelper.Model.Schedule;
import com.example.coursehelper.Model.ScheduleObserver;
import com.example.coursehelper.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheetDialogFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {

    private View view;
    private ListView listView;
    private CourseArrayAdapter adapter;

    private ScheduleObserver scheduleObserver;
    private Schedule schedule;

    public BottomSheetDialogFragment() {
        // Required empty public constructor
    }

    public static BottomSheetDialogFragment newInstance() {
        return new BottomSheetDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false);

        schedule = new Schedule();
        setupListView();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scheduleObserver = new ViewModelProvider(requireActivity()).get(ScheduleObserver.class);
        scheduleObserver.getSchedule().observe(requireActivity(), new Observer<Schedule>() {
            @Override
            public void onChanged(Schedule schedule) {
                System.out.println("BottomSheetDialogFragment's adapter have " + schedule.getCourses().size() + " many courses on changed");
                updateSchedule(schedule);
            }
        });
        notifyScheduleChangesToObserver();
    }

    public void updateSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void notifyScheduleChangesToObserver() {
        scheduleObserver.setSchedule(schedule);
    }

    private void setupListView() {
        listView = view.findViewById(R.id.bottomSheetListView);
        adapter = new CourseArrayAdapter(getActivity(), 0, schedule.getCourses());
        listView.setAdapter(adapter);

        System.out.println("BottomSheetDialogFragment's adapter have " + adapter.getCount() + " many courses");

        Fragment f = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = schedule.getCourses().get(position);
                //TODO: show dialog
                new AlertDialog.Builder(getContext())
                        .setTitle("Remove Entry")
                        .setMessage(course.toPrettyFormatString())
                        .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                schedule.removeCourse(course);
                                updateUI();
                                notifyScheduleChangesToObserver();
                            }
                        })
                        .show();
            }
        });
    }

    private void updateUI() {
        adapter.updateList(schedule.getCourses());
        listView.setAdapter(adapter);
    }

}
