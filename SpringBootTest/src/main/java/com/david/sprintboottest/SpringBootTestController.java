package com.david.sprintboottest;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/words")
public class SpringBootTestController {
	// Define content types that could contain CSV data
	private static final List<String> TEXT_CONTENT_TYPES = Arrays.asList(new String[] { "text/plain", "text/csv" });

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
		if (StringUtils.isNotBlank(searchTerm)) {
			// Return result TODO
			return "Searching for " + searchTerm;
		} else {
			// Throw a 400 Bad Request if search term was missing.
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No search term was provided.");
		}
	}

	/**
	 * Adds a word to the word list.
	 * 
	 * @param newWord The new word to be added to the database.
	 * @return Returns the newly-added word.
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String addWord(@RequestParam(name = "newWord", required = false) String newWord) {
		if (StringUtils.isNotBlank(newWord)) {
			// Return the newly-added word if successful TODO
			return "Adding word " + newWord;
		} else {
			// Throw a 400 Bad Request if new word was missing.
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No word to add was provided.");
		}
	}

	/**
	 * Upload a CSV text file containing a list of words to be added to the word
	 * list.
	 * 
	 * @param wordListFile A CSV text file containing a list of words to be added to
	 *                     the word list. Example: apple,banana,pear,pineapple
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadWordListFile(
			@RequestParam(name = "wordListFile", required = false) MultipartFile wordListFile) {
		if (wordListFile == null) {
			// Throw a 400 Bad Request if no word list file was uploaded.
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No word list file was uploaded.");
		} else if (!TEXT_CONTENT_TYPES.contains(wordListFile.getContentType())) {
			// Throw a 400 Bad Request if the file type isn't appropriate.
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid file type: " + wordListFile.getContentType());
		} else {
			// Parse the uploaded file to retrieve list of new words TODO

			// Return the list of newly-added words if successful TODO
			return "Uploading file " + wordListFile.getOriginalFilename();
		}
	}
}
