package org.store.vinyl.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.store.vinyl.Model.States.AvailableState;
import org.store.vinyl.Interfaces.PropertyChangeSubject;
import org.store.vinyl.Model.States.State;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Vinyl implements PropertyChangeSubject {
    private String title;
    private String artist;
    private int releaseYear;
    private State currentState;
    private StringProperty borrowedBy;
    private StringProperty reservedBy;
    private PropertyChangeSupport support;

    public Vinyl(String title, String artist, int releaseYear) {
        this.title = title;
        this.artist = artist;
        this.releaseYear = releaseYear;
        borrowedBy = new SimpleStringProperty("");
        reservedBy = new SimpleStringProperty("");
        currentState = new AvailableState();
        support = new PropertyChangeSupport(this);
    }

    // Getters
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
        return borrowedBy.get();
    }
    public String getReservedBy() {
        return reservedBy.get();
    }
    public State getCurrentState() {
        return currentState;
    }

    // Setters
    public void setState(State newState) {
        State old = this.currentState;
        this.currentState = newState;
        support.firePropertyChange("state", old, newState);
    }
    public void setBorrowedBy(String borrowedBy) {
        String oldVal = this.borrowedBy.get();
        this.borrowedBy.set(borrowedBy);
        support.firePropertyChange("borrowedBy", oldVal, borrowedBy);

    }
    public void setReservedBy(String reservedBy) {
        String oldVal = this.reservedBy.get();
        this.reservedBy.set(reservedBy);
        support.firePropertyChange("reservedBy", oldVal, reservedBy);
    }

    // Vinyl actions
    public void reserve(User user) {
        currentState.reserve(this, user.getUserId());
    }
    public void borrow(User user) {
        System.out.println(currentState.getStateName());
        currentState.borrow(this, user.getUserId());
    }
    public void returnVinyl() {
        currentState.returnVinyl(this);
    }

    // Listeners
    @Override
    public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
        support.addPropertyChangeListener(name, listener);
    }
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    @Override
    public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
        support.removePropertyChangeListener(name, listener);
    }
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);

    }
}
