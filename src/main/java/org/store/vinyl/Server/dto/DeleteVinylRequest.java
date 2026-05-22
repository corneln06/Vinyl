package org.store.vinyl.Server.dto;
import java.io.Serializable;

public class DeleteVinylRequest implements Serializable
{
    private final String title;

    public DeleteVinylRequest(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
}