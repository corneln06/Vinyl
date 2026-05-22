package org.store.vinyl.Server.dto;
import org.store.vinyl.Model.User;

import java.io.Serializable;

public class DeleteVinylRequest implements Serializable
{
    private final String title;
    private final User user;

    public DeleteVinylRequest(String title, User user)
    {
        this.title = title;
        this.user = user;
    }

    public String getTitle()
    {
        return title;
    }

    public User getUser()
    {
        return user;
    }
}