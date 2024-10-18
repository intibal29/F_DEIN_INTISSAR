module com.example.f {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.f to javafx.fxml;
    exports com.example.f;
}