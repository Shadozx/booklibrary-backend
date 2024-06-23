package com.shadoww.BookLibraryApp.service.impl;

import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.model.image.ChapterImage;
import com.shadoww.BookLibraryApp.model.image.Image;
import com.shadoww.BookLibraryApp.repository.image.ImagesRepository;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import com.shadoww.BookLibraryApp.service.interfaces.ChapterService;
import com.shadoww.BookLibraryApp.service.interfaces.ImageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {

    private final BookService bookService;

    private final ChapterService chapterService;

    private final ImagesRepository imagesRepository;

    @Autowired
    public ImageServiceImpl(BookService bookService,
                            ChapterServiceImpl chapterService,
                            ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
        this.chapterService = chapterService;
        this.bookService = bookService;

    }

    @Override
    @Transactional
    public Image create(Image image) {

        checkIsImageNull(image);

        return save(image);
    }

    @Override
    @Transactional
    public void createChapterImages(List<ChapterImage> images) {
        if (images != null && !images.isEmpty()) {
            for (var image : images) {
                create(image);
            }
        }
    }

    @Override
    public Image readById(Long id) {
        return imagesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Немає такої фотографії"));
    }

    @Override
    public boolean existsById(Long id) {
        return imagesRepository.existsById(id);
    }

    @Override
    public Image getImageByFilename(String filename) {
        return imagesRepository.findImageByFilename(filename).orElseThrow(() -> new EntityNotFoundException("Немає такої фотографії"));
    }

    @Override
    public Image update(Image updatedImage) {
        checkIsImageNull(updatedImage);

        readById(updatedImage.getId());

        return save(updatedImage);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        delete(readById(id));
    }

    @Override
    public long count() {
        return imagesRepository.count();
    }

    @Override
    @Transactional
    public void deleteByFilename(String filename) {
        imagesRepository.deleteImageByFilename(filename);
    }

    @Override
    @Transactional
    public void deleteChapterImages(List<ChapterImage> images) {
        if (images != null && !images.isEmpty())
            for (var image : images)
                delete(image);
    }

    @Override
    @Transactional
    public void deleteChaptersImages(List<Chapter> chapters) {
        if (chapters != null) {
            for (var c : chapters) {
                deleteChapterImages(c.getImages());
            }
        }
    }

    @Override
    @Transactional
    public void deleteBookImages(Long bookId) {
        Book book = bookService.readById(bookId);

        delete(book.getBookImage());

        List<Chapter> chapters = chapterService.getBookChapters(book);

        if (!chapters.isEmpty()) {
            chapters.forEach(c -> deleteChapterImages(c.getImages()));
        }
    }

    @Override
    public List<Image> getAll() {
        return imagesRepository.findAll();
    }

    @Transactional
    Image save(Image image) {

        checkIsImageNull(image);

        return imagesRepository.save(image);
    }

    @Transactional
    void delete(Image image) {
        checkIsImageNull(image);

        imagesRepository.delete(image);
    }

    private void checkIsImageNull(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Фотографія не може бути пустою");
        }
    }
}