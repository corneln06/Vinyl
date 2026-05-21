package org.store.vinyl.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientConnection
{
  private final ObjectOutputStream outputStream;
  private final ObjectInputStream inputStream;

  public ClientConnection(ObjectOutputStream outputStream,
      ObjectInputStream inputStream)
  {
    this.outputStream = outputStream;
    this.inputStream = inputStream;
  }
}
