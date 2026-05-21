package org.store.vinyl.Server.dto;

import org.store.vinyl.Model.User;

import java.io.Serializable;

public class ReserveVinylRequest implements Serializable
{
  private static final long serialVersionUID = 1L;

  private final String title;
  private final User user;

  public ReserveVinylRequest(String title, User user)
  {
    this.title = title;
    this.user = user;
  }

  public String getTitle()
  {
    return title;
  }

  public User getUser()
  {
    return user;
  }
}
