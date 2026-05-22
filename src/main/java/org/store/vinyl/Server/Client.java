package org.store.vinyl.Server;

import javafx.application.Platform;
import org.store.vinyl.Server.dto.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class Client
{
  private Socket socket;
  private ObjectOutputStream outputStream;
  private ObjectInputStream inputStream;

  private Consumer<GetAllVinylsResponse> getAllVinylsListener;
  private Consumer<ReserveVinylResponse> reserveVinylListener;
  private Consumer<BorrowVinylResponse> borrowVinylListener;
  private Consumer<ReturnVinylResponse> returnVinylListener;
  private Consumer<VinylUpdatedMessage> vinylUpdatedListener;
  private Consumer<DeleteVinylResponse> deleteVinylListener;

  public void connect() throws IOException{
//    socket = new Socket("10.154.198.25", 2910); use this to connect to someone else branch
    socket = new Socket("localHost", 2910);

    outputStream = new ObjectOutputStream(socket.getOutputStream());
    outputStream.flush();
    inputStream = new ObjectInputStream(socket.getInputStream());
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

  public void startListening(){
    Thread listenerThread = new Thread(() ->
    {
      try
      {
        while (!socket.isClosed()){
          Object object = inputStream.readObject();

          Platform.runLater(() -> {
            if (object instanceof GetAllVinylsResponse response && getAllVinylsListener != null ){
              getAllVinylsListener.accept(response);
            } else if(object instanceof ReserveVinylResponse response && reserveVinylListener != null){
              reserveVinylListener.accept(response);
            }
            else if (object instanceof BorrowVinylResponse response && borrowVinylListener != null)
            {
              borrowVinylListener.accept(response);
            }
            else if (object instanceof ReturnVinylResponse response && returnVinylListener != null)
            {
              returnVinylListener.accept(response);
            }
            else if (object instanceof VinylUpdatedMessage message && vinylUpdatedListener != null)
            {
              vinylUpdatedListener.accept(message);
            }
            else if (object instanceof DeleteVinylResponse message && deleteVinylListener != null)
            {
                deleteVinylListener.accept(message);
            }
          });
        }
      }
      catch (EOFException e)
      {
        System.out.println("Server closed the connection");
      }
      catch (IOException | ClassNotFoundException e)
      {
        e.printStackTrace();
      }
    });
    listenerThread.setDaemon(true);
    listenerThread.start();
  }

  public void setGetAllVinylsListener(Consumer<GetAllVinylsResponse> listener){
    this.getAllVinylsListener = listener;
  }
  public void setReserveVinylListener(Consumer<ReserveVinylResponse> listener){
    this.reserveVinylListener = listener;
  }
  public void setBorrowVinylListener(Consumer<BorrowVinylResponse> listener){
    this.borrowVinylListener = listener;
  }
  public void setReturnVinylListener(Consumer<ReturnVinylResponse> listener){
    this.returnVinylListener = listener;
  }
  public void setVinylUpdatedListener(Consumer<VinylUpdatedMessage> listener){
    this.vinylUpdatedListener = listener;
  }
  public void setDeleteVinylListener(Consumer<DeleteVinylResponse> listener){ this.deleteVinylListener = listener; }
}
