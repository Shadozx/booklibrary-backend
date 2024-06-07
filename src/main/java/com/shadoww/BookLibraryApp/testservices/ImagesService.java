package com.shadoww.BookLibraryApp.testservices;

import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import com.shadoww.BookLibraryApp.models.images.Image;

import java.util.List;
import java.util.Optional;

public interface ImagesService {

    void save(Image image);

    void saveChapterImages(List<ChapterImage> images);

    Optional<Image> findImageByFilename(String filename);

    void delete(Image image);

    void deleteByFilename(String filename);

    void deleteChapterImages(List<ChapterImage> images);

    void deleteChaptersImages(List<Chapter> chapters);

    void deleteByBook(int id);
}
