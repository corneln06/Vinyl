package org.store.vinyl;

import javafx.beans.property.SimpleStringProperty;
import org.store.vinyl.Interfaces.State;

public class BorrowedState implements State {
    @Override
    public String getStateName() {
        return "Borrowed";
    }

    @Override
    public void reserve(Vinyl vinyl, String userId) {
        vinyl.setReservedBy(userId);
        vinyl.setState(new ReservedState());
    }

    @Override
    public void borrow(Vinyl vinyl, String userId) {
        System.out.println("Vinyl is already borrowed");
    }

    @Override
    public void returnVinyl(Vinyl vinyl) {
        vinyl.setState(new AvailableState());
        vinyl.setBorrowedBy("");
    }


}
