module org.store.vinyl {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.store.vinyl to javafx.fxml;
    exports org.store.vinyl;
}