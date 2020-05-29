package com.example.coursehelper.View;

import android.content.Context;
import android.graphics.Color;
import android.transition.Scene;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.coursehelper.Model.Course;
import com.example.coursehelper.Model.Schedule;
import com.example.coursehelper.R;

import java.util.HashMap;
import java.util.List;

public class CourseArrayAdapter extends ArrayAdapter<Course> {

    private static Context context;
    private List<Course> courses;
    private Schedule schedule;

    public CourseArrayAdapter(Context context, int resource, List<Course> objects) {
        super(context, resource, objects);

        this.context = context;
        this.courses = objects;
        schedule = new Schedule();
    }

    @Override
    public int getCount() {
        if(courses == null) return 0;
        return courses.size();
    }

    //called when rendering the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the property we are displaying
        Course course = courses.get(position);
        ViewHolder viewHolder;

        // if view is not created yet
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.course_layout_simple, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String raw_title = course.getTitle();
        double raw_credit = course.getCredit();
        String raw_timeContent = course.getTimeContent();

        viewHolder.title.setText(raw_title);
        viewHolder.credit.setText(Double.toString(raw_credit));
        viewHolder.timeContent.setText(raw_timeContent);
        if(course.getIsCancelled()){
            viewHolder.classCancelled.setText("CANCELLED");
        }else{
            viewHolder.classCancelled.setText("");
        }

        return convertView;
    }

    public void updateList(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    class ViewHolder {
        private TextView title;
        private TextView credit;
        private TextView timeContent;
        private TextView classCancelled;
        private View colorCode;

        public ViewHolder(View view) {
            title = view.findViewById(R.id.title);
            credit = view.findViewById(R.id.credit);
            timeContent = view.findViewById(R.id.timeContent);
            classCancelled = view.findViewById(R.id.cancelled);
            colorCode = view.findViewById(R.id.colorCode);
        }
    }
}
