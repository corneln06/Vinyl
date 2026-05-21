package org.store.vinyl.Server.dto;

import org.store.vinyl.Model.Vinyl;

import java.io.Serializable;

public class ReturnVinylResponse implements Serializable
{
  private static final long serialVersionUID = 1L;

  private final Vinyl vinyl;

  public ReturnVinylResponse(Vinyl vinyl)
  {
    this.vinyl = vinyl;
  }

  public Vinyl getVinyl()
  {
    return vinyl;
  }
}
