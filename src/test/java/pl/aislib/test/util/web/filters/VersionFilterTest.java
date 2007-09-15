package pl.aislib.test.util.web.filters;

import java.io.IOException;
import java.util.regex.PatternSyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.TestCase;

import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import pl.aislib.util.web.filters.VersionFilter;


/**
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.2 $
 */
public class VersionFilterTest extends TestCase {

  private static final String SAMPLE_VERSION = "1234-56";
  private static final String SAMPLE_SVN = "http://svn.server.path:8080/repo/application/path/tags/" + SAMPLE_VERSION + "/and/path/inside/module";

  private MockServletContext servletContext;
  private MockFilterConfig config;
  private Filter filter;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private SimpleChain chain;

  public void setUp() throws Exception {
    servletContext = new MockServletContext();
    config = new MockFilterConfig(servletContext);
    filter = new VersionFilter();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    chain = new SimpleChain();
  }

  /**
   * No config paramter, no request parameter.
   * @throws Exception
   */
  public void testNoParams() throws Exception {
    filter.init(config);
    filter.doFilter(request, response, chain);
    assertTrue("Filter must not handle request", chain.wasExecuted());
  }

  /**
   * Config parameter exists, no request parameter.
   * @throws Exception
   */
  public void testContextParamNoRequestParam() throws Exception {
    servletContext.addInitParameter("app.version", SAMPLE_VERSION);
    filter.init(config);
    filter.doFilter(request, response, chain);
    assertTrue("Filter must not handle request", chain.wasExecuted());
  }

  /**
   * Context param exists, request param exists.
   * @throws Exception
   */
  public void testParams() throws Exception {
    servletContext.addInitParameter("app.version", SAMPLE_VERSION);
    request.addParameter("version", "");
    filter.init(config);
    filter.doFilter(request, response, chain);
    assertFalse("Filter must handle request", chain.wasExecuted());
    String responseContent = response.getContentAsString();
    assertTrue("response must contain expected version", responseContent.indexOf(SAMPLE_VERSION) > 0);
  }

  /**
   * Context param exists, request param exists, regex exists
   * @throws Exception
   */
  public void testRegex() throws Exception {
    servletContext.addInitParameter("app.version", SAMPLE_SVN);
    request.addParameter("version", "");
    config.addInitParameter("pattern", ".*/tags/([-\\w]*)/.*");
    filter.init(config);
    filter.doFilter(request, response, chain);
    assertFalse("Filter must handle request", chain.wasExecuted());
    String responseContent = response.getContentAsString();
    assertEquals("response must contain expected version", "Version: " + SAMPLE_VERSION + "\n", responseContent);
  }

  /**
   * Malformed regex
   * @throws Exception
   */
  public void testMalformedRegex() throws Exception {
    servletContext.addInitParameter("app.version", SAMPLE_SVN);
    request.addParameter("version", "");
    config.addInitParameter("pattern", ".*/tags/.*)");
    try {
      filter.init(config);
      fail("malformed regex do not handled");
    } catch (ServletException se) {
      assertNotNull(PatternSyntaxException.class.getName() +" expected", se.getRootCause());
      assertEquals(PatternSyntaxException.class, se.getRootCause().getClass());
    }
  }

  /**
   * Context param exists, request param exists, one package defined.
   * @throws Exception
   */
  public void testParamsAndPackage() throws Exception {
    String packageName = "java.lang";
    Package patzkage = Package.getPackage(packageName);
    servletContext.addInitParameter("app.version", SAMPLE_VERSION);
    config.addInitParameter("package.lang", packageName);
    request.addParameter("version", "");
    filter.init(config);
    filter.doFilter(request, response, chain);
    assertFalse("Filter must handle request", chain.wasExecuted());
    String responseContent = response.getContentAsString();
    assertTrue("response must contain expected version", responseContent.indexOf(SAMPLE_VERSION) > 0);
    assertTrue("response must contain expected package spec version", responseContent.indexOf(patzkage.getSpecificationVersion()) > 0);
    assertTrue("response must contain expected package impl version", responseContent.indexOf(patzkage.getImplementationVersion()) > 0);
  }

  /**
   * Simple chain implementation, reporting, if chain was executed.
   */
  class SimpleChain implements FilterChain {

    private boolean wasExecuted = false;

    public void doFilter(ServletRequest arg0, ServletResponse arg1) throws IOException, ServletException {
      wasExecuted = true;
    }

    /**
     * @return <code>true</code> if <code>doFilter</code> method was executed.
     */
    public boolean wasExecuted() {
      return wasExecuted;
    }
  }

}
