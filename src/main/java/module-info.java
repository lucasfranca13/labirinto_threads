module org.example.labirintothreads {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.labirintothreads to javafx.fxml;
    exports org.example.labirintothreads;
}