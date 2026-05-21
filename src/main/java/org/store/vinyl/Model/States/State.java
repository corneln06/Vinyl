package org.store.vinyl.Model.States;

import org.store.vinyl.Model.Vinyl;

import java.io.Serializable;

public interface State extends Serializable
{
  void reserve(Vinyl vinyl, String userId);
  void borrow(Vinyl vinyl, String userId);
  void returnVinyl(Vinyl vinyl);
  String getStateName();
}
