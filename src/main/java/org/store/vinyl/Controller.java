package org.store.vinyl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller
{
  @FXML private void openWindow(ActionEvent event){
    Button button = (Button) event.getSource();
    String itemName = (String) button.getUserData();
    openBookingWindow(itemName);
  }
  private void openBookingWindow(String vinyl) {
    try {

      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("Vinyl-view.fxml")
      );

      Parent root = loader.load();

      VinylBookController controller = loader.getController();
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
