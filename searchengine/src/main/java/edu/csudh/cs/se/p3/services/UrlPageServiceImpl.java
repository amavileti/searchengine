package edu.csudh.cs.se.p3.services;

import static com.google.common.base.Preconditions.*;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.csudh.cs.se.p3.dao.PageDao;
import edu.csudh.cs.se.p3.domain.Page;

@Named
public class UrlPageServiceImpl implements UrlPageService {

    @Inject private PageDao pageDao;
    private static final Logger LOG = LoggerFactory.getLogger(UrlPageServiceImpl.class);
    
    public Collection<Page> findPages(String url) {
        checkNotNull(url);
        LOG.info("Searching to description {}", url);
        return pageDao.findByDescription(url.toLowerCase());
    }

}
