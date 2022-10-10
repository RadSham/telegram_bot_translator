package com.example.telegbot.repository;
import com.example.telegbot.model.Word;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
    @Query("SELECT w FROM Word w WHERE w.name = :name ORDER BY w.id")
    List<Word> getAllByName(@Param("name") String name);

    @Query("SELECT w FROM Word w WHERE w.nameTwo = :nameTwo ORDER BY w.id")
    List<Word> getAllByNameTwo(@Param("nameTwo") String nameTwo);
}
