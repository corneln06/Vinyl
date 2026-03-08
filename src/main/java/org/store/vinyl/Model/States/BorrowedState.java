package org.store.vinyl.Model.States;

import org.store.vinyl.Model.Vinyl;

public class BorrowedState implements State {
    @Override
    public String getStateName() {
        return "Borrowed";
    }

    @Override
    public void reserve(Vinyl vinyl, String userId) {
        vinyl.setReservedBy(userId);
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
