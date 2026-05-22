package org.store.vinyl.Server.dto;
import java.io.Serializable;

public class DeleteVinylResponse implements Serializable
{
    private final String title;

    public DeleteVinylResponse(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
}