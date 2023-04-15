package GUIClient.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CourseRegistrationView {
    private BorderPane root;
    private ComboBox<String> sessionComboBox;
    private Button loadCoursesButton;
    private ListView<String> coursesListView;
    private TextField courseCodeTextField;
    private TextField firstNameTextField;
    private TextField lastNameTextField;
    private TextField emailTextField;
    private TextField studentIdTextField;
    private Button submitButton;

    public CourseRegistrationView() {
        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        root = new BorderPane();
        sessionComboBox = new ComboBox<>();
        loadCoursesButton = new Button("Charger les cours");
        coursesListView = new ListView<>();
        courseCodeTextField = new TextField();
        firstNameTextField = new TextField();
        lastNameTextField = new TextField();
        emailTextField = new TextField();
        studentIdTextField = new TextField();
        submitButton = new Button("Soumettre l'inscription");
    }

    private void layoutComponents() {
        sessionComboBox.getItems().addAll("Automne", "Hiver", "Ete");
        sessionComboBox.setPromptText("Choisir la session");

        VBox leftVBox = new VBox(10, sessionComboBox, loadCoursesButton, coursesListView);
        leftVBox.setPadding(new Insets(10));
        leftVBox.setSpacing(10);

        GridPane formGridPane = new GridPane();
        formGridPane.setHgap(10);
        formGridPane.setVgap(10);
        formGridPane.setPadding(new Insets(10));
        formGridPane.addRow(0, new Label("Code de cours:"), courseCodeTextField);
        formGridPane.addRow(1, new Label("Prénom:"), firstNameTextField);
        formGridPane.addRow(2, new Label("Nom:"), lastNameTextField);
        formGridPane.addRow(3, new Label("Email:"), emailTextField);
        formGridPane.addRow(4, new Label("Matricule:"), studentIdTextField);

        VBox rightVBox = new VBox(10, formGridPane, submitButton);
        rightVBox.setPadding(new Insets(10));
        rightVBox.setSpacing(10);

        root.setLeft(leftVBox);
        root.setRight(rightVBox);
    }

    public BorderPane getRoot() {
        return root;
    }

    public ComboBox<String> getSessionComboBox() {
        return sessionComboBox;
    }

    public Button getLoadCoursesButton() {
        return loadCoursesButton;
    }

    public ListView<String> getCoursesListView() {
        return coursesListView;
    }

    public TextField getCourseCodeTextField() {
        return courseCodeTextField;
    }

    public TextField getFirstNameTextField() {
        return firstNameTextField;
    }

    public TextField getLastNameTextField() {
        return lastNameTextField;
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }

    public TextField getStudentIdTextField() {
        return studentIdTextField;
    }

    public Button getSubmitButton() {
        return submitButton;
    }
}


