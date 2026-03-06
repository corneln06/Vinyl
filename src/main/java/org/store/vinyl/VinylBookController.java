package org.store.vinyl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class VinylBookController
{
  @FXML private Label titleLabel;
  @FXML private Label artistLabel;
  @FXML private Label releaseYearLabel;
  @FXML private Label borrowedByLabel;
  @FXML private Label reservedByLabel;
  @FXML private Label stateLabel;
  @FXML private Label errorLabel;
  @FXML private TextField nameField;
  @FXML private TextField userIdField;
  @FXML private Button borrowButton;
  @FXML private Button returnButton;

  private Vinyl vinyl;
  private Controller mainController;

  public void setItem(Vinyl vinyl) {
    this.vinyl = vinyl;
    updateUI();

    vinyl.addPropertyChangeListener("state", e -> updateUI());
    vinyl.addPropertyChangeListener("borrowedBy", e -> updateUI());
    vinyl.addPropertyChangeListener("reservedBy", e -> updateUI());
  }

  public void setMainController(Controller mainController) {
    this.mainController = mainController;
  }

  private void updateUI() {
    titleLabel.setText(vinyl.getTitle());
    artistLabel.setText(vinyl.getArtist());
    releaseYearLabel.setText(String.valueOf(vinyl.getReleaseYear()));
    borrowedByLabel.setText(vinyl.getBorrowedBy().isEmpty() ? "Nobody" : vinyl.getBorrowedBy());
    reservedByLabel.setText(vinyl.getReservedBy().isEmpty() ? "Nobody" : vinyl.getReservedBy());
    stateLabel.setText(vinyl.getCurrentState().getStateName());
    errorLabel.setText("");

    String state = vinyl.getCurrentState().getStateName();
    borrowButton.setDisable(state.equals("Borrowed"));
    returnButton.setDisable(state.equals("Available"));
  }

  @FXML
  private void handleBorrow() {
    String name = nameField.getText().trim();
    String userId = userIdField.getText().trim();
    if (name.isEmpty() || userId.isEmpty()) {
      errorLabel.setText("Please enter both a name and user ID.");
      return;
    }
    vinyl.borrow(new User(name, userId));
    if (mainController != null) mainController.saveVinylsToJson();
    nameField.clear();
    userIdField.clear();
  }

  @FXML
  private void handleReturn() {
    vinyl.returnVinyl();
    if (mainController != null) mainController.saveVinylsToJson();
    nameField.clear();
    userIdField.clear();
  }
}