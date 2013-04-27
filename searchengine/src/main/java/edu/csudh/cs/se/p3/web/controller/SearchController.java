package edu.csudh.cs.se.p3.web.controller;



import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
@RequestMapping(value="/search", method=RequestMethod.GET)
public class SearchController {

    
    @ResponseBody
    public List<String> search(@PathVariable String urlString){
        return Lists.newArrayList();
    }
}
