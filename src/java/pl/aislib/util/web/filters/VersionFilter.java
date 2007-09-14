package pl.aislib.util.web.filters;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter which checks for presence of <code>version</code> parameter
 * and handle such requests in special way.
 *
 * If filter found that request containing <code>version</code> parameter,
 * present version of the application specified as <code>app.version</code>
 * context parameter.
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.1 $
 */
public class VersionFilter extends AbstractHttpFilter {

  private String parameterName = "version";
  private String configKey = "app.version";
  private String version = null;

  private static final String WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
  private static final String TEXT_PLAIN = "text/plain";

  /**
   * Gets version from servlet config init parameter.
   */
  public void init() {
    version = config.getServletContext().getInitParameter(configKey);
  }

  /**
   * @see pl.AbstractHttpFilter#doFilter(HttpServletRequest, HttpServletResponse, FilterChain)
   */
  public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
    throws IOException, ServletException {
    String contentType = req.getContentType();
    if ((contentType == null) || (contentType.equals(WWW_FORM_URLENCODED))) {
      if ((req.getParameter(parameterName) != null) && (version != null)) {
        resp.setContentType(TEXT_PLAIN);
        Writer writer = resp.getWriter();
        writer.write("Version: " + version);
        return;
      }
    }

    chain.doFilter(req, resp);
  }
}
