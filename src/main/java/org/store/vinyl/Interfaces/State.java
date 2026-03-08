package org.store.vinyl.Interfaces;



import javafx.beans.property.StringProperty;
import org.store.vinyl.Vinyl;

public interface State
{
  void reserve(Vinyl vinyl, String userId);
  void borrow(Vinyl vinyl, String userId);
  void returnVinyl(Vinyl vinyl);
  String getStateName();
}
