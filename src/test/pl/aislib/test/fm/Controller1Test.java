package pl.aislib.test.fm;

import pl.aislib.fm.PageInfo;

/**
 * Test basic funcionality of {@link pl.aislib.fm.Controller}.
 * 
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.4 $
 */
public class Controller1Test extends AbstractControllerTest {

  /**
   * @param arg0
   */
  public Controller1Test(String arg0) {
    super(arg0);
  }

  public void testIndexPage() throws Exception  {
    PageInfo pageInfo = controller.getPageInfo(request, response);
    assertNotNull(pageInfo);

    assertEquals("index", pageInfo.getActionKey());
  }

  public void testSecondPage() throws Exception  {
    request.setupAddParameter("page", "second");
    PageInfo pageInfo = controller.getPageInfo(request, response);
    assertNotNull(pageInfo);

    assertEquals("second", pageInfo.getActionKey());
  }

  public void testTriggeredPage() throws Exception  {
    request.setupAddParameter("page", "third");

    PageInfo thirdPage = controller.getPageInfo(request, response);
    assertNotNull(thirdPage);
    assertEquals("third", thirdPage.getActionKey());
    
    request.setupAddParameter("trigger", "trigger");
    PageInfo fourthPage = controller.getPageInfo(request, response);
    assertNotNull(fourthPage);
    assertEquals("fourth", fourthPage.getActionKey());
  }

  public void testQueueOfTriggers() throws Exception {
    request.setupAddParameter("page", "trigger1");
    
    PageInfo firstPage = controller.getPageInfo(request, response);
    assertNotNull(firstPage);
    assertEquals("trigger1", firstPage.getActionKey());

    request.setupAddParameter("trigger", "trigger");
    PageInfo triggeredPage = controller.getPageInfo(request, response);
    assertNotNull("trigger2", triggeredPage.getActionKey());
  }

  /**
   * @see pl.aislib.test.fm.AbstractControllerTest#getConfigurationFileName()
   */
  public String getConfigurationFileName() {
    return "controller1.xml";
  }
}
