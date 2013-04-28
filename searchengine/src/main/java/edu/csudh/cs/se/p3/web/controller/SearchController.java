package edu.csudh.cs.se.p3.web.controller;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.csudh.cs.se.p3.domain.Page;
import edu.csudh.cs.se.p3.services.UrlPageService;

@Controller
@RequestMapping(value="/search", method=RequestMethod.GET)
public class SearchController {

    @Inject private UrlPageService urlPageService;
    
    @ResponseBody
    public Collection<Page> search(@PathVariable String urlString){        
        return urlPageService.findPages(urlString);
    }
}
