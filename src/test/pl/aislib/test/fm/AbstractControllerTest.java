package pl.aislib.test.fm;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.impl.SimpleLog;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.mockobjects.servlet.MockServletConfig;
import com.mockobjects.servlet.MockServletContext;
import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;
import com.mockobjects.servlet.MockHttpSession;

import pl.aislib.fm.Application;
import pl.aislib.fm.Controller;
import pl.aislib.fm.Workflow;
import pl.aislib.util.xml.XMLUtils;
import junit.framework.TestCase;

/**
 * Base class for test cases of {@link pl.aislib.fm.Controller}. 
 * 
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.5 $
 */
public abstract class AbstractControllerTest extends TestCase {

  protected Controller controller;
  protected MockHttpServletRequest request;
  protected MockHttpServletResponse response;
  protected MockHttpSession session;

  /**
   * @see junit.framework.TestCase#TestCase(String)
   */  
  public AbstractControllerTest(String arg) {
    super(arg);
  }

  /**
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() throws Exception {
    controller = createController(getConfigurationFileName());

    request  = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    session  = new MockHttpSession();
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
    xmlReader.parse(source);
    Application application = new Application(xmlFileName, new MockServlet());
    return new Controller(application, workflow);
  }

  class MockServlet extends HttpServlet {

    MockServlet() throws ServletException {
      ServletContext context = new MockServletContext();
      MockServletConfig config = new MockServletConfig();
      config.setServletContext(context);
      init(config);
    }
  }
}
