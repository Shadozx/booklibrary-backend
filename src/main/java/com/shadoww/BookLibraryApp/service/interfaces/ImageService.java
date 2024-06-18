package com.shadoww.BookLibraryApp.service.interfaces;

import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.model.image.ChapterImage;
import com.shadoww.BookLibraryApp.model.image.Image;
import com.shadoww.BookLibraryApp.service.CrudService;

import java.util.List;


public interface ImageService extends CrudService<Image, Long> {

    void createChapterImages(List<ChapterImage> images);

    Image getImageByFilename(String filename);

    void deleteByFilename(String filename);

    void deleteChapterImages(List<ChapterImage> images);


    void deleteChaptersImages(List<Chapter> chapters);

    void deleteBookImages(Long bookId);
}
