package pl.aislib.test.fm;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;

import pl.aislib.fm.Page;
import pl.aislib.fm.PageResponse;

/**
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.1 $
 */
public class DummyPage extends Page {

  /**
   * @see pl.aislib.fm.Page#getPageResponse()
   */
  public PageResponse getPageResponse() throws IOException, ServletException {
    return new PageResponse(this, new HashMap());
  }
}
