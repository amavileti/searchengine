package edu.csudh.cs.se.p3.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.common.base.Objects;

@Entity
@Table(name="page")
public class Page {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer pageId;
	
	@Column(name="name")
	private String pageName;
	
	@Column(name="url")
	private String url;
	
	@Column(name="parentId")
	@ManyToOne(optional=true)
	private Page parent;

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Page getParent() {
		return parent;
	}

	public void setParent(Page parent) {
		this.parent = parent;
	}

	@Override
	public String toString(){
		return Objects.toStringHelper(this)
			.add("pageId", pageId)
			.add("pageName", pageName)
			.add("url", url)
			.add("parent", parent)
			.toString();
	}
	
	@Override
	public int hashCode(){
		return Objects.hashCode(pageId);
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Page)){
			return false;
		}else if(o == this){
			return true;
		}else{
			Page other = (Page) o;
			return Objects.equal(pageId, other.pageId);
		}
	}
}
