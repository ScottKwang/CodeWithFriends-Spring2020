package com.example.coursehelper.View;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;

import com.example.coursehelper.Model.Course;
import com.example.coursehelper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseDescriptionDialogFragment extends DialogFragment {

    private CoursesFragment targetFragment;
    private Course course;

    public void setCourse(Course course) {
        if(null == course) return;
        this.course = course;
    }

    public static CourseDescriptionDialogFragment newInstance(Bundle bundle) {
        CourseDescriptionDialogFragment f = new CourseDescriptionDialogFragment();
        f.setArguments(bundle);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_course_description, null);
        
        inflateDialog(view);
        setClickListener(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        targetFragment = (CoursesFragment) getTargetFragment();
    }

    public void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button addButton = view.findViewById(R.id.addButton);
        Button dropButton = view.findViewById(R.id.dropButton);

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                targetFragment.addCourseToSchedule(course);
                dismiss();
            }
        });

        dropButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                targetFragment.removeCourseFromSchedule(course);
                dismiss();
            }
        });
    }

    public void inflateDialog(View view) {
        TextView title = view.findViewById(R.id.courseTitle);
        TextView crn = view.findViewById(R.id.courseCRN);
        TextView faculty = view.findViewById(R.id.courseFaculty);
        TextView room = view.findViewById(R.id.courseRoom);
        TextView timeContent = view.findViewById(R.id.courseTimeContent);
        TextView credit = view.findViewById(R.id.courseCredit);
        TextView description = view.findViewById(R.id.courseDescription);
        TextView cores = view.findViewById(R.id.courseCores);

        title.setText(course.getTitle());
        crn.setText(course.getCourseCRN());
        faculty.setText(course.getFaculty());
        room.setText(course.getRoom());
        timeContent.setText(course.getTimeContent());
        credit.setText(Double.toString(course.getCredit()));
        if(course.getCourseDescription() == null || course.getCourseDescription().length() == 0){
            description.setText("No description available.");
        }else{
            description.setText(course.getCourseDescription());
        }
        description.setMovementMethod(new ScrollingMovementMethod());
        cores.setText(course.getCoresAsString());
    }
}
