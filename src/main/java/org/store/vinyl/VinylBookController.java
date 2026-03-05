package org.store.vinyl;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class VinylBookController
{
  @FXML
  private Label itemLabel;

  public void setItem(String itemName) {
    itemLabel.setText(itemName);
  }
}
