package edu.csudh.cs.se.p3.job;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.CoreConnectionPNames;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.HttpClientErrorException;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;

import edu.csudh.cs.se.p3.services.UrlPageService;

/***
 * Crawler implemenation that crawl HTTP content, picking up
 * HTTP meta description header and indexes them
 * This system uses BFS (breadth first search) to scan to all pages and 
 * stores that content to database
 * @author amavileti
 *
 */
@Named
public class CrawlWebImpl implements CrawlWeb {

    private static final Logger LOG = LoggerFactory.getLogger(CrawlWebImpl.class);
    private Pattern urlPattern = Pattern.compile("http://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]+[-a-zA-Z0-9+&@#/%=~_|]");
    private static final String EMPTY = "";
    private Set<String> scannedPages = Sets.newHashSet();

    private static final String metaNodeName = "meta";

    @Inject
    private HtmlCleaner htmlCleaner;
    @Inject
    private UrlPageService pageService;
    @Inject
    private HttpClient httpClient;
    
    @PostConstruct
    public void setDefaultParams(){
        //Aggressive settings for home use, but had to use to it to not get stuck for a response
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2*1000);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2*1000);
    }

    /**
     * Primary implementation for crawling. Accepts a url as a starting point, and
     * recursively scans child pages referenced by this content. 
     * Utilizes a Queue as a container to store child urls;
     */
    @Async
    public void doCrawl(String url) throws IOException {
        Queue<String> content = Queues.newArrayDeque();
        content.offer(url);
        while (!content.isEmpty() && scannedPages.size() < 1000) {
            String v = content.remove();
            LOG.info("Scanning {}", v);
            String input = EMPTY;
            try{
                input = getUrlContent(v);
                String metaContent = getMetaContent(input);

                pageService.saveUrlDescription(v, metaContent);
            }catch(Throwable e){
                LOG.error("Unable to index {}", url);
                continue;
            }


            LOG.debug("Got input {} ", input);
            Matcher matcher = urlPattern.matcher(input);
            scannedPages.add(input);
            // find and print all matches
            while (matcher.find()) {
                String w = matcher.group();
                LOG.debug("Scanning {}", w);
                if (!scannedPages.contains(w)) {
                    LOG.debug("Queueing {}", w);
                    content.offer(w);
                    scannedPages.add(w);
                }
            }

        }
    }

    public void crawl() {
        try {
            doCrawl("http://www.csudh.edu");
        } catch (IOException ie) {
            throw new RuntimeException(ie);
        }
    }

    /**
     * Given a URL, establishes connect to target Host, with specified constraints:
     * 1. Timeout of 2 secs
     * 2. Socket timeout of 2 secs
     * Response content type has to be text/html - otherwise system doesn't index them
     * @param url
     * @return
     * @throws IOException
     */
    private String getUrlContent(String url) throws IOException {
            HttpGet get = new HttpGet(url);
            HttpResponse response = httpClient.execute(get);
            
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK || response.getEntity() == null) {
                throw new HttpClientErrorException(org.springframework.http.HttpStatus.PRECONDITION_FAILED);
            }
            Header contentType = response.getEntity().getContentType();
            if (contentType.getValue().contains("text/html")) {
                InputStream is = null;
                try {
                    if (response.getEntity().getContentLength() <= 0l) {
                        throw new HttpClientErrorException(org.springframework.http.HttpStatus.NOT_ACCEPTABLE);
                    }
                    byte[] responseBytes = new byte[(int) response.getEntity().getContentLength()];
    
                    is = response.getEntity().getContent();
                    ByteStreams.readFully(is, responseBytes);
                    return new String(responseBytes, "UTF-8");
                } finally {
                    if(is!=null)
                        is.close();
                }
            } else {
                throw new HttpClientErrorException(org.springframework.http.HttpStatus.PRECONDITION_FAILED);
            }
       
    }

    /**
     * Accepts an entire response pages, parses, and picks Meta description
     * @param description
     * @return
     */
    private String getMetaContent(String description) {
        TagNode tagNode = htmlCleaner.clean(description);
        TagNode[] metaNodes = tagNode.getElementsByAttValue("name", "description", true, false);
        StringBuilder descriptionBuilder = new StringBuilder();
        for (TagNode metaNode : metaNodes) {
            if (metaNodeName.equalsIgnoreCase(metaNode.getName())) {
                if (metaNode.getText() != null && !metaNode.getText().equals(EMPTY))
                    descriptionBuilder.append(metaNode.getText());
                String attributeValue = metaNode.getAttributeByName("content");
                if (attributeValue != null && !attributeValue.equals(EMPTY)) {
                    descriptionBuilder.append(attributeValue);
                }
            }
        }
        // Case where there's no meta content
        if (descriptionBuilder.length() == 0) {
            throw new RuntimeException("No description found");

        }

        return descriptionBuilder.toString();
    }

}
