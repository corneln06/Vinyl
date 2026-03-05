package org.store.vinyl;

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
  private StringProperty title;
  private StringProperty artist;
  private IntegerProperty releaseYear;
  private State currentState;
  private StringProperty borrowedBy;
  private StringProperty reservedBy;
  private PropertyChangeSupport support;

  public Vinyl(String title, String artist, int releaseYear){
    this.title = new SimpleStringProperty(title);
    this.artist = new SimpleStringProperty(artist);
    this.releaseYear = new SimpleIntegerProperty(releaseYear);
    borrowedBy = new SimpleStringProperty();
    reservedBy = new SimpleStringProperty();
    currentState = new AvailableState();
    support = new PropertyChangeSupport(this);
  }
  public void setState(State newState){
    State old = this.currentState;
    this.currentState = newState;
    support.firePropertyChange("state", old, newState);
  }
  public void reserve(User user){
    currentState.reserve(this, user.getUserId());
  }
  public void borrow(User user){
    currentState.borrow(this, user.getUserId());
  }
  public void returnVinyl(){
    currentState.returnVinyl(this);
  }

  @Override public void addPropertyChangeListener(String name,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(name, listener);
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
      support.addPropertyChangeListener(listener);
  }

  @Override public void removePropertyChangeListener(String name,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(name, listener);
  }

  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);

  }

  public String getBorrowedBy()
  {
    return borrowedBy.get();
  }

  public String getReservedBy()
  {
    return reservedBy.get();
  }

  public void setBorrowedBy(String borrowedBy)
  {
    String oldVal = this.borrowedBy.get();
    this.borrowedBy.set(borrowedBy);
    support.firePropertyChange("borrowedBy", oldVal, borrowedBy);

  }

  public void setReservedBy(String reservedBy)
  {
    String oldVal = this.reservedBy.get();
    this.reservedBy.set(reservedBy);
    support.firePropertyChange("reservedBy", oldVal, reservedBy);
  }

  public String getArtist()
  {
    return artist.get();
  }

  public String getTitle()
  {
    return title.get();
  }

  public State getCurrentState()
  {
    return currentState;
  }

  public StringProperty borrowedByProperty()
  {
    return borrowedBy;
  }
  public StringProperty titleProperty() {
    return title;
  }

  public StringProperty artistProperty() {
    return artist;
  }

  public IntegerProperty releaseYearProperty() {
    return releaseYear;
  }

  public StringProperty reservedByProperty() {
    return reservedBy;
  }
}
