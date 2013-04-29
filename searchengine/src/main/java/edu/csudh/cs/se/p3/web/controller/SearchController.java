package edu.csudh.cs.se.p3.web.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import edu.csudh.cs.se.p3.domain.Page;
import edu.csudh.cs.se.p3.job.CrawlWeb;
import edu.csudh.cs.se.p3.services.UrlPageService;

@Controller
public class SearchController {

    @Inject private UrlPageService urlPageService;
    @Inject private CrawlWeb crawlWeb;
    
    @ResponseBody
    @RequestMapping(value="/search/{searchString}", consumes="*/*", method=RequestMethod.GET,
    		produces = "application/json; charset=utf-8")
    public Collection<Page> search(@PathVariable String searchString){        
        Collection<Page> response = urlPageService.findPages(searchString);
        return response;
    }
    
    @ResponseBody
    @RequestMapping(value="/crawl/{url}", method=RequestMethod.GET)
    public Map<String, String> crawl(@PathVariable String url) throws IOException{
    	crawlWeb.doCrawl("http://www.csudh.edu");
    	return ImmutableMap.of("success", "true");
    }
    
    @ExceptionHandler(IOException.class)
    public Map<String, String> onIOException(){
    	return ImmutableMap.of("success", "false");
    }
}
