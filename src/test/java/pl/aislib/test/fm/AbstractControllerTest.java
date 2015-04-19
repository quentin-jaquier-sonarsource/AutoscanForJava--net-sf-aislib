package pl.aislib.test.fm;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.apache.commons.logging.impl.SimpleLog;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import pl.aislib.fm.Application;
import pl.aislib.fm.Controller;
import pl.aislib.fm.FmEntityResolver;
import pl.aislib.fm.PageSelector;
import pl.aislib.fm.Workflow;
import pl.aislib.util.xml.XMLUtils;

/**
 * Base class for test cases of {@link pl.aislib.fm.Controller}.
 *
 * @author Tomasz Pik, AIS.PL
 */
public abstract class AbstractControllerTest extends TestCase {

  protected Controller controller;
  protected MockHttpServletRequest request;
  protected MockHttpServletResponse response;
  protected MockHttpSession session;
  protected PageSelector selector = new DefaultPageSelector();

  /**
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() throws Exception {
    controller = createController(getConfigurationFileName());

    request  = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    session  = new MockHttpSession();
    request.setSession(session);
  }

  /**
   * Return name of file that should be used to configure {@link Controller}.
   *
   * @return name of file that should be used to configure {@link Controller}
   */
  public abstract String getConfigurationFileName();

  private Controller createController(String xmlFileName) throws Exception {
    InputStream stream = this.getClass().getResourceAsStream(xmlFileName);
    assertNotNull("config file: " + xmlFileName + " is null", stream);

    InputSource source = new InputSource(stream);
    source.setSystemId(this.getClass().getResource(xmlFileName).toString());

    XMLReader xmlReader = XMLUtils.newXMLReader(true, true);
    Workflow workflow = new Workflow(new SimpleLog(xmlFileName));
    xmlReader.setContentHandler(workflow);
    xmlReader.setEntityResolver(FmEntityResolver.getResolverInstance());
    xmlReader.parse(source);
    Application application = new Application(xmlFileName, new MockServlet());
    return new Controller(application, workflow);
  }

  class MockServlet extends HttpServlet {

    MockServlet() throws ServletException {
      ServletContext context = new MockServletContext();
      MockServletConfig config = new MockServletConfig(context);
      init(config);
    }
  }

  class DefaultPageSelector implements PageSelector {

    private static final String PAGE = "page";

    public String getRequestedPageKey(HttpServletRequest request) {
      return request.getParameter(PAGE);
    }

  }

}
