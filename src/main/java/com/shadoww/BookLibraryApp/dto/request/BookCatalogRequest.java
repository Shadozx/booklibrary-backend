package com.shadoww.BookLibraryApp.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class BookCatalogRequest {

    private String title;

    private boolean isPublic = true;

    @Override
    public String toString() {
        return "CatalogForm{" +
                "title='" + title + '\'' +
                ", isPublic=" + isPublic +
                '}';
    }


    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean getIsPublic() {
        return this.isPublic;
    }


    public boolean isTitleEmpty() {
        return title == null || title.equals("");
    }
}
