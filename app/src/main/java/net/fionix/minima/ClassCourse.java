package net.fionix.minima;

public class ClassCourse {

    public String code, faculty, group, title;

    public ClassCourse(String faculty, String code, String group, String title) {
        this.faculty = faculty;
        this.code = code;
        this.group = group;
        this.title = title;
    }

    public String getCode() {
        return this.code;
    }
}
