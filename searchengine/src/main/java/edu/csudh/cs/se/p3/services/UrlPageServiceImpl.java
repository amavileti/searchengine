package edu.csudh.cs.se.p3.services;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import edu.csudh.cs.se.p2.service.IndexSearcher;
import edu.csudh.cs.se.p3.dao.PageDao;
import edu.csudh.cs.se.p3.domain.Page;

/**
 * Service layer to transactionally persist, search url and descriptions
 * @author amavileti
 *
 */
@Named
public class UrlPageServiceImpl implements UrlPageService {

    @Inject private PageDao pageDao;
    private static final Logger LOG = LoggerFactory.getLogger(UrlPageServiceImpl.class);
    
    @Inject
    private IndexSearcher indexSearcher;
    
    
    @Transactional(readOnly=true)
    public Collection<Page> findPages(String url) {
        checkNotNull(url);
        LOG.info("Searching to description {}", url);
        return pageDao.findByDescriptionLikeOrderByRankDesc("%"+url.toLowerCase()+"%");
    }
    
    @Transactional
    public Page saveUrlDescription(String url, String description){
        Map<String, String> toPersist = indexSearcher.rotate(url, description);
        Page p = null;
        for(Map.Entry<String, String> urlDescription : toPersist.entrySet()){
            p = persist(urlDescription.getValue(), urlDescription.getKey());
        }
        return p;
    }
   

    @Transactional(readOnly=true)
    public Page findPageByDescription(String s){
        Collection<Page> pageCollection = pageDao.findByDescriptionLikeOrderByRankDesc(getLikeString(s));
        if(pageCollection.size()>0){
            return pageCollection.iterator().next();
        }else{
            return null;
        }
    }
    
    @Transactional(readOnly=true)
    public Page findPageByUrl(String s){
        Collection<Page> pageCollection = pageDao.findByUrlLikeOrderByRankDesc(getLikeString(s));
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
    
    private String getLikeString(String s){
        return new StringBuilder()
            .append("%")
            .append(s.toLowerCase())
            .append("%")
            .toString();
    }
    
    private Page persist(String url, String description){
        Collection<Page> pages = pageDao.findByDescriptionLikeAndUrlLike(getLikeString(description), getLikeString(url));
        if(pages.size()>0){
            return pages.iterator().next();
        }
        Page p = new Page();
        String transformedUrl = getUrl(url);
        p.setUrl(transformedUrl);
        p.setDescription(getTransformedDescription(description));
        p.setPageName(transformedUrl);
        
        LOG.info("Saving page {}", p);
        
        return pageDao.save(p);

    }
}
