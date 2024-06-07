package com.shadoww.BookLibraryApp.dto.response;

import com.shadoww.BookLibraryApp.models.BookCatalog;
import lombok.Value;

@Value
public class BookCatalogResponse {

    long id;

    String title;

    boolean isPublic;

    long ownerId;

    public BookCatalogResponse(BookCatalog catalog) {
        this.id = catalog.getId();
        this.isPublic = catalog.getIsPublic();
        this.title = catalog.getTitle();
        this.ownerId = catalog.getOwner().getId();
    }
    public boolean getIsPublic() {
        return isPublic;
    }
}
