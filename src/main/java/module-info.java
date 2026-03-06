module org.store.vinyl {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires com.fasterxml.jackson.databind;

  opens org.store.vinyl to javafx.fxml;
  exports org.store.vinyl;
}