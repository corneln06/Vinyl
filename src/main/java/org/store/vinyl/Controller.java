package org.store.vinyl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller
{
    private final VinylLibrary vinylLibrary = new VinylLibrary();
    private final File file = new File("vinyls.json");

    public void initialize() {
        loadVinylsFromJson();
    }

    @FXML
    private void openWindow(ActionEvent event) {
        Button button = (Button) event.getSource();
        String itemName = (String) button.getUserData();
        Vinyl vinyl = vinylLibrary.getVinyls().stream()
                .filter(v -> v.getTitle().equals(itemName))
                .findFirst()
                .orElse(null);
        if (vinyl != null) openBookingWindow(vinyl);
    }

    public void loadVinylsFromJson() {
        if (!file.exists()) {
            saveVinylsToJson();
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Vinyl> loaded = mapper.readValue(file, new TypeReference<>() {});
            loaded.forEach(vinylLibrary::addVinyl); // add through VinylLibrary so listeners fire
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveVinylsToJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(file, vinylLibrary.getVinyls());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openBookingWindow(Vinyl vinyl) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("Vinyl-view.fxml")
            );
            Parent root = loader.load();
            VinylBookController controller = loader.getController();
            controller.setMainController(this);
            controller.setItem(vinyl);
            Stage stage = new Stage();
            stage.setTitle("Book Vinyl");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}