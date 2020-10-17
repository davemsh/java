package com.david.sprintboottest.datajpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {
	// By having the keyword "containing" in the method name, JPA creates an
	// implicit LIKE clause for the word column.
	public List<Word> findByWordContaining(String word);
}
