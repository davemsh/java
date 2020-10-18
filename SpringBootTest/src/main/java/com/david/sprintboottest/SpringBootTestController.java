package com.david.sprintboottest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.david.sprintboottest.datajpa.Word;
import com.david.sprintboottest.datajpa.WordRepository;

@RestController
@RequestMapping("/words")
public class SpringBootTestController {
	@Autowired
	private WordRepository wordRepository;

	/**
	 * Searches the word list for the provided search term and returns the number of
	 * times it appeared.
	 * 
	 * @param searchTerm The word to be searched for.
	 * @return Returns the number of words in the word list that match the given
	 *         search term.
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String searchWord(@RequestParam(name = "searchTerm", required = false) String searchTerm) {
		if (StringUtils.isBlank(searchTerm)) {
			// Throw a 400 Bad Request if search term was missing.
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No search term was provided.");
		} else {
			try {
				// Retrieve words that match the search term and return result.
				List<Word> wordList = wordRepository.findByWordContaining(searchTerm);
				return "Found " + wordList.size() + " words matching the search term \"" + searchTerm + "\".";
			} catch (DataAccessException e) {
				e.printStackTrace();
				// Throw a 500 Internal Server Error if the JPA repository failed
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error.");
			}
		}
	}

	/**
	 * Upload a text file containing a list of words to be added to the word list,
	 * one word per line.
	 * 
	 * @param wordListFile A text file containing a list of words to be added to the
	 *                     word list.
	 * @return Returns the list of newly-added words.
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadWordListFile(
			@RequestParam(name = "wordListFile", required = false) MultipartFile wordListFile) {
		if (wordListFile == null) {
			// Throw a 400 Bad Request if no word list file was uploaded.
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No word list file was uploaded.");
		} else if (!wordListFile.getContentType().equals(MediaType.TEXT_PLAIN.toString())) {
			// Throw a 400 Bad Request if the file type isn't text
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid file type: " + wordListFile.getContentType());
		} else if (wordListFile.getSize() > (5 * 1024 * 1024)) {
			// Throw a 400 Bad Request if the file is bigger than 5 MB.
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size exceeds 5 MB limit.");
		} else {
			try {
				// Create a set of the existing words to ensure duplicate words aren't added to
				// the word list - there's a unique constraint on the "word" column in the
				// database.
				Set<String> wordSet = new HashSet<String>(
						wordRepository.findAll().stream().map(Word::getWord).collect(Collectors.toList()));
				List<String> newWordList = new ArrayList<String>();

				// Parse the incoming word list file and add the word on each line to the new
				// word list.
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(wordListFile.getInputStream()))) {
					while (reader.ready()) {
						String wordString = reader.readLine().trim();

						if (StringUtils.isNotBlank(wordString) && !wordSet.contains(wordString)) {
							wordSet.add(wordString);
							newWordList.add(wordString);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error parsing word list file.");
				}

				// Insert new words, return result.
				wordRepository.saveAll(newWordList.stream().map(Word::new).collect(Collectors.toList()));
				return "Uploaded file " + wordListFile.getOriginalFilename() + ", " + newWordList.size()
						+ " new words were added to the word list.";
			} catch (DataAccessException e) {
				e.printStackTrace();
				// Throw a 500 Internal Server Error if the JPA repository failed
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error.");
			}
		}
	}
}
