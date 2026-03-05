package org.store.vinyl;

import org.store.vinyl.Interfaces.State;

public class ReservedState implements State
{

  @Override public void reserve(Vinyl vinyl, String userId)
  {

  }

  @Override public void borrow(Vinyl vinyl, String userId)
  {

  }

  @Override public void returnVinyl(Vinyl vinyl)
  {
  vinyl.setState(new AvailableState());
  }

  @Override public String getStateName()
  {
    return "Reserved";
  }
}
