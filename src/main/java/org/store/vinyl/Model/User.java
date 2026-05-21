package org.store.vinyl.Model;

import java.io.Serializable;

public class User implements Serializable
{
  private static final long serialVersionUID = 1L;

  private final String name;
  private final String userId;

  public User(String name, String userId){
    this.name = name;
    this.userId = userId;
  }

  public String getUserId()
  {
    return userId;
  }

  public String getName()
  {
    return name;
  }
}
