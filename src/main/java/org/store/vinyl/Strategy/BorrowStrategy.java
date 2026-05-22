package org.store.vinyl.Strategy;

import org.store.vinyl.Model.Vinyl;
import org.store.vinyl.Services.VinylsService;

public class BorrowStrategy implements VinylActionStrategy
{
    @Override
    public void execute(Vinyl vinyl, VinylsService service)
    {
        service.borrowVinyl(vinyl.getTitle());
    }
}
