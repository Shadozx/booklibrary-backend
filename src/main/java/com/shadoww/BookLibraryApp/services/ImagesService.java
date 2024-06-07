package com.shadoww.BookLibraryApp.services;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.images.BookImage;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import com.shadoww.BookLibraryApp.models.images.Image;
import com.shadoww.BookLibraryApp.repositories.BooksRepository;
import com.shadoww.BookLibraryApp.repositories.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ImagesService {

    private ImagesRepository imagesRepository;
    private ChaptersService chaptersService;

    private final BooksRepository booksRepository;


    @Autowired
    public ImagesService(ImagesRepository imagesRepository, ChaptersService chaptersService, BooksRepository booksRepository) {
        this.imagesRepository = imagesRepository;
        this.chaptersService = chaptersService;
        this.booksRepository = booksRepository;

    }


//    public List<Image> findAll() {
//        return imagesRepository.findAll();
//    }
//    @Transactional
//    public void save(BookImage image) {
//        if(image != null) imagesRepository.save(image);
//        else System.out.println("Image is null!!!");
//    }
//
    @Transactional
    public void save(Image image) {
        imagesRepository.save(image);
    }


//    @Transactional
//    public void save(ChapterImage image) {
//        imagesRepository.save(image);
//    }

    @Transactional
    public void saveChapterImages(List<ChapterImage> images) {
        if(images !=null && !images.isEmpty())
            for (var image : images)
                save(image);
    }

//
//    @Transactional
//    public void save(Book book, List<Image> images) {
//
//        if(images != null && !images.isEmpty()) {
//            for(int i = 1; i <= images.size(); i++) {
//                Image image = images.get(i-1);
////                image.setChapter(book, i);
//                System.out.println("Image " + i + ": " + image);
//                save(image);
//            }
//        }
//    }

    public Optional<Image> findImageByFilename(String filename) {
        return imagesRepository.findImageByFilename(filename);
    }
//    public Optional<Image> findBookImageByFileName(String filename) {
//        return bookImagesRepository.findByFilename(filename);
//    }
//
//    public Optional<ChapterImage> findChapterImageByFileName(String filename) {
//        return chapterImagesRepository.findByFilename(filename);
//    }


//    @Transactional
//    public void delete(Image image) {
//        imagesRepository.delete(image);
//    }
//
//    @Transactional
//    public void deleteBookImageByFilename(String filename) {
//        bookImagesRepository.deleteByFilename(filename);
//    }
//    @Transactional
//    public void deleteChapterImageByFilename(String filename) {
//        chapterImagesRepository.deleteByFilename(filename);
//    }



    @Transactional
    public void delete(Image image) {
        imagesRepository.delete(image);
    }


    @Transactional
    public void deleteByFilename(String filename) {
        imagesRepository.deleteImageByFilename(filename);
    }

    @Transactional
    public void deleteChapterImages(List<ChapterImage> images) {
        if(images !=null && !images.isEmpty())
            for (var image : images)
                delete(image);
    }

    @Transactional
    public void deleteChaptersImages(List<Chapter> chapters) {
        if(chapters !=null && !chapters.isEmpty())
            for (var c : chapters)
                deleteChapterImages(c.getImages());
    }

    @Transactional
    public void deleteByBook(int id) {
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent()) {
            delete(book.get().getBookImage());

            List<Chapter> chapters = chaptersService.findChaptersByBook(book.get());

            if(!chapters.isEmpty()) chapters.forEach(c->deleteChapterImages(c.getImages()));
        }
    }

}
