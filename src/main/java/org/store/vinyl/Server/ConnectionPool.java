package org.store.vinyl.Server;

import java.util.ArrayList;
import java.util.List;

public class ConnectionPool
{
  private final List<ServerConnection> connections = new ArrayList<>();

  public synchronized void add(ServerConnection serverConnection){
    connections.add(serverConnection);
  }

  public synchronized void remove(ServerConnection serverConnection){
    connections.remove(serverConnection);
  }

  public synchronized void broadcast(Object object){
    List<ServerConnection> disconnected = new ArrayList<>();

    for(ServerConnection connection : connections){
      try
      {
        connection.send(object);
      }
      catch (Exception e)
      {
        disconnected.add(connection);
      }
    }

    connections.removeAll(disconnected);
  }
}
