package org.store.vinyl.Model.States;



import org.store.vinyl.Model.Vinyl;

public interface State
{
  void reserve(Vinyl vinyl, String userId);
  void borrow(Vinyl vinyl, String userId);
  void returnVinyl(Vinyl vinyl);
  String getStateName();
}
