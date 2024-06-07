package com.shadoww.BookLibraryApp.mapper;

import com.shadoww.BookLibraryApp.dto.request.BookCatalogRequest;
import com.shadoww.BookLibraryApp.models.BookCatalog;
import org.mapstruct.Mapper;

@Mapper
public interface BookCatalogMapper {
    BookCatalog dtoToModel(BookCatalogRequest request);
}
