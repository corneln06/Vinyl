package org.store.vinyl;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class VinylBookController implements Initializable {

    @FXML
    private FlowPane vinylContainer;

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
            new Vinyl("Led Zeppelin IV", "Led Zeppelin", 1971)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        renderVinyls();
    }

    private void renderVinyls() {
        vinylContainer.getChildren().clear();

        for (Vinyl vinyl : vinyls) {
            Pane card = createVinylCard(vinyl);
            vinylContainer.getChildren().add(card);
        }
    }

    private Pane createVinylCard(Vinyl vinyl) {
        Pane card = new Pane();
        card.setPrefSize(124, 155);
        card.setStyle("-fx-background-color: #AFAFAF; -fx-background-radius: 10;");

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
        titleLabel.setLayoutY(90);
        titleLabel.setPrefWidth(94);
        titleLabel.setAlignment(Pos.CENTER);

        Button bookButton = new Button("Book");
        bookButton.setLayoutX(21);
        bookButton.setLayoutY(129);
        bookButton.setPrefSize(90, 20);
        bookButton.setStyle("-fx-background-color: #51D03A; -fx-background-radius: 30; -fx-text-fill: white;");

        bookButton.setOnAction(event -> openWindow(vinyl));

        card.getChildren().addAll(imageView, titleLabel, bookButton);

        return card;
    }

    private void openWindow(Vinyl vinyl) {
        System.out.println("Selected vinyl: " + vinyl.getTitle());
    }
}