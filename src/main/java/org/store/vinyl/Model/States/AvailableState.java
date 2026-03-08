package org.store.vinyl.Model.States;

import org.store.vinyl.Model.Vinyl;

public class AvailableState implements State {

    @Override
    public String getStateName() {
        return "Available";
    }

    @Override
    public void reserve(Vinyl vinyl, String userId) {
        vinyl.setReservedBy(userId);
        vinyl.setState(new ReservedState());
    }

    @Override
    public void borrow(Vinyl vinyl, String userId) {
        vinyl.setBorrowedBy(userId);
        vinyl.setReservedBy("");
        vinyl.setState(new BorrowedState());
    }

    @Override
    public void returnVinyl(Vinyl vinyl) {
        System.out.println("Vinyl is already available.");
    }


}
