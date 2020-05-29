package com.example.coursehelper.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScheduleObserver extends ViewModel {
    private final MutableLiveData<Schedule> schedule = new MutableLiveData<>();

    public void setSchedule(Schedule schedule) {
        this.schedule.setValue(schedule);
    }

    public LiveData<Schedule> getSchedule() {
        return schedule;
    }
}
