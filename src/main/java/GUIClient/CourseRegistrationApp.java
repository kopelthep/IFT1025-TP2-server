package GUIClient;

import GUIClient.controllers.CourseRegistrationController;
import GUIClient.models.CourseRegistrationModel;
import GUIClient.views.CourseRegistrationView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CourseRegistrationApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        CourseRegistrationModel model = new CourseRegistrationModel();
        CourseRegistrationView view = new CourseRegistrationView();
        CourseRegistrationController controller = new CourseRegistrationController(model, view);

        primaryStage.setTitle("Inscription aux cours");
        primaryStage.setScene(new Scene(view.getRoot(), 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
