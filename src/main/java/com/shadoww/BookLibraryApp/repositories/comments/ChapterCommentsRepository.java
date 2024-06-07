package com.shadoww.BookLibraryApp.repositories.comments;

import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.comments.ChapterComment;
import com.shadoww.BookLibraryApp.models.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterCommentsRepository extends JpaRepository<ChapterComment, Integer> {

    List<ChapterComment> findByChapter(Chapter chapter);

    List<ChapterComment> findByChapterAndOwner(Chapter chapter, Person owner);
}
