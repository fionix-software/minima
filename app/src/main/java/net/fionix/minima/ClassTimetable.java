package net.fionix.minima;

/**
 * Created by nazebzurati on 15/09/2017.
 */

public class ClassTimetable {
    public String course, group, start, end, location, day, dayCode;

    public ClassTimetable(String course, String group, String start, String end, String day, String dayCode, String location) {
        this.course = course;
        this.group = group;
        this.start = start;
        this.end = end;
        this.day = day;
        this.dayCode = dayCode;
        this.location = location;
    }

    public String getDayCode() {
        return dayCode;
    }
}
