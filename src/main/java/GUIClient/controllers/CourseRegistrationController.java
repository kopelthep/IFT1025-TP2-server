package GUIClient.controllers;

import GUIClient.models.CourseRegistrationModel;
import GUIClient.views.CourseRegistrationView;
import javafx.scene.control.Alert;
import server.Server;
import server.models.Course;

import server.models.RegistrationForm;

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
        String prenom = view.getFirstNameTextField().getText().trim();
        String nom = view.getLastNameTextField().getText().trim();
        String email = view.getEmailTextField().getText().trim();
        String matricule = view.getStudentIdTextField().getText().trim();
        Course selectedCourse = view.getCourseTableView().getSelectionModel().getSelectedItem();

        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty() || matricule.isEmpty() || selectedCourse == null) {
            showError("Veuillez remplir tous les champs et sélectionner un cours.");
            return;
        }

        RegistrationForm registrationForm = new RegistrationForm(prenom, nom, email, matricule, selectedCourse);

        try {
            socket = new Socket("localhost", 1337);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            objectOutputStream.writeObject(Server.REGISTER_COMMAND);
            objectOutputStream.flush();

            objectOutputStream.writeObject(registrationForm);
            objectOutputStream.flush();

            String response = (String) objectInputStream.readObject();
            showConfirmation(response);

            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}