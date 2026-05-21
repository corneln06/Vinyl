package org.store.vinyl.ViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.store.vinyl.Model.User;
import org.store.vinyl.Model.Vinyl;

import java.util.List;

public class VinylBookViewModel {

    private final ObservableList<VinylCardViewModel> vinyls;
    private final User currentUser;

    public VinylBookViewModel(List<Vinyl> vinylList, User currentUser) {
        this.vinyls = FXCollections.observableArrayList();
        this.currentUser = currentUser;

        for (Vinyl vinyl : vinylList) {
            this.vinyls.add(new VinylCardViewModel(vinyl, currentUser));
        }
    }

    public ObservableList<VinylCardViewModel> getVinyls() {
        return vinyls;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setVinyls(List<Vinyl> vinylList) {
        vinyls.clear();

        for (Vinyl vinyl : vinylList) {
            vinyls.add(new VinylCardViewModel(vinyl, currentUser));
        }
    }

    public void updateVinyl(Vinyl updatedVinyl) {
        for (int i = 0; i < vinyls.size(); i++) {
            VinylCardViewModel vinylVm = vinyls.get(i);

            if (vinylVm.titleProperty().get().equals(updatedVinyl.getTitle())) {
                vinyls.set(i, new VinylCardViewModel(updatedVinyl, currentUser));
                return;
            }
        }

        vinyls.add(new VinylCardViewModel(updatedVinyl, currentUser));
    }
}
