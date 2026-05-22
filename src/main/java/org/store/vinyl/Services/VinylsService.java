package org.store.vinyl.Services;

import org.store.vinyl.Model.User;
import org.store.vinyl.Server.Client;
import org.store.vinyl.Server.dto.*;

import java.util.function.Consumer;

public class VinylsService
{
  private final Client client;
  private final User currentUser;

  public VinylsService(Client client, User currentUser)
  {
    this.client = client;
    this.currentUser = currentUser;
  }

  public User getCurrentUser()
  {
    return currentUser;
  }

  public void getAllVinylsFromList(GetAllVinylsRequest request){
    client.send(request);
  }
  public void reserveVinyl(String title){
    client.send(new ReserveVinylRequest(title, currentUser));
  }
  public void borrowVinyl(String title){
    client.send(new BorrowVinylRequest(title, currentUser));
  }
  public void deleteVinyl(String title){
      client.send(new DeleteVinylRequest(title, currentUser));
  }
  public void returnVinyl(String title){
    client.send(new ReturnVinylRequest(title, currentUser));
  }
  public void onGetAllVinyls(Consumer<GetAllVinylsResponse> listener){
    client.setGetAllVinylsListener(listener);
  }
  public void onReserveVinyl(Consumer<ReserveVinylResponse> listener){
    client.setReserveVinylListener(listener);
  }
  public void onBorrowVinyl(Consumer<BorrowVinylResponse> listener){
    client.setBorrowVinylListener(listener);
  }
  public void onReturnVinyl(Consumer<ReturnVinylResponse> listener){
    client.setReturnVinylListener(listener);
  }
  public void onVinylUpdated(Consumer<VinylUpdatedMessage> listener){
    client.setVinylUpdatedListener(listener);
  }
  public void onVinylDelete(Consumer<DeleteVinylResponse> listener){ client.setDeleteVinylListener(listener);}

}
