package com.shadoww.BookLibraryApp.repository.comment;

import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.model.comment.ChapterComment;
import com.shadoww.BookLibraryApp.model.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterCommentsRepository extends JpaRepository<ChapterComment, Long> {

    List<ChapterComment> findByChapter(Chapter chapter);

    List<ChapterComment> findByChapterAndOwner(Chapter chapter, Person owner);
}
