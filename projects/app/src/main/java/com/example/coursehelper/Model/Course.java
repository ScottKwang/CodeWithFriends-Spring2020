package com.example.coursehelper.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class Course implements Serializable {

    private String courseCRN;
    private String subject;
    private String title;
    private boolean isLabCourse;
    private String courseDescription;
    private String faculty;
    private String room;
    private double credit;
    private List<String> cores;
    private EnumMap<DayOfWeek, List<Hours>> hoursOfDay; // use this to detect the overlap
    private String timeContent;
    private boolean isCancelled;

    public Course() {
        cores = new ArrayList();
        hoursOfDay = new EnumMap<DayOfWeek, List<Hours>>(DayOfWeek.class);
    }

    public Course(String courseCRN, String title, String faculty, String room, double credit, List<String> cores, EnumMap<DayOfWeek,List<Hours>> hoursOfDay, String timeContent) {
        this.courseCRN = courseCRN;
        this.title = title;
        this.faculty = faculty;
        this.room = room;
        this.credit = credit;
        this.cores = cores;
        this.hoursOfDay = hoursOfDay;
        this.timeContent = timeContent;
    }

    public Course(String courseCRN, String title, String faculty, String room, double credit, String timeContent) {
        this.courseCRN = courseCRN;
        this.title = title;
        this.faculty = faculty;
        this.room = room;
        this.credit = credit;
        this.cores = new ArrayList<String>();
        this.hoursOfDay = new EnumMap<DayOfWeek, List<Hours>>(DayOfWeek.class);
        this.timeContent = timeContent;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean getIsLabCourse() {
        return isLabCourse;
    }

    public void serIsLabCourse(boolean isLabCourse) {
        this.isLabCourse = isLabCourse;
    }

    public String getCourseCRN() {
        return courseCRN;
    }

    public void setCourseCRN(String courseCRN) {
        this.courseCRN = courseCRN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public List<String> getCores() {
        return cores;
    }

    public void addCore(String core) {
        cores.add(core);
    }

    public EnumMap<DayOfWeek,List<Hours>> getHoursOfDay() {
        return hoursOfDay;
    }

    public List<Hours> getHoursFromDay(DayOfWeek day) {
        return hoursOfDay.get(day);
    }

    public boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public void putHoursOfDay(DayOfWeek dayOfWeek, List<Hours> hours) {
        hoursOfDay.put(dayOfWeek, hours);
    }

    public void setHoursOfDay(EnumMap<DayOfWeek, List<Hours>> hoursOfDay) {
        this.hoursOfDay = hoursOfDay;
    }

    public String getTimeContent() {
        return timeContent;
    }

    public void setTimeContent(String timeContent) {
        this.timeContent = timeContent;
    }

    public String getCoresAsString() {
        String res = "";
        for(String core : cores){
            res += core + "/";
        }
        return res.substring(0,res.length()-1);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseCRN='" + courseCRN + '\'' +
                ", subject='" + subject + '\'' +
                ", isLabCourse=" + isLabCourse +
                ", courseDescription='" + courseDescription + '\'' +
                ", title='" + title + '\'' +
                ", faculty='" + faculty + '\'' +
                ", room='" + room + '\'' +
                ", credit=" + credit +
                ", cores=" + cores +
                ", hoursOfDay=" + hoursOfDay +
                ", timeContent='" + timeContent + '\'' +
                ", isCancelled=" + isCancelled +
                "}";
    }

    public String toPrettyFormatString() {
        return "Title: " + title + "\n" +
                "CRN info: " + courseCRN + "\n" +
                "Time: " + timeContent + "\n";
    }
}

