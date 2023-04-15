package GUIClient.models;

import server.models.Course;
import server.models.RegistrationForm;

import java.util.List;

public class CourseRegistrationModel {
    private List<Course> courses;
    private Course selectedCourse;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }
}
