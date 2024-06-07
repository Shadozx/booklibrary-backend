package com.shadoww.BookLibraryApp.mapper.impl;

import com.shadoww.BookLibraryApp.dto.request.BookCatalogRequest;
import com.shadoww.BookLibraryApp.mapper.BookCatalogMapper;
import com.shadoww.BookLibraryApp.models.BookCatalog;
import org.springframework.stereotype.Component;

@Component
public class BookCatalogMapperImpl implements BookCatalogMapper {
    @Override
    public BookCatalog dtoToModel(BookCatalogRequest request) {
        BookCatalog catalog = new BookCatalog();

        catalog.setTitle(request.getTitle());
        catalog.setIsPublic(request.getIsPublic());

        return catalog;
    }
}
