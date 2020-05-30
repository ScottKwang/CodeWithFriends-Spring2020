package com.example.coursehelper.Model;

import java.io.Serializable;

public enum DayOfWeek implements Serializable {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private final int dayCode;

    private DayOfWeek(int dayCode) {
        this.dayCode = dayCode;
    }

    public int getDayCode() {
        return dayCode;
    }
}
