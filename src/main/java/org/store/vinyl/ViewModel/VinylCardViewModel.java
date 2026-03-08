package org.store.vinyl.ViewModel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.store.vinyl.Model.User;
import org.store.vinyl.Model.Vinyl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

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
    private final VinylBookViewModel parentViewModel;

    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty artist = new SimpleStringProperty();
    private final StringProperty releaseYear = new SimpleStringProperty();

    private final StringProperty buttonText = new SimpleStringProperty();
    private final StringProperty buttonStyle = new SimpleStringProperty();
    private final BooleanProperty buttonDisabled = new SimpleBooleanProperty();

    public VinylCardViewModel(Vinyl vinyl, VinylBookViewModel parentViewModel) {
        this.vinyl = vinyl;
        this.parentViewModel = parentViewModel;

        title.set(vinyl.getTitle());
        artist.set(vinyl.getArtist());
        releaseYear.set(String.valueOf(vinyl.getReleaseYear()));

        vinyl.addPropertyChangeListener(this);
        parentViewModel.selectedUserNameProperty().addListener((obs, oldVal, newVal) -> refreshButtonState());

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

    public void handleMainAction() {
        User currentUser = parentViewModel.getSelectedUser();

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

        refreshButtonState();
    }

    private ButtonConfig resolveButtonConfig(String state,
                                             String currentUserId,
                                             String reservedBy,
                                             String borrowedBy) {

        boolean isReserver = Objects.equals(currentUserId, reservedBy);
        boolean isBorrower = Objects.equals(currentUserId, borrowedBy);
        boolean hasReservation = reservedBy != null && !reservedBy.isEmpty();

        return switch (state) {
            case "Available" -> (!hasReservation || isReserver)
                    ? new ButtonConfig("Borrow", false, GREEN_BUTTON)
                    : new ButtonConfig("Unavailable", true, GREY_BUTTON);

            case "Borrowed" -> {
                if (isBorrower) {
                    yield new ButtonConfig("Return", false, RED_BUTTON);
                }
                if (!hasReservation) {
                    yield new ButtonConfig("Reserve", false, ORANGE_BUTTON);
                }
                if (isReserver) {
                    yield new ButtonConfig("Reserved", true, GREY_BUTTON);
                }
                yield new ButtonConfig("Unavailable", true, GREY_BUTTON);
            }

            case "Reserved" -> isReserver
                    ? new ButtonConfig("Borrow", false, GREEN_BUTTON)
                    : new ButtonConfig("Unavailable", true, GREY_BUTTON);

            default -> new ButtonConfig("Unavailable", true, GREY_BUTTON);
        };
    }

    private void refreshButtonState() {
        User currentUser = parentViewModel.getSelectedUser();

        if (currentUser == null) {
            buttonText.set("Select user");
            buttonDisabled.set(true);
            buttonStyle.set(GREY_BUTTON);
            return;
        }

        String state = vinyl.getCurrentState().getStateName();
        String currentUserId = currentUser.getUserId();
        String borrowedBy = vinyl.getBorrowedBy();
        String reservedBy = vinyl.getReservedBy();

        ButtonConfig config = resolveButtonConfig(state, currentUserId, reservedBy, borrowedBy);

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