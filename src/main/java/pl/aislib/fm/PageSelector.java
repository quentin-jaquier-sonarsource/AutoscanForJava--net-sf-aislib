package pl.aislib.fm;

import javax.servlet.http.HttpServletRequest;

/**
 * Selecting page code based on request.
 *
 * @author Tomasz Pik, AIS.PL
 */
public interface PageSelector {

  /**
   * Analyze request and returns key representing page selected by user.
   *
   * @param request user's request.
   * @return key of page requested by user.
   */
  String getRequestedPageKey(HttpServletRequest request);
}
