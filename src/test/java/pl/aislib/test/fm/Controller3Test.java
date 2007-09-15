package pl.aislib.test.fm;

import pl.aislib.fm.PageInfo;

/**
 * Testing <code>session-expiration</code> attributes.
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.1 $
 */
public class Controller3Test extends AbstractControllerTest {

  public void testIndexPageNewSession() {
    session.setNew(true);
    PageInfo pageInfo = controller.getPageInfo(request, response, selector);
    assertNotNull(pageInfo);

    assertEquals("index", pageInfo.getActionKey());
  }

  public void testIndexPageExistingSession() {
    session.setNew(false);
    PageInfo pageInfo = controller.getPageInfo(request, response, selector);
    assertNotNull(pageInfo);

    assertEquals("index", pageInfo.getActionKey());
  }

  public void testSecondPageNewSession() {
    session.setNew(true);
    request.addParameter("page", "second");
    PageInfo pageInfo = controller.getPageInfo(request, response, selector);
    assertNotNull(pageInfo);

    assertEquals("second", pageInfo.getActionKey());
  }

  public void testSecondPageExistingSession() {
    session.setNew(false);
    request.addParameter("page", "second");
    PageInfo pageInfo = controller.getPageInfo(request, response, selector);
    assertNotNull(pageInfo);

    assertEquals("second", pageInfo.getActionKey());
  }

  public void testThirdPageNewSession() {
    session.setNew(true);
    request.addParameter("page", "third");
    PageInfo pageInfo = controller.getPageInfo(request, response, selector);
    assertNotNull(pageInfo);

    assertEquals("index", pageInfo.getActionKey());
  }

  public void testThirdPageExistingSession() {
    session.setNew(false);
    request.addParameter("page", "third");
    PageInfo pageInfo = controller.getPageInfo(request, response, selector);
    assertNotNull(pageInfo);

    assertEquals("third", pageInfo.getActionKey());
  }


  public String getConfigurationFileName() {
    return "controller3.xml";
  }

}
