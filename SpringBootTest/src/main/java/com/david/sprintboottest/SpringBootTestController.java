package com.david.sprintboottest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/words")
public class SpringBootTestController {
	/**
	 * Searches the word list for the provided search term and returns the number of
	 * times it appeared.
	 * 
	 * @param searchTerm
	 * @return Returns the number of words in the word list that match the given
	 *         search term.
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String searchWord(@RequestParam(name = "search", required = false) String searchTerm) {
		if (StringUtils.isNotBlank(searchTerm)) {
			// Return result TODO
			return "Searching for " + searchTerm;
		} else {
			// Throw error TODO
			return "No search term provided.";
		}
	}

//	/**
//	 * Adds a word to the word list.
//	 * 
//	 * @param newWord The new word to be added to the database
//	 * @return Returns the newly-added word.
//	 */
//	@RequestMapping(method = RequestMethod.POST)
//	public String addWord(@RequestParam(name = "new") String newWord) {
//
//		// Return the newly-added word if successful TODO
//		return "Adding word ";
//	}
//
//	/**
//	 * Upload a CSV file containing a list of words to be added to the word list.
//	 * 
//	 * @return Returns the list of newly added words.
//	 */
//	@RequestMapping(value = "/upload", method = RequestMethod.POST)
//	public String upload() {
//
//		// Return the list of newly-added words if successful TODO
//		return "Uploading file";
//	}
}
