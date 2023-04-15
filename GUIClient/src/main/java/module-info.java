module com.example.guiclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens guiclient to javafx.fxml;
    exports guiclient;
}