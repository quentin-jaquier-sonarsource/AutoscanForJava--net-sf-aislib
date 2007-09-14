package pl.aislib.test.fm;

import pl.aislib.fm.PageInfo;

/**
 * Test global triggers handling in {@link pl.aislib.fm.Controller}
 * 
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.2 $
 */
public class Controller2Test extends AbstractControllerTest {

  /**
   * @param arg
   */
  public Controller2Test(String arg) {
    super(arg);
  }

  public void setUp() throws Exception {
    super.setUp();
    request.setSession(session);
  }

  public void testIndexPage() {
    PageInfo pageInfo = controller.getPageInfo(request, response);
    assertNotNull(pageInfo);

    assertEquals("index", pageInfo.getActionKey());
  }

  public void testIndexWithRequestParameter() {
    request.setupAddParameter("param", "param");
    PageInfo pageInfo = controller.getPageInfo(request, response);
    assertNotNull(pageInfo);

    assertEquals("parampage", pageInfo.getActionKey());
  }

  public void testSecondWithParameter() {
    request.setupAddParameter("param", "param");
    request.setupAddParameter("page", "second");
    PageInfo pageInfo = controller.getPageInfo(request, response);
    assertNotNull(pageInfo);

    assertEquals("parampage", pageInfo.getActionKey());
  }

  public void testIndexWithSessionAttribute() {
    session.setAttribute("controller2.xml.session", "session");
    PageInfo pageInfo = controller.getPageInfo(request, response);
    assertNotNull(pageInfo);

    assertEquals("sessionpage", pageInfo.getActionKey());
  }

  public void testSecondWithSessionAttribute() {
    session.setAttribute("controller2.xml.session", "session");
    request.setupAddParameter("page", "second");
    PageInfo pageInfo = controller.getPageInfo(request, response);
    assertNotNull(pageInfo);

    assertEquals("sessionpage", pageInfo.getActionKey());
  }

  /**
   * @see pl.aislib.test.fm.AbstractControllerTest#getConfigurationFileName()
   */
  public String getConfigurationFileName() {
    return "controller2.xml";
  }
}
