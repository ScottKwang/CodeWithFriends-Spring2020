package com.example.coursehelper.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.coursehelper.Model.Course;
import com.example.coursehelper.Model.DayOfWeek;
import com.example.coursehelper.Model.Hours;
import com.example.coursehelper.Model.Schedule;
import com.example.coursehelper.Model.ScheduleObserver;
import com.example.coursehelper.R;

import java.util.EnumMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    private Schedule schedule;
    private ScheduleObserver scheduleObserver;

    private LinearLayout mondayTimeGraph;
    private LinearLayout tuesdayTimeGraph;
    private LinearLayout wednesdayTimeGraph;
    private LinearLayout thursdayTimeGraph;
    private LinearLayout fridayTimeGraph;

    private RelativeLayout mondayTimeCol;
    private RelativeLayout tuesdayTimeCol;
    private RelativeLayout wednesdayTimeCol;
    private RelativeLayout thursdayTimeCol;
    private RelativeLayout fridayTimeCol;

    private LinearLayout warningSection;

    private TextView tvTotalCredit;

    private View view;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        schedule = new Schedule();
        warningSection = view.findViewById(R.id.warningArea);
        tvTotalCredit = view.findViewById(R.id.creditAccumulation);

        initialButtonSetup();
        initialDayGraphSetup();
        updateUI();
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scheduleObserver = new ViewModelProvider(requireActivity()).get(ScheduleObserver.class);
        scheduleObserver.getSchedule().observe(getViewLifecycleOwner(), new Observer<Schedule>() {
            @Override
            public void onChanged(Schedule schedule) {
                setSchedule(schedule);
                updateUI();
            }
        });
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void initialButtonSetup() {
        Button clearButton = view.findViewById(R.id.clearSchedule);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule = new Schedule();
                updateUI();
                notifyScheduleChangesToObserver();
                System.out.println("Clear pressed");
            }
        });
    }

    public void initialDayGraphSetup() {
        View dayGraph = view.findViewById(R.id.dayGraph);
        mondayTimeGraph = dayGraph.findViewById(R.id.mondayCol);
        tuesdayTimeGraph = dayGraph.findViewById(R.id.tuesdayCol);
        wednesdayTimeGraph = dayGraph.findViewById(R.id.wednesdayCol);
        thursdayTimeGraph = dayGraph.findViewById(R.id.thursdayCol);
        fridayTimeGraph = dayGraph.findViewById(R.id.fridayCol);

        mondayTimeCol = mondayTimeGraph.findViewById(R.id.timeCol);
        tuesdayTimeCol = tuesdayTimeGraph.findViewById(R.id.timeCol);
        wednesdayTimeCol = wednesdayTimeGraph.findViewById(R.id.timeCol);
        thursdayTimeCol = thursdayTimeGraph.findViewById(R.id.timeCol);
        fridayTimeCol = fridayTimeGraph.findViewById(R.id.timeCol);

        TextView mondayHeader = mondayTimeGraph.findViewById(R.id.header_dayOfWeek);
        mondayHeader.setText("Monday");
        TextView tuesdayHeader = tuesdayTimeGraph.findViewById(R.id.header_dayOfWeek);
        tuesdayHeader.setText("Tuesday");
        TextView wednesdayHeader = wednesdayTimeGraph.findViewById(R.id.header_dayOfWeek);
        wednesdayHeader.setText("Wednesday");
        TextView thursdayHeader = thursdayTimeGraph.findViewById(R.id.header_dayOfWeek);
        thursdayHeader.setText("Thursday");
        TextView fridayHeader = fridayTimeGraph.findViewById(R.id.header_dayOfWeek);
        fridayHeader.setText("Friday");
    }

    public void addWarningsInWarningsSection() {
        EnumMap<DayOfWeek, List<String>> map = schedule.getOverlapWarning();
        warningSection.removeAllViewsInLayout();
        for(DayOfWeek day : map.keySet()){
            for(String warning : map.get(day)){
                TextView tvWarning = new TextView(getContext());
                tvWarning.setText(warning);
                tvWarning.setTextColor(Color.RED);
                tvWarning.setTextSize(14);
                tvWarning.setGravity(Gravity.CENTER);
                tvWarning.setBackground(getResources().getDrawable(R.drawable.warning_border));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(20,10,0,0);
                tvWarning.setLayoutParams(params);
                warningSection.addView(tvWarning);
            }
        }
    }

    public void updateUI() {
        if(schedule == null) return;
        if(schedule.isHoursOverlap()){
            System.out.println("*****OVERLAPPED*****");
            addWarningsInWarningsSection();
        }else{
            warningSection.removeAllViewsInLayout();
            warningSection.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        tvTotalCredit.setText("total credit: " + schedule.getTotalCredit());

        // Display all course schedule in timeline graph
        clearSchedule();// There has to be a better/efficient way to draw timeline
        for(Course course : schedule.getCourses()){
            EnumMap<DayOfWeek, List<Hours>> dayOpenHours = course.getHoursOfDay();
            for(DayOfWeek day : dayOpenHours.keySet()){
                List<Hours> hoursList = dayOpenHours.get(day);
                for(Hours hours : hoursList){
                    int[] classTimeInterval = hours.getInIntervalForm();
                    switch(day){
                        case MONDAY:
                            displayClassToGraph(mondayTimeCol, course, classTimeInterval);
                            break;

                        case TUESDAY:
                            displayClassToGraph(tuesdayTimeCol, course, classTimeInterval);
                            break;

                        case WEDNESDAY:
                            displayClassToGraph(wednesdayTimeCol, course, classTimeInterval);
                            break;

                        case THURSDAY:
                            displayClassToGraph(thursdayTimeCol, course, classTimeInterval);
                            break;

                        case FRIDAY:
                            displayClassToGraph(fridayTimeCol, course, classTimeInterval);
                            break;
                    }
                }
            }
        }
    }

    public void displayClassToGraph(RelativeLayout dayTimeCol, Course course, int[] classTimeInterval) {
        //TODO: Animate height changes
        int newClassStart = defaultMinToTimelineMin(classTimeInterval[0]);
        int newClassEnd = defaultMinToTimelineMin(classTimeInterval[1]);
        int newClassStartPx = dpToPx(newClassStart);
        int lengthDp = newClassEnd - newClassStart;
        int lengthPx = dpToPx(lengthDp);

        TextView newClass = new TextView(getContext());
        newClass.setHeight(lengthPx);
        newClass.setText(course.getTitle());
        newClass.setTextSize(12);
        newClass.setTextColor(Color.WHITE);
        newClass.setGravity(Gravity.CENTER);
        newClass.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.course_cell));
        setNewClassClickListener(newClass, course);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dpToPx(60), lengthPx);
        params.topMargin = newClassStartPx;
        dayTimeCol.addView(newClass, params);
    }

    public void setNewClassClickListener(TextView newClass, Course course) {
        newClass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

    public void notifyScheduleChangesToObserver() {
        scheduleObserver.setSchedule(schedule);
    }

    public int defaultMinToTimelineMin(int minutesDefault) {
        int diffInMinutes = 7 * 60;
        return minutesDefault - diffInMinutes;
    }


    public int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public int pxToDp(int px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    public void clearSchedule() {
        mondayTimeCol.removeAllViewsInLayout();
        tuesdayTimeCol.removeAllViews();
        wednesdayTimeCol.removeAllViews();
        thursdayTimeCol.removeAllViews();
        fridayTimeCol.removeAllViews();
    }
}
