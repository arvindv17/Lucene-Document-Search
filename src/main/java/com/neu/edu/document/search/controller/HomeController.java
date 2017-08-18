/*
 * @Author - Arvind
 * Created on - 08/17/2017
 * Class Description- This class is the home controller for the request response mapping for the web application.
 * Request comes in from the button click and redirects it to the corresponding JSP page. * 
 * 
 * */

package com.neu.edu.document.search.controller;

import java.nio.file.Paths;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.neu.edu.document.search.highlight.SearchHighlighter;
import com.neu.edu.document.search.dao.HighlightInformation;

@Controller
public class HomeController {

	private static final String ERROR_PAGE = "error";
	private static int counter = 0;
	private static final String VIEW_PAGE = "index";
	private static final String SEARCHED_PAGE = "searchResult";
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(ModelMap model) {
		
		/*model.addAttribute("message", "Welcome");
		model.addAttribute("counter", ++counter);
		*/

		return VIEW_PAGE;

	}

	@RequestMapping(value = "/searchWords.htm", method = RequestMethod.GET)
	public String getSearchedWords(ModelMap model, HttpServletRequest req) throws Exception {
		ArrayList<HighlightInformation> resultsFetched = new ArrayList<HighlightInformation>();

		logger.debug("[welcomeName] search : {}", counter);
		String INDEX_DIR = "indexedFiles";

		ArrayList<HighlightInformation> displayData = SearchHighlighter.getDoc(req.getParameter("searchWord"),
				resultsFetched);

		if (displayData.size() != 0) {
			logger.debug("search highlight", SEARCHED_PAGE);
			if (displayData.size() > 0) {
				HttpSession session = req.getSession();
				session.setAttribute("foundResults", displayData);

				return SEARCHED_PAGE;
			} else {
				return ERROR_PAGE;
			}
		}
		return SEARCHED_PAGE;

	}

}