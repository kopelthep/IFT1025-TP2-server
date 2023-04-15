package guiclient.models;

import
import server.models.RegistrationForm;

import java.util.List;

public class CourseRegistrationModel {
    private List<Course> availableCourses;
    private RegistrationForm registrationForm;

    public List<Course> getAvailableCourses() {
        return availableCourses;
    }

    public void setAvailableCourses(List<Course> availableCourses) {
        this.availableCourses = availableCourses;
    }

    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }
}
