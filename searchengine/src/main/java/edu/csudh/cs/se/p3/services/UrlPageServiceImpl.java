package edu.csudh.cs.se.p3.services;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import edu.csudh.cs.se.p3.dao.PageDao;
import edu.csudh.cs.se.p3.domain.Page;

@Named
public class UrlPageServiceImpl implements UrlPageService {

    @Inject private PageDao pageDao;
    private static final Logger LOG = LoggerFactory.getLogger(UrlPageServiceImpl.class);
    
    @Transactional(readOnly=true)
    public Collection<Page> findPages(String url) {
        checkNotNull(url);
        LOG.info("Searching to description {}", url);
        return pageDao.findByDescriptionLikeOrderByRankDesc("%"+url.toLowerCase()+"%");
    }
    
    @Transactional
    public Page saveUrlDescription(String url, String description){
    	Page p = new Page();
    	String transformedUrl = getUrl(url);
    	p.setUrl(transformedUrl);
    	p.setDescription(getTransformedDescription(description));
    	p.setPageName(transformedUrl);
    	
    	LOG.info("Saving page {}", p);
    	
    	return pageDao.save(p);
    }

    @Transactional(readOnly=true)
    public Page findPageByDescription(String s){
        Collection<Page> pageCollection = pageDao.findByDescriptionLikeOrderByRankDesc("%"+s.toLowerCase()+"%");
        if(pageCollection.size()>0){
            return pageCollection.iterator().next();
        }else{
            return null;
        }
    }
    
    @Transactional(readOnly=true)
    public Page findPageByUrl(String s){
        Collection<Page> pageCollection = pageDao.findByUrlLikeOrderByRankDesc("%"+s.toLowerCase()+"%");
        if(pageCollection.size()>0){
            return pageCollection.iterator().next();
        }else{
            return null;
        }
    }
   
    @Transactional(readOnly=false)
    public Page findByPageId(Integer i){
        return pageDao.findOne(i);
    }
    
    @Transactional(readOnly=false)
    public Page update(Page p){
        return pageDao.save(p);
    }
    
    @Transactional(readOnly=false)
    public void updatePageRank(Integer pageId){
        Page page = findByPageId(pageId);
        page.setRank(page.getRank()+1);
        update(page);
    }
    
    private String getTransformedDescription(String s){
    	String description = s.replace('\n', ' ').replace('\r',' ');
    	if(description.length()>=512){
    		return description.substring(0, 512);
    	}
    	return description;
    }
    
    private String getUrl(String s){
        if(s.length()>256){
            return s.substring(0, 256);
        }
        return s;
    }
}
