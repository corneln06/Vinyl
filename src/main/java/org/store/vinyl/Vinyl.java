package org.store.vinyl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.store.vinyl.Interfaces.PropertyChangeSubject;
import org.store.vinyl.Interfaces.State;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Vinyl implements PropertyChangeSubject
{
  private StringProperty title = new SimpleStringProperty();
  private StringProperty artist = new SimpleStringProperty();
  private IntegerProperty releaseYear = new SimpleIntegerProperty();
  private State currentState;
  private StringProperty borrowedBy = new SimpleStringProperty();
  private StringProperty reservedBy = new SimpleStringProperty();
  private PropertyChangeSupport support;

  // Required by Jackson
  public Vinyl() {
    currentState = new AvailableState();
    support = new PropertyChangeSupport(this);
  }

  public Vinyl(String title, String artist, int releaseYear) {
    this();
    this.title.set(title);
    this.artist.set(artist);
    this.releaseYear.set(releaseYear);
  }

  public void setState(State newState) {
    State old = this.currentState;
    this.currentState = newState;
    support.firePropertyChange("state", old, newState);
  }

  public void reserve(User user) {
    currentState.reserve(this, user.getUserId());
  }

  public void borrow(User user) {
    currentState.borrow(this, user.getUserId());
  }

  public void returnVinyl() {
    currentState.returnVinyl(this);
  }

  // Plain getters/setters for Jackson
  public String getTitle() { return title.get(); }
  public void setTitle(String title) { this.title.set(title); }

  public String getArtist() { return artist.get(); }
  public void setArtist(String artist) { this.artist.set(artist); }

  public int getReleaseYear() { return releaseYear.get(); }
  public void setReleaseYear(int releaseYear) { this.releaseYear.set(releaseYear); }

  public String getBorrowedBy() { return borrowedBy.get(); }
  public void setBorrowedBy(String borrowedBy) {
    String oldVal = this.borrowedBy.get();
    this.borrowedBy.set(borrowedBy);
    support.firePropertyChange("borrowedBy", oldVal, borrowedBy);
  }

  public String getReservedBy() { return reservedBy.get(); }
  public void setReservedBy(String reservedBy) {
    String oldVal = this.reservedBy.get();
    this.reservedBy.set(reservedBy);
    support.firePropertyChange("reservedBy", oldVal, reservedBy);
  }

  @JsonIgnore
  public State getCurrentState() { return currentState; }

  // JavaFX property getters — ignored by Jackson
  @JsonIgnore public StringProperty titleProperty() { return title; }
  @JsonIgnore public StringProperty artistProperty() { return artist; }
  @JsonIgnore public IntegerProperty releaseYearProperty() { return releaseYear; }
  @JsonIgnore public StringProperty borrowedByProperty() { return borrowedBy; }
  @JsonIgnore public StringProperty reservedByProperty() { return reservedBy; }

  @Override public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
    support.addPropertyChangeListener(name, listener);
  }

  @Override public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  @Override public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
    support.removePropertyChangeListener(name, listener);
  }

  @Override public void removePropertyChangeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
  }
}