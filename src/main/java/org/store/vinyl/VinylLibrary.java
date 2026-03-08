package org.store.vinyl;

import org.store.vinyl.Interfaces.PropertyChangeSubject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class VinylLibrary implements PropertyChangeSubject
{
  private ArrayList<Vinyl> vinyls;
  private PropertyChangeSupport support;

  public VinylLibrary() {
    vinyls = new ArrayList<>();
    support = new PropertyChangeSupport(this);
  }

  public void addVinyl(Vinyl vinyl) {
    vinyls.add(vinyl);
    vinyl.addPropertyChangeListener(evt ->
        support.firePropertyChange(
            evt.getPropertyName(),
            evt.getOldValue(),
            evt.getNewValue()));
    support.firePropertyChange("vinylAdded", null, vinyl);
  }

  public void removeVinyl(Vinyl vinyl) {
    vinyls.remove(vinyl);
    support.firePropertyChange("vinylRemoved", vinyl, null);
  }

  public ArrayList<Vinyl> getVinyls() {
    return vinyls;
  }

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