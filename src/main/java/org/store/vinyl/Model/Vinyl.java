package org.store.vinyl.Model;

import org.store.vinyl.Interfaces.PropertyChangeSubject;
import org.store.vinyl.Model.States.AvailableState;
import org.store.vinyl.Model.States.State;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Vinyl implements PropertyChangeSubject, Serializable {
    private static final long serialVersionUID = 1L;

    private final String title;
    private final String artist;
    private final int releaseYear;
    private State currentState;
    private String borrowedBy;
    private String reservedBy;
    private String returnedBy;
    private transient PropertyChangeSupport support;

    public Vinyl(String title, String artist, int releaseYear) {
        this.title = title;
        this.artist = artist;
        this.releaseYear = releaseYear;
        this.borrowedBy = "";
        this.reservedBy = "";
        this.returnedBy ="";
        this.currentState = new AvailableState();
        this.support = new PropertyChangeSupport(this);
    }

    public String getArtist() {
        return artist;
    }
    public String getTitle() {
        return title;
    }
    public int getReleaseYear() {
        return releaseYear;
    }
    public String getBorrowedBy() {
        return borrowedBy;
    }
    public String getReservedBy() {
        return reservedBy;
    }


    public State getCurrentState() {
        return currentState;
    }

    public void setState(State newState) {
        State old = this.currentState;
        this.currentState = newState;
        getSupport().firePropertyChange("state", old, newState);
    }
    public void setBorrowedBy(String borrowedBy) {
        String oldVal = this.borrowedBy;
        this.borrowedBy = borrowedBy == null ? "" : borrowedBy;
        getSupport().firePropertyChange("borrowedBy", oldVal, this.borrowedBy);
    }
    public void setReservedBy(String reservedBy) {
        String oldVal = this.reservedBy;
        this.reservedBy = reservedBy == null ? "" : reservedBy;
        getSupport().firePropertyChange("reservedBy", oldVal, this.reservedBy);
    }

    public void reserve(User user) {
        if (user == null) {
            return;
        }
        currentState.reserve(this, user.getUserId());
    }
    public void borrow(User user) {
        if (user == null) {
            return;
        }
        currentState.borrow(this, user.getUserId());
    }
    public void returnVinyl() {
        currentState.returnVinyl(this);
    }

    @Override
    public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
        getSupport().addPropertyChangeListener(name, listener);
    }
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getSupport().addPropertyChangeListener(listener);
    }
    @Override
    public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
        getSupport().removePropertyChangeListener(name, listener);
    }
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getSupport().removePropertyChangeListener(listener);
    }

    private PropertyChangeSupport getSupport() {
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        return support;
    }

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        support = new PropertyChangeSupport(this);
    }
}
