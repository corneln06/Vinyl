package org.store.vinyl;

import org.store.vinyl.Interfaces.State;

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
        if (vinyl.getBorrowedBy().equals(userId)){
            vinyl.setBorrowedBy(userId);
            vinyl.setReservedBy("");
            vinyl.setState(new ReservedState());
        } else{
            System.out.println("Vinyl is already reserved by another user, we apologize for the inconvenience");
        }
    }

    @Override
    public void returnVinyl(Vinyl vinyl) {
        System.out.println("You can't return a reserved Vinyl");
    }


}
