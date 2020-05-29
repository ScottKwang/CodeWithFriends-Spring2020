package com.example.coursehelper.Model;

import com.fasterxml.jackson.core.JsonpCharacterEscapes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schedule implements Serializable {

    public static final String PREFS_KEY = "com.example.coursehelper.Model.Schedule.java.SharedPreferences";
    List<Course> courses;
    double totalCredit;
    // if overlapped class scheduled, put the warning message in this map
    private EnumMap<DayOfWeek, List<String>> overlapWarning = new EnumMap<>(DayOfWeek.class);

    public Schedule() {
        this.courses = new ArrayList<Course>();
        totalCredit = 0;
    }

    public EnumMap<DayOfWeek, List<String>> getOverlapWarning() {
        return overlapWarning;
    }

    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Add course that is from param, then check if the course cause schedule conflict
     *
     * @param course
     * @return True, if added courses cause time overlap conflict with other courses
     */
    public boolean addCourse(Course course) {
        courses.add(course);
        totalCredit += course.getCredit();
        return isHoursOverlap();
    }

    public double getTotalCredit() {
        return totalCredit;
    }

    /**
     * Remove the course that matches the param course
     *
     * @return True, if successfully found course to be removed False, otherwise
     */
    public boolean removeCourse(Course course) {
        totalCredit -= course.getCredit();
        return courses.remove(course);
    }

    /**
     * Remove the courses based on index
     *
     * @param idx
     * @return Course
     */
    public Course removeCourse(int idx) {
        if(idx < 0 || idx >= courses.size()) return null;
        Course removedCourse = courses.remove(idx);
        totalCredit -= removedCourse.getCredit();
        return removedCourse;
    }

    /**
     * Check if the courses have any overlapped hours
     *
     * @return True, if some course hours is overlapped
     */
    public boolean isHoursOverlap() {
        boolean anyOverlap = false;
        if (courses.size() <= 1) return anyOverlap;

        EnumMap<DayOfWeek, List<Course>> map = new EnumMap(DayOfWeek.class);
        for(Course course : courses){
            for(DayOfWeek day : course.getHoursOfDay().keySet()){
                if(map.get(day) == null){
                    List<Course> courses = new ArrayList<Course>();
                    courses.add(course);
                    map.put(day, courses);
                }else{
                    map.get(day).add(course);
                }
            }
        }

        for(DayOfWeek day : map.keySet()){
            Comparator<Course> com = new Comparator<Course>(){
                public int compare(Course o1, Course o2) {
                    return o1.getHoursOfDay().get(day).get(0).getInIntervalForm()[0] - o2.getHoursOfDay().get(day).get(0).getInIntervalForm()[0];
                }
            };
            List<Course> courses = map.get(day);
            Collections.sort(courses, com);
        }

        clearWarnings();

        for(DayOfWeek day : map.keySet()){
            List<Course> courses = map.get(day);
            int[] prevClassTime = courses.get(0).getHoursOfDay().get(day).get(0).getInIntervalForm();
            for(int i=1; i<courses.size(); i++){
                int[] currClassTime = courses.get(i).getHoursOfDay().get(day).get(0).getInIntervalForm();
                if(prevClassTime[1] > currClassTime[0]){
                    // overlapped
                    anyOverlap = true;
                    if(overlapWarning.get(day) == null){
                        List<String> warnings = new ArrayList();
                        warnings.add("Time conflict on " + day +
                                ":\n class1 @"
                                + minToMilitaryTime(prevClassTime[0]) + "-" + minToMilitaryTime(prevClassTime[1]) +
                                ", class2 @"
                                + minToMilitaryTime(currClassTime[0]) + "-" + minToMilitaryTime(currClassTime[1]));
                        overlapWarning.put(day, warnings);
                    }else{
                        overlapWarning.get(day).add("Time conflict on " + day +
                                ":\n class1 @"
                                + minToMilitaryTime(prevClassTime[0]) + "-" + minToMilitaryTime(prevClassTime[1]) +
                                ", class2 @"
                                + minToMilitaryTime(currClassTime[0]) + "-" + minToMilitaryTime(currClassTime[1]));
                    }
                }
                prevClassTime = currClassTime;
            }
        }

        return anyOverlap;
    }

    private void clearWarnings() {
        for(DayOfWeek day : overlapWarning.keySet()){
            overlapWarning.put(day, new ArrayList<String>());
        }
    }

    private String minToMilitaryTime(int min) {
        String h = Integer.toString(min / 60);
        String m = Integer.toString(min % 60);
        if(h.length() == 1) h = "0" + h;
        if(m.length() == 1) m = "0" + m;
        return h + ":" + m;
    }
}