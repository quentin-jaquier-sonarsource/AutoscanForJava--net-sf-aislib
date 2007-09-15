package pl.aislib.fm.pages;

import java.util.HashMap;

import pl.aislib.fm.Page;
import pl.aislib.fm.PageResponse;

/**
 * Static page that returns an empty map in response.
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.2 $
 * @since AISLIB 0.5.2
 */
public class StaticPage extends Page {

  /**
   * @see pl.aislib.fm.Page#getPageResponse()
   */
  public PageResponse getPageResponse() {
    return new PageResponse(this, new HashMap());
  }

}
