package edu.csudh.cs.se.p3.dao;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import edu.csudh.cs.se.p3.domain.Page;

public interface PageDao extends CrudRepository<Page, Integer>{

    @Query("from Page where lower(description) like ?1")
    Collection<Page> findByDescription(String s);
}
