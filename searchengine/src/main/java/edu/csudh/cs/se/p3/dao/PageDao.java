package edu.csudh.cs.se.p3.dao;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import edu.csudh.cs.se.p3.domain.Page;

public interface PageDao extends CrudRepository<Page, Integer>{

    Collection<Page> findByDescriptionLikeOrderByRankDesc(String s);
    
    Collection<Page> findByUrlLikeOrderByRankDesc(String s);
}
