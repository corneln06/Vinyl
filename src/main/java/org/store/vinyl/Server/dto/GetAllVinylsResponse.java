package org.store.vinyl.Server.dto;

import org.store.vinyl.Model.Vinyl;

import java.io.Serializable;
import java.util.ArrayList;

public class GetAllVinylsResponse implements Serializable
{
  private static final long serialVersionUID = 1L;

  private final ArrayList<Vinyl> vinyls;

  public GetAllVinylsResponse(ArrayList<Vinyl> vinyls){
    this.vinyls = vinyls;
  }

  public ArrayList<Vinyl> getVinyls()
  {
    return vinyls;
  }
}
