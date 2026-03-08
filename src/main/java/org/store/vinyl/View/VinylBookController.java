package org.store.vinyl.View;

import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.store.vinyl.ViewModel.VinylBookViewModel;
import org.store.vinyl.ViewModel.VinylCardViewModel;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class VinylBookController implements Initializable {

    @FXML
    private FlowPane vinylContainer;

    @FXML
    private ChoiceBox<String> userSelector;

    private final VinylBookViewModel viewModel;

    private StackPane openedCard = null;
    private Pane openedFront = null;
    private Pane openedBack = null;
    private boolean isFlipping = false;

    public VinylBookController(VinylBookViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userSelector.setItems(FXCollections.observableArrayList(viewModel.getUserNames()));
        userSelector.valueProperty().bindBidirectional(viewModel.selectedUserNameProperty());

        renderVinyls();
    }

    private void renderVinyls() {
        vinylContainer.getChildren().clear();

        for (VinylCardViewModel vinylVm : viewModel.getVinyls()) {
            Pane card = createVinylCard(vinylVm);
            vinylContainer.getChildren().add(card);
        }
    }

    private Pane createVinylCard(VinylCardViewModel vinylVm) {
        StackPane card = new StackPane();
        card.setPrefSize(124, 165);

        Pane front = createFrontSide(vinylVm);
        Pane back = createBackSide(vinylVm);

        back.setVisible(false);

        card.getChildren().addAll(back, front);
        card.setUserData(false);

        card.setOnMouseClicked(event -> {
            if (isFlipping) return;

            boolean isThisCardOpen = (boolean) card.getUserData();

            if (isThisCardOpen) {
                flipCard(card, front, back, () -> {
                    openedCard = null;
                    openedFront = null;
                    openedBack = null;
                });
                return;
            }

            if (openedCard != null && openedCard != card) {
                StackPane previousCard = openedCard;
                Pane previousFront = openedFront;
                Pane previousBack = openedBack;

                flipCard(previousCard, previousFront, previousBack, () -> {
                    openedCard = null;
                    openedFront = null;
                    openedBack = null;

                    flipCard(card, front, back, () -> {
                        openedCard = card;
                        openedFront = front;
                        openedBack = back;
                    });
                });
                return;
            }

            flipCard(card, front, back, () -> {
                openedCard = card;
                openedFront = front;
                openedBack = back;
            });
        });

        return card;
    }

    private Pane createFrontSide(VinylCardViewModel vinylVm) {
        Pane front = new Pane();
        front.setPrefSize(124, 165);
        front.setStyle("-fx-background-color: #AFAFAF; -fx-background-radius: 10;");

        ImageView imageView = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/pic.png")))
        );
        imageView.setFitWidth(99);
        imageView.setFitHeight(99);
        imageView.setLayoutX(17);
        imageView.setLayoutY(10);
        imageView.setPreserveRatio(true);

        Label titleLabel = new Label();
        titleLabel.textProperty().bind(vinylVm.titleProperty());
        titleLabel.setLayoutX(15);
        titleLabel.setLayoutY(100);
        titleLabel.setPrefWidth(94);
        titleLabel.setAlignment(Pos.CENTER);

        Button bookButton = new Button();
        bookButton.setLayoutX(21);
        bookButton.setLayoutY(129);
        bookButton.setPrefSize(90, 20);

        bookButton.textProperty().bind(vinylVm.buttonTextProperty());
        bookButton.disableProperty().bind(vinylVm.buttonDisabledProperty());
        bookButton.styleProperty().bind(vinylVm.buttonStyleProperty());

        bookButton.setOnAction(e -> {
            e.consume();
            vinylVm.handleMainAction();
        });

        front.getChildren().addAll(imageView, titleLabel, bookButton);
        return front;
    }

    private Pane createBackSide(VinylCardViewModel vinylVm) {
        Pane back = new Pane();
        back.setPrefSize(124, 165);
        back.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 10;");

        Label titleLabel = new Label();
        titleLabel.textProperty().bind(vinylVm.titleProperty());
        titleLabel.setLayoutX(10);
        titleLabel.setLayoutY(25);
        titleLabel.setPrefWidth(104);
        titleLabel.setAlignment(Pos.CENTER);

        Label artistLabel = new Label();
        artistLabel.textProperty().bind(vinylVm.artistProperty());
        artistLabel.setLayoutX(10);
        artistLabel.setLayoutY(65);
        artistLabel.setPrefWidth(104);
        artistLabel.setAlignment(Pos.CENTER);

        Label yearLabel = new Label();
        yearLabel.textProperty().bind(vinylVm.releaseYearProperty());
        yearLabel.setLayoutX(10);
        yearLabel.setLayoutY(100);
        yearLabel.setPrefWidth(104);
        yearLabel.setAlignment(Pos.CENTER);

        back.getChildren().addAll(titleLabel, artistLabel, yearLabel);
        return back;
    }

    private void flipCard(StackPane card, Pane front, Pane back, Runnable onFinishedAction) {
        isFlipping = true;

        boolean showingBack = (boolean) card.getUserData();

        RotateTransition firstHalf = new RotateTransition(Duration.millis(150), card);
        firstHalf.setFromAngle(0);
        firstHalf.setToAngle(90);

        RotateTransition secondHalf = new RotateTransition(Duration.millis(150), card);
        secondHalf.setFromAngle(-90);
        secondHalf.setToAngle(0);

        firstHalf.setOnFinished(e -> {
            front.setVisible(showingBack);
            back.setVisible(!showingBack);

            card.setRotate(-90);
            card.setUserData(!showingBack);

            secondHalf.setOnFinished(e2 -> {
                isFlipping = false;
                if (onFinishedAction != null) {
                    onFinishedAction.run();
                }
            });

            secondHalf.play();
        });

        firstHalf.play();
    }
}