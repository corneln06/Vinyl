module org.store.vinyl {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;

  opens org.store.vinyl to javafx.fxml;
  exports org.store.vinyl;
  exports org.store.vinyl.Model;
  opens org.store.vinyl.Model to javafx.fxml;
}