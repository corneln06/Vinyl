package org.store.vinyl;

import org.store.vinyl.Model.User;

public class LogMessage {
  private String message;
  private String time;
  private User user;

  public LogMessage(String message, String time, User user) {
    this.message = message;
    this.time = time;
    this.user = user;
  }
}