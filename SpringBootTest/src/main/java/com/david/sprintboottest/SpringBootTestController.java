package com.david.sprintboottest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
			// Retrieve words that match the search term.
			List<Word> wordList = wordRepository.findByWordContaining(searchTerm);
			return "Found " + wordList.size() + " words matching the search term \"" + searchTerm + "\".";
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
		} else {
			// Parse the uploaded file to retrieve list of new words TODO

			// Return the list of newly-added words TODO
			return "Uploading file " + wordListFile.getOriginalFilename();
		}
	}
}
