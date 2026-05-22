package org.store.vinyl.Server;

import org.store.vinyl.Data.DemoData;
import org.store.vinyl.Model.Vinyl;
import org.store.vinyl.Server.dto.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection implements Runnable
{
  private static final ArrayList<Vinyl> vinyls = new ArrayList<>(DemoData.getVinyls());

  private final Socket socket;
  private final ConnectionPool connectionPool;
  private final ObjectOutputStream outputStream;
  private final ObjectInputStream inputStream;

  public ServerConnection(Socket connectionSocket, ConnectionPool connectionPool){
    this.socket = connectionSocket;
    this.connectionPool = connectionPool;
    try
    {
      outputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
      outputStream.flush();
      inputStream = new ObjectInputStream(connectionSocket.getInputStream());
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public void run()
  {
    try
    {
      while(!socket.isClosed()){
        Object object = inputStream.readObject();
        if(object instanceof GetAllVinylsRequest request){
          handleGetAllVinyls(request);
        }
        else if (object instanceof BorrowVinylRequest request)
        {
          handleBorrowVinyl(request);
        }
        else if (object instanceof ReserveVinylRequest request){
          handleReserveVinyl(request);
        }
        else if (object instanceof ReturnVinylRequest request)
        {
          handleReturnVinyl(request);
        }
        else if (object instanceof DeleteVinylRequest request)
        {
            handleDeleteVinyl(request);
        } else
        {
          System.out.println("[SERVER] Unknown object received");
        }
      }
    }
    catch (EOFException e)
    {
      System.out.println("Client disconnected");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      connectionPool.remove(this);
      try
      {
        socket.close();
      }
      catch (IOException ignored)
      {
      }
    }
  }

  private void handleReturnVinyl(ReturnVinylRequest request)
  {
    synchronized (vinyls)
    {
      Vinyl vinyl = findVinylByTitle(request.getTitle());

      if(vinyl == null || request.getUser() == null)
      {
        return;
      }

      if (request.getUser().getUserId().equals(vinyl.getBorrowedBy()))
      {
        vinyl.returnVinyl();
        connectionPool.broadcast(new VinylUpdatedMessage(vinyl));
      }
    }
  }

  private void handleReserveVinyl(ReserveVinylRequest request)
  {
    synchronized (vinyls)
    {
      Vinyl vinyl = findVinylByTitle(request.getTitle());

      if(vinyl == null || request.getUser() == null)
      {
        return;
      }

      if ("Borrowed".equals(vinyl.getCurrentState().getStateName())
          && vinyl.getReservedBy().isEmpty()
          && !request.getUser().getUserId().equals(vinyl.getBorrowedBy()))
      {
        vinyl.reserve(request.getUser());
        connectionPool.broadcast(new VinylUpdatedMessage(vinyl));
      }
    }
  }

  private void handleGetAllVinyls(GetAllVinylsRequest request)
  {
    synchronized (vinyls)
    {
      send(new GetAllVinylsResponse(new ArrayList<>(vinyls)));
    }
  }

  private Vinyl findVinylByTitle(String title)
  {
    return vinyls.stream()
        .filter(v -> v.getTitle().equals(title))
        .findFirst()
        .orElse(null);
  }

  private void handleBorrowVinyl(BorrowVinylRequest request)
  {
    synchronized (vinyls)
    {
      Vinyl vinyl = findVinylByTitle(request.getTitle());

      if(vinyl == null || request.getUser() == null)
      {
        return;
      }

      String userId = request.getUser().getUserId();
      String state = vinyl.getCurrentState().getStateName();

      if ("Available".equals(state)
          || ("Reserved".equals(state) && userId.equals(vinyl.getReservedBy())))
      {
        vinyl.borrow(request.getUser());
        connectionPool.broadcast(new VinylUpdatedMessage(vinyl));
      }
    }
  }

  private void handleDeleteVinyl(DeleteVinylRequest request)
  {
      synchronized (vinyls)
      {
          Vinyl vinyl = findVinylByTitle(request.getTitle());
          vinyls.removeIf(vinylToDelete ->
                  vinylToDelete.getTitle().equals(request.getTitle())
          );
          send(new DeleteVinylResponse(request.getTitle()));
          connectionPool.broadcast(new VinylUpdatedMessage(vinyl));
      }
  }

  public synchronized void send(Object object){
    try
    {
      outputStream.reset();
      outputStream.writeObject(object);
      outputStream.flush();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}
