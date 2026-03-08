package org.store.vinyl;

import javafx.animation.RotateTransition;
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

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class VinylBookController implements Initializable {

    @FXML
    private FlowPane vinylContainer;
    @FXML
    private ChoiceBox<String> userSelector;

    private StackPane openedCard = null;
    private Pane openedFront = null;
    private Pane openedBack = null;
    private boolean isFlipping = false;
    private User currentUser;

    private final List<Vinyl> vinyls = List.of(
            new Vinyl("Abbey Road", "The Beatles", 1969),
            new Vinyl("Thriller", "Michael Jackson", 1982),
            new Vinyl("Back in Black", "AC/DC", 1980),
            new Vinyl("The Dark Side of the Moon", "Pink Floyd", 1973),
            new Vinyl("Rumours", "Fleetwood Mac", 1977),
            new Vinyl("Hotel California", "Eagles", 1976),
            new Vinyl("Nevermind", "Nirvana", 1991),
            new Vinyl("Born in the U.S.A.", "Bruce Springsteen", 1984),
            new Vinyl("Purple Rain", "Prince", 1984),
            new Vinyl("Led Zeppelin IV", "Led Zeppelin", 1971),
            new Vinyl("Why Cornel is gay", "Led Zeppelin", 1971)
    );

    private final List<User> users = List.of(
            new User("Deadpool", "deadpool"),
            new User("Daredevil", "daredevil"),
            new User("Iron Man", "ironman"),
            new User("Hulk", "hulk"),
            new User("Thanos", "thanos")
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        renderVinyls();
        addUsers();
    }

    private void renderVinyls() {
        vinylContainer.getChildren().clear();

        for (Vinyl vinyl : vinyls) {
            Pane card = createVinylCard(vinyl);
            vinylContainer.getChildren().add(card);
        }
    }

    private void addUsers() {
        userSelector.getItems().addAll(
                users.stream().map(User::getName).toList()
        );
    }

    public void setCurrentUser() {
        currentUser = users.stream()
                .filter(u -> u.getName().equals(userSelector.getValue()))
                .findFirst()
                .orElse(null);
    }

    private Pane createVinylCard(Vinyl vinyl) {
        StackPane card = new StackPane();
        card.setPrefSize(124, 165);

        Pane front = createFrontSide(vinyl);
        Pane back = createBackSide(vinyl);

        back.setVisible(false);

        card.getChildren().addAll(back, front);
        card.setUserData(false);

        card.setOnMouseClicked(event -> {
            if (isFlipping) {
                return;
            }

            boolean isThisCardOpen = (boolean) card.getUserData();

            // If the card is already open, close it
            if (isThisCardOpen) {
                flipCard(card, front, back, () -> {
                    openedCard = null;
                    openedFront = null;
                    openedBack = null;
                });
                return;
            }

            // If there is a card open, first close that one and then open a new one
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

            // If no card is open, open this one
            flipCard(card, front, back, () -> {
                openedCard = card;
                openedFront = front;
                openedBack = back;
            });
        });

        return card;
    }

    private Pane createFrontSide(Vinyl vinyl) {
        Pane front = new Pane();
        front.setPrefSize(124, 165);
        front.setStyle("-fx-background-color: #AFAFAF; -fx-background-radius: 10;");

        ImageView imageView = new ImageView(
                new Image(getClass().getResourceAsStream("/Images/pic.png"))
        );
        imageView.setFitWidth(99);
        imageView.setFitHeight(99);
        imageView.setLayoutX(17);
        imageView.setLayoutY(10);
        imageView.setPreserveRatio(true);

        Label titleLabel = new Label(vinyl.getTitle());
        titleLabel.setLayoutX(15);
        titleLabel.setLayoutY(100);
        titleLabel.setPrefWidth(94);
        titleLabel.setAlignment(Pos.CENTER);

        Button bookButton = new Button();
        bookButton.setId("bookButton");
        bookButton.setLayoutX(21);
        bookButton.setLayoutY(129);
        bookButton.setPrefSize(90, 20);

        Runnable refreshButton = () -> {
            if ("Borrowed".equals(vinyl.getCurrentState().getStateName()) && Objects.equals(currentUser.getUserId(), vinyl.getReservedBy())) {
                bookButton.setText("Return");
                bookButton.setDisable(false);
                bookButton.setStyle("-fx-background-color: red; -fx-background-radius: 30; -fx-text-fill: white;");
            } else if ("Borrowed".equals(vinyl.getCurrentState().getStateName()) && !vinyl.getReservedBy().isEmpty() && Objects.equals(currentUser.getUserId(), vinyl.getReservedBy())) {
                bookButton.setText("Reserved");
                bookButton.setDisable(true);
                bookButton.setStyle("-fx-background-color: grey; -fx-background-radius: 30; -fx-text-fill: white;");
            } else if ("Borrowed".equals(vinyl.getCurrentState().getStateName()) && !vinyl.getReservedBy().isEmpty() && !Objects.equals(currentUser.getUserId(), vinyl.getReservedBy())) {
                bookButton.setText("Unavailable");
                bookButton.setDisable(true);
                bookButton.setStyle("-fx-background-color: grey; -fx-background-radius: 30; -fx-text-fill: white;");
            } else if ("Reserved".equals(vinyl.getCurrentState().getStateName()) && Objects.equals(currentUser.getUserId(), vinyl.getReservedBy())) {
                bookButton.setText("Borrow");
                bookButton.setDisable(false);
                bookButton.setStyle("-fx-background-color: #51D03A; -fx-background-radius: 30; -fx-text-fill: white;");
            } else if ("Reserved".equals(vinyl.getCurrentState().getStateName()) && !Objects.equals(currentUser.getUserId(), vinyl.getReservedBy())) {
                bookButton.setText("Unavailable");
                bookButton.setDisable(true);
                bookButton.setStyle("-fx-background-color: grey; -fx-background-radius: 30; -fx-text-fill: white;");
            } else {
                bookButton.setText("Book");
                bookButton.setDisable(false);
                bookButton.setStyle("-fx-background-color: #51D03A; -fx-background-radius: 30; -fx-text-fill: white;");
            }
        };

        refreshButton.run();

        vinyl.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())
                    || "borrowedBy".equals(evt.getPropertyName())
                    || "reservedBy".equals(evt.getPropertyName())) {
                refreshButton.run();
            }
        });

        bookButton.setOnAction(e -> {
            e.consume();

            if ("Available".equals(vinyl.getCurrentState().getStateName())) {
                vinyl.borrow(currentUser);
            } else if ("Borrowed".equals(vinyl.getCurrentState().getStateName()) && vinyl.getReservedBy().isEmpty()) {
                vinyl.reserve(currentUser);
            } else if ("Reserved".equals(vinyl.getCurrentState().getStateName())) {
                vinyl.borrow(currentUser);
            }

            System.out.println(vinyl.getCurrentState().getStateName());
        });

        front.getChildren().addAll(imageView, titleLabel, bookButton);
        return front;
    }

    private Pane createBackSide(Vinyl vinyl) {
        Pane back = new Pane();
        back.setPrefSize(124, 165);
        back.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 10;");

        Label titleLabel = new Label(vinyl.getTitle());
        titleLabel.setLayoutX(10);
        titleLabel.setLayoutY(25);
        titleLabel.setPrefWidth(104);
        titleLabel.setAlignment(Pos.CENTER);

        Label artistLabel = new Label(vinyl.getArtist());
        artistLabel.setLayoutX(10);
        artistLabel.setLayoutY(65);
        artistLabel.setPrefWidth(104);
        artistLabel.setAlignment(Pos.CENTER);

        Label yearLabel = new Label(String.valueOf(vinyl.getReleaseYear()));
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

    private void openWindow(Vinyl vinyl) {
        System.out.println("Selected vinyl: " + vinyl.getTitle());
    }
}