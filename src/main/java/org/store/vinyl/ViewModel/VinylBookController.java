package org.store.vinyl.ViewModel;

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
import org.store.vinyl.Model.User;
import org.store.vinyl.Model.Vinyl;

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
        addUsers();
        renderVinyls();

        userSelector.setOnAction(e -> {
            setCurrentUser();
            renderVinyls();
        });
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

    private Pane createFrontSide(Vinyl vinyl) {
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
            String state = vinyl.getCurrentState().getStateName();

            if (currentUser == null) {
                bookButton.setText("Select user");
                bookButton.setDisable(true);
                bookButton.setStyle("-fx-background-color: grey; -fx-background-radius: 30; -fx-text-fill: white;");
                return;
            }

            String currentUserId = currentUser.getUserId();
            String borrowedBy = vinyl.getBorrowedBy();
            String reservedBy = vinyl.getReservedBy();

            if ("Available".equals(state) && (reservedBy.isEmpty() || Objects.equals(currentUserId, reservedBy))) {
                bookButton.setText("Borrow");
                bookButton.setDisable(false);
                bookButton.setStyle("-fx-background-color: #51D03A; -fx-background-radius: 30; -fx-text-fill: white;");

            } else if ("Borrowed".equals(state) && Objects.equals(currentUserId, borrowedBy)) {
                bookButton.setText("Return");
                bookButton.setDisable(false);
                bookButton.setStyle("-fx-background-color: red; -fx-background-radius: 30; -fx-text-fill: white;");

            } else if ("Borrowed".equals(state) && !reservedBy.isEmpty() && Objects.equals(currentUserId, reservedBy)) {
                bookButton.setText("Reserved");
                bookButton.setDisable(true);
                bookButton.setStyle("-fx-background-color: grey; -fx-background-radius: 30; -fx-text-fill: white;");

            } else if ("Borrowed".equals(state) && !reservedBy.isEmpty() && !Objects.equals(currentUserId, reservedBy)) {
                bookButton.setText("Unavailable");
                bookButton.setDisable(true);
                bookButton.setStyle("-fx-background-color: grey; -fx-background-radius: 30; -fx-text-fill: white;");

            } else if ("Borrowed".equals(state) && reservedBy.isEmpty()) {
                bookButton.setText("Reserve");
                bookButton.setDisable(false);
                bookButton.setStyle("-fx-background-color: #51D03A; -fx-background-radius: 30; -fx-text-fill: white;");

            } else if ("Reserved".equals(state) && Objects.equals(currentUserId, reservedBy)) {
                bookButton.setText("Borrow");
                bookButton.setDisable(false);
                bookButton.setStyle("-fx-background-color: #51D03A; -fx-background-radius: 30; -fx-text-fill: white;");

            } else if ("Reserved".equals(state)) {
                bookButton.setText("Unavailable");
                bookButton.setDisable(true);
                bookButton.setStyle("-fx-background-color: grey; -fx-background-radius: 30; -fx-text-fill: white;");

            } else if ("Available".equals(state) && !reservedBy.isEmpty() && !Objects.equals(currentUserId, reservedBy)){
                bookButton.setText("Unavailable");
                bookButton.setDisable(true);
                bookButton.setStyle("-fx-background-color: grey; -fx-background-radius: 30; -fx-text-fill: white;");
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

            if (currentUser == null) {
                return;
            }

            String state = vinyl.getCurrentState().getStateName();
            String currentUserId = currentUser.getUserId();

            if ("Available".equals(state)) {
                vinyl.borrow(currentUser);

            } else if ("Borrowed".equals(state) && Objects.equals(currentUserId, vinyl.getBorrowedBy())) {
                vinyl.returnVinyl();

            } else if ("Borrowed".equals(state)
                    && (vinyl.getReservedBy() == null || vinyl.getReservedBy().isEmpty())
                    && !Objects.equals(currentUserId, vinyl.getBorrowedBy())) {
                vinyl.reserve(currentUser);

            } else if ("Reserved".equals(state) && Objects.equals(currentUserId, vinyl.getReservedBy())) {
                vinyl.borrow(currentUser);
            }

            refreshButton.run();
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