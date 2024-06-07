package com.shadoww.BookLibraryApp.services;


import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.repositories.ChaptersRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ChaptersService {
    private final ChaptersRepository chaptersRepository;



    @Autowired
    public ChaptersService(ChaptersRepository chaptersRepository) {
        this.chaptersRepository = chaptersRepository;
    }



    public List<Chapter> findChapters() {
        return chaptersRepository.findAll();
    }


    public Chapter findByBookAndNumber(Book book, int number) {
        return chaptersRepository.findChaptersByBookAndNumberOfPage(book, number).orElseThrow(()->new EntityNotFoundException("Такого розділу не існує"));

    }

    public Chapter findFirstChapterByBook(Book book) {
        return chaptersRepository.findChapterByBookAndNumberOfPage(book, 1).orElseThrow(()->new EntityNotFoundException("Такого розділу не існує"));
    }

    public List<Chapter> findChaptersByBook(Book book) {
        List<Chapter> chapters  = chaptersRepository.findAllByBookId(book, Sort.by(Sort.Direction.ASC, "numberOfPage"));
         Collections.reverse(chapters);
         return chapters;
    }

    public List<Chapter> findByBook(Book book) {

        return chaptersRepository.findChaptersByBook(book, Sort.by(Sort.Direction.ASC, "numberOfPage"));
    }
    public Chapter readById(int id) {
        return chaptersRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Такого розділу не існує"));
    }

    @Transactional
    public Chapter create(Chapter chapter) {
        if (chapter == null) {
            throw new NullPointerException("Розділ книги не може бути пустим");
        }

        return save(chapter);
    }

    @Transactional
    public Chapter save(Chapter c) {
        return chaptersRepository.save(c);
    }

    @Transactional
    public void update(Chapter updated) {
        Chapter forUpdate = readById(updated.getId());


        save(updated);
        //            forUpdate.get().setTitle(updated.getTitle());
        //            forUpdate.get().setText(updated.getText());
        //            forUpdate.get().setNumberOfPage(updated.getNumberOfPage());
        //            save(forUpdate.get());
    }

    @Transactional
    public void update(Chapter updated, Chapter toUpdated) {
        toUpdated.setTitle(updated.getTitle());
        toUpdated.setText(updated.getText());
        toUpdated.setNumberOfPage(updated.getNumberOfPage());
        save(toUpdated);
    }

    @Transactional
    public void deleteById(int id) {
        Chapter chapter = readById(id);

        delete(chapter);
    }

    @Transactional
    public void delete(Chapter forDelete) {
        chaptersRepository.delete(forDelete);
    }


    @Transactional
    public void deleteByBook(int id) {
        chaptersRepository.deleteChaptersByBook_Id(id);
    }
}
