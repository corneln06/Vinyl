package org.store.vinyl.ViewModel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.store.vinyl.Model.User;
import org.store.vinyl.Model.Vinyl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class VinylCardViewModel implements PropertyChangeListener {

    private static final String GREEN_BUTTON =
        "-fx-background-color: #51D03A; -fx-background-radius: 30; -fx-text-fill: white;";
    private static final String GREY_BUTTON =
        "-fx-background-color: grey; -fx-background-radius: 30; -fx-text-fill: white;";
    private static final String RED_BUTTON =
        "-fx-background-color: red; -fx-background-radius: 30; -fx-text-fill: white;";
    private static final String ORANGE_BUTTON =
        "-fx-background-color: orange; -fx-background-radius: 30; -fx-text-fill: white;";

    private record ButtonConfig(String text, boolean disabled, String style) {}

    private final Vinyl vinyl;
    private final User currentUser;

    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty releaseYear = new SimpleStringProperty();

    private final StringProperty buttonText = new SimpleStringProperty();
    private final StringProperty buttonStyle = new SimpleStringProperty();
    private final BooleanProperty buttonDisabled = new SimpleBooleanProperty();

    public VinylCardViewModel(Vinyl vinyl, User currentUser) {
        this.vinyl = vinyl;
        this.currentUser = currentUser;

        title.set(vinyl.getTitle());
        artist.set(vinyl.getArtist());
        releaseYear.set(String.valueOf(vinyl.getReleaseYear()));

        vinyl.addPropertyChangeListener(this);

        refreshButtonState();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty artistProperty() {
        return artist;
    }

    public StringProperty releaseYearProperty() {
        return releaseYear;
    }

    public StringProperty buttonTextProperty() {
        return buttonText;
    }

    public StringProperty buttonStyleProperty() {
        return buttonStyle;
    }

    public BooleanProperty buttonDisabledProperty() {
        return buttonDisabled;
    }

    public Vinyl getVinyl() {
        return vinyl;
    }

    private ButtonConfig resolveButtonConfig() {
        String state = vinyl.getCurrentState().getStateName();
        String userId = currentUser.getUserId();

        return switch (state) {
            case "Available" -> {
                if(vinyl.getReservedBy() != null
                        && !vinyl.getReservedBy().isEmpty()
                        && !userId.equals(vinyl.getReservedBy()))
                {
                    yield new ButtonConfig("Unavailable", true, GREY_BUTTON);
                }

                yield new ButtonConfig("Borrow", false, GREEN_BUTTON);
            }

            case "Borrowed" -> {
                if (userId.equals(vinyl.getBorrowedBy())) {
                    yield new ButtonConfig("Return", false, RED_BUTTON);
                }
                if (vinyl.getReservedBy() == null || vinyl.getReservedBy().isEmpty()) {
                    yield new ButtonConfig("Reserve", false, ORANGE_BUTTON);
                }
                yield new ButtonConfig("Unavailable", true, GREY_BUTTON);
            }

            case "Reserved" -> {
                if (userId.equals(vinyl.getReservedBy())) {
                    yield new ButtonConfig("Borrow", false, GREEN_BUTTON);
                }
                yield new ButtonConfig("Unavailable", true, GREY_BUTTON);
            }

            default -> new ButtonConfig("Unavailable", true, GREY_BUTTON);
        };
    }

    private void refreshButtonState() {
        ButtonConfig config = resolveButtonConfig();

        buttonText.set(config.text());
        buttonDisabled.set(config.disabled());
        buttonStyle.set(config.style());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())
            || "borrowedBy".equals(evt.getPropertyName())
            || "reservedBy".equals(evt.getPropertyName())) {
            refreshButtonState();
        }
    }
}
