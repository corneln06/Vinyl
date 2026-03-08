package org.store.vinyl.ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.store.vinyl.Model.User;
import org.store.vinyl.Model.Vinyl;

import java.util.List;

public class VinylBookViewModel {

    private final List<User> users;
    private final ObservableList<VinylCardViewModel> vinyls;
    private final StringProperty selectedUserName = new SimpleStringProperty();

    public VinylBookViewModel(List<Vinyl> vinylList, List<User> users) {
        this.users = users;
        this.vinyls = FXCollections.observableArrayList();

        for (Vinyl vinyl : vinylList) {
            this.vinyls.add(new VinylCardViewModel(vinyl, this));
        }
    }

    public ObservableList<VinylCardViewModel> getVinyls() {
        return vinyls;
    }

    public List<String> getUserNames() {
        return users.stream()
                .map(User::getName)
                .toList();
    }

    public StringProperty selectedUserNameProperty() {
        return selectedUserName;
    }

    public User getSelectedUser() {
        String selectedName = selectedUserName.get();

        if (selectedName == null || selectedName.isBlank()) {
            return null;
        }

        return users.stream()
                .filter(user -> user.getName().equals(selectedName))
                .findFirst()
                .orElse(null);
    }
}