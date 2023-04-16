package GUIClient.controllers;

import GUIClient.models.CourseRegistrationModel;
import GUIClient.views.CourseRegistrationView;
import javafx.scene.control.Alert;
import server.Server;
import server.models.Course;

import java.io.IOException;
import java.util.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class CourseRegistrationController {
    private CourseRegistrationModel model;
    private CourseRegistrationView view;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public CourseRegistrationController(CourseRegistrationModel model, CourseRegistrationView view) {
        this.model = model;
        this.view = view;
        initEventHandlers();
    }

    private void initEventHandlers() {
        view.getLoadCoursesButton().setOnAction(event -> loadCourses());
        view.getSubmitButton().setOnAction(event -> submitRegistration());
    }

    private void loadCourses() {
        String session = view.getSessionComboBox().getValue();
        if (session == null) {
            showError("Veuillez choisir une session.");
            return;
        }

        try {
            socket = new Socket("localhost", 1337);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            objectOutputStream.writeObject(Server.LOAD_COMMAND + " " + session);
            objectOutputStream.flush();

            List<Course> courses = (List<Course>) objectInputStream.readObject();
            model.setCourses(courses);

            view.getCourseTableView().getItems().clear();
            for (Course course : courses) {
                view.getCourseTableView().getItems().add(course);
            }


            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void submitRegistration() {
        //TODO: implémenter la soumission du formulaire d'inscription
        showError("Soumission du formulaire d'inscription");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}