package edu.csudh.cs.se.p3.job;

import java.io.IOException;

public interface CrawlWeb {

	void crawl();
	
	void doCrawl(String s) throws IOException;
}
