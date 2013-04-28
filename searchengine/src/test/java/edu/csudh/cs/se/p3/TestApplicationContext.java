package edu.csudh.cs.se.p3;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockRequestDispatcher;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class TestApplicationContext {
    private static final String APPLICATION_CONTEXT = "file:src/main/webapp/WEB-INF/applicationContext*.xml";

    @Test
    public void validateApplicationContext() {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT);
            assertThat(context).isNotNull();
            context.close();
    }

    @Test
    public void validateServletContext() {
            List<String> list = Arrays.asList(APPLICATION_CONTEXT, "file:src/main/webapp/WEB-INF/searchengine-servlet.xml");
            XmlWebApplicationContext context = new XmlWebApplicationContext();
            context.setConfigLocations(list.toArray(new String[list.size()]));
            context.setServletContext(new MockServletContext("file:src/main/webapp/WEB-INF") {
                    @Override
                    public RequestDispatcher getNamedDispatcher(String path) {
                            if ("default".equalsIgnoreCase(path)) {
                                    return new MockRequestDispatcher(path);
                            } else {
                                    return super.getRequestDispatcher(path);
                            }
                    }
            });
            context.refresh();

            assertThat(context).isNotNull();
            context.close();
    }
}