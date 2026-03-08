module org.store.vinyl {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires javafx.base;


    exports org.store.vinyl.Model;
    opens org.store.vinyl.Model to javafx.fxml;
    exports org.store.vinyl.Model.States;
    opens org.store.vinyl.Model.States to javafx.fxml;
    exports org.store.vinyl.View;
    opens org.store.vinyl.View to javafx.fxml;
    exports org.store.vinyl.ViewModel;
    opens org.store.vinyl.ViewModel to javafx.fxml;
}