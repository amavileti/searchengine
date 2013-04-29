package edu.csudh.cs.se.p3.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.eq;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import edu.csudh.cs.se.p1.applet.KWRotator;
import edu.csudh.cs.se.p2.repository.UrlRepository;
import edu.csudh.cs.se.p2.service.IndexSearchMapImplKw;
import edu.csudh.cs.se.p2.service.IndexSearcher;
import edu.csudh.cs.se.p3.dao.PageDao;
import edu.csudh.cs.se.p3.domain.Page;



@RunWith(MockitoJUnitRunner.class)
public class TestUrlPageService {

    @InjectMocks private UrlPageServiceImpl service;
    
    @Mock private PageDao pageDao;
    
    private IndexSearcher indexSearcher;
    
    private KWRotator rotator;
    
    @Mock private UrlRepository repository;
    
    
    
    @Before
    public void setUp(){
        rotator = new KWRotator();
        indexSearcher = new IndexSearchMapImplKw(repository, rotator);
        ReflectionTestUtils.setField(service, "indexSearcher", indexSearcher);
    }
    
    @Test
    public void testSaveUrlDescription(){
        String url = "url1";
        String description = "term1 term2";
        service.saveUrlDescription(url, description);
        verify(pageDao, times(2)).save((Page) anyObject());
    }

    @Test
    public void serviceUrlDescriptionWithNoiseWords(){
        String url = "url1";
        String description = "the term1 term2";
        service.saveUrlDescription(url, description);
        verify(pageDao, times(2)).save((Page) anyObject());
    }
}
