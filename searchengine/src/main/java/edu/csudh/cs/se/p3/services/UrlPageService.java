package edu.csudh.cs.se.p3.services;

import java.util.Collection;

import edu.csudh.cs.se.p3.domain.Page;

public interface UrlPageService {

    Collection<Page> findPages(String url);
}
