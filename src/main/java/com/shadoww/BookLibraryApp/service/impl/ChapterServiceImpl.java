package com.shadoww.BookLibraryApp.service.impl;


import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.repository.ChaptersRepository;
import com.shadoww.BookLibraryApp.service.interfaces.ChapterService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChapterServiceImpl implements ChapterService {
    private final ChaptersRepository chaptersRepository;

    @Autowired
    public ChapterServiceImpl(ChaptersRepository chaptersRepository) {
        this.chaptersRepository = chaptersRepository;
    }

    @Override
    @Transactional
    public Chapter create(Chapter chapter) {

        checkIsChapterNull(chapter);

        return save(chapter);
    }

    @Override
    public Chapter readById(Long id) {
        return chaptersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Такого розділу не існує"));
    }

    @Override
    public Chapter getChapterByBookAndNumber(Book book, int number) {
        return chaptersRepository.findChapterByBookAndChapterNumber(book, number)
                .orElseThrow(() -> new EntityNotFoundException("Такого розділу не існує"));
    }

    @Override
    public List<Chapter> getBookChapters(Book book) {
        List<Chapter> chapters = chaptersRepository.findAllByBookId(book, Sort.by(Sort.Direction.ASC, "chapterNumber"));
        Collections.reverse(chapters);
        return chapters;
    }

    @Override
    @Transactional
    public Chapter update(Chapter updated) {

        checkIsChapterNull(updated);

        readById(updated.getId());

        return save(updated);
    }

    @Override
    public boolean existsById(Long id) {
        return chaptersRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        delete(readById(id));
    }

    @Override
    public long count() {
        return chaptersRepository.count();
    }

    @Override
    public List<Chapter> getAll() {
        return chaptersRepository.findAll();
    }

    @Transactional
    Chapter save(Chapter c) {

        checkIsChapterNull(c);
        return chaptersRepository.save(c);
    }

    @Transactional
    void delete(Chapter chapter) {

        checkIsChapterNull(chapter);
        chaptersRepository.delete(chapter);
    }


    @Transactional
    public void deleteByBook(long id) {
        chaptersRepository.deleteChaptersByBook_Id(id);
    }

    private void checkIsChapterNull(Chapter chapter) {
        if (chapter == null) {
            throw new NullPointerException("Розділ книги не може бути пустим");
        }
    }
}
