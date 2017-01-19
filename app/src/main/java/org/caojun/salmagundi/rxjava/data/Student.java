package org.caojun.salmagundi.rxjava.data;

import java.util.List;

/**
 * 学生
 * Created by CaoJun on 2017/1/19.
 */

public class Student {
    private String id;
    private String name;
    private List<Course> courses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
