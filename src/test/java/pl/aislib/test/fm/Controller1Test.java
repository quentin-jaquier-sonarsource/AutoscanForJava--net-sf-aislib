package pl.aislib.test.fm;

import pl.aislib.fm.PageInfo;

/**
 * Test basic funcionality of {@link pl.aislib.fm.Controller}.
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.6 $
 */
public class Controller1Test extends AbstractControllerTest {

  public void testIndexPage() throws Exception  {
    PageInfo pageInfo = controller.getPageInfo(request, response, selector);
    assertNotNull(pageInfo);

    assertEquals("index", pageInfo.getActionKey());
  }

  public void testSecondPage() throws Exception  {
    request.addParameter("page", "second");
    PageInfo pageInfo = controller.getPageInfo(request, response, selector);
    assertNotNull(pageInfo);

    assertEquals("second", pageInfo.getActionKey());
  }

  public void testTriggeredPage() throws Exception  {
    request.addParameter("page", "third");

    PageInfo thirdPage = controller.getPageInfo(request, response, selector);
    assertNotNull(thirdPage);
    assertEquals("third", thirdPage.getActionKey());

    request.addParameter("trigger", "trigger");
    PageInfo fourthPage = controller.getPageInfo(request, response, selector);
    assertNotNull(fourthPage);
    assertEquals("fourth", fourthPage.getActionKey());
  }

  public void testQueueOfTriggers() throws Exception {
    request.addParameter("page", "trigger1");

    PageInfo firstPage = controller.getPageInfo(request, response, selector);
    assertNotNull(firstPage);
    assertEquals("trigger1", firstPage.getActionKey());

    request.addParameter("trigger", "trigger");
    PageInfo triggeredPage = controller.getPageInfo(request, response, selector);
    assertNotNull("trigger2", triggeredPage.getActionKey());
  }

  /**
   * @see pl.aislib.test.fm.AbstractControllerTest#getConfigurationFileName()
   */
  public String getConfigurationFileName() {
    return "controller1.xml";
  }
}
