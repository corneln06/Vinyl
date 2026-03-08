package org.store.vinyl.Model;

public class User
{
  private String name;
  private String userId;

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
