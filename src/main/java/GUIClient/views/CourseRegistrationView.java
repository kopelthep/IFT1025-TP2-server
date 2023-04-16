package GUIClient.views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import server.models.Course;
import javafx.scene.control.cell.PropertyValueFactory;

public class CourseRegistrationView {
    private BorderPane root;
    private ComboBox<String> sessionComboBox;
    private Button loadCoursesButton;
    private TableView<Course> courseTableView;
    private TableColumn<Course, String> courseNameColumn;
    private TableColumn<Course, String> courseCodeColumn;
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

        courseTableView = new TableView<>();
        courseNameColumn = new TableColumn<>("Nom du cours");
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("code"));//THEY ARE SWAPPED, AND I WILL NOT BE FORGIVEN FOR THIS, IT IS TERRIBLE AND MUST BE FIXED
        courseNameColumn.setMinWidth(200);
        courseCodeColumn = new TableColumn<>("Code du cours");
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseCodeColumn.setMinWidth(100);
        courseTableView.getColumns().addAll(courseNameColumn, courseCodeColumn);

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

        VBox leftVBox = new VBox(10, sessionComboBox, loadCoursesButton, courseTableView);
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

        courseTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                courseCodeTextField.setText(newSelection.getCode());
            }
        });
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

    public TableView<Course> getCourseTableView() {
        return courseTableView;
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