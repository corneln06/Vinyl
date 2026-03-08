package org.store.vinyl;

import org.store.vinyl.Model.States.State;
import org.store.vinyl.Model.Vinyl;

public class ReservedState implements State {
    @Override
    public String getStateName() {
        return "Reserved";
    }

    @Override
    public void reserve(Vinyl vinyl, String userId) {
        System.out.println("Vinyl is already reserved");
    }

    @Override
    public void borrow(Vinyl vinyl, String userId) {
        if (vinyl.getReservedBy().equals(userId)){
            vinyl.setState(new BorrowedState());
            vinyl.setBorrowedBy(userId);
            vinyl.setReservedBy("");
        } else{
            System.out.println("Vinyl is already reserved by another user, we apologize for the inconvenience");
        }
    }

    @Override
    public void returnVinyl(Vinyl vinyl) {
        System.out.println("You can't return a reserved Vinyl");
    }


}
