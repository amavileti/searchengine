package edu.csudh.cs.se.p3.web.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.csudh.cs.se.p3.domain.Page;
import edu.csudh.cs.se.p3.services.UrlPageService;

@Controller
public class AdminController {

    @Inject UrlPageService urlPageService;
    
    @RequestMapping(value="/admin/sdesc/{description:.+}", method=RequestMethod.GET)
    @ResponseBody
    public Page loadPageByDescription(@PathVariable String description){
        return urlPageService.findPageByDescription(description);
    }
    
    @RequestMapping(value="/admin/surl/{url:.+}", method=RequestMethod.GET)
    @ResponseBody
    public Page loadPageByUrl(@PathVariable String url){
        return urlPageService.findPageByUrl(url);
    }
    
    @RequestMapping(value="/admin/insert/{url}/{description:.+}", method=RequestMethod.GET)
    @ResponseBody
    public Page savePage(@PathVariable String url, @PathVariable String description){
        return urlPageService.saveUrlDescription(url, description);
    }
    
    @RequestMapping(value="/admin/update", method=RequestMethod.GET)
    @ResponseBody
    public Page updatePage(@RequestParam Integer pageId, @RequestParam String url, 
            @RequestParam String description){
        Page page = urlPageService.findByPageId(pageId);
        if(page != null){
            page.setUrl(url);
            page.setDescription(description);
            return urlPageService.update(page);
        }else{
            return urlPageService.saveUrlDescription(url, description);
        }
        
    }
}
