package pl.aislib.util.web.filters;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
 * Also, for every filter parameter with name staring with <code>package.</code>
 * Java Package is loaded (using init parameter value as package name) and
 * specification and implementation versions are presented, with init parameter
 * name (with <code>package.</code> substracked) as name.
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.5 $
 */
public class VersionFilter extends AbstractHttpFilter {

  private String parameterName = "version";
  private String configKey = "app.version";
  private String version = null;

  private static final String WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
  private static final String TEXT_PLAIN = "text/plain";
  private static final String PACKAGE = "package.";
  private static final String PATTERN = "pattern";
  private static final String DEFAULT_PATTERN = "(.*)";

  private Map packages;

  /**
   * Gets version from servlet config init parameter.
   */
  public void init() throws ServletException {
    String patternS = DEFAULT_PATTERN;
    if (config.getInitParameter(PATTERN) != null) {
      patternS = config.getInitParameter(PATTERN);
    }
    version = config.getServletContext().getInitParameter(configKey);
    if (version != null) {
      try {
        Pattern pattern = Pattern.compile(patternS);
        Matcher matcher = pattern.matcher(version);
        if (matcher.matches()) {
          version = matcher.group(1);
        }
      } catch (PatternSyntaxException pse) {
        throw new ServletException(pse);
      }
    }
    packages = new LinkedHashMap();
    Enumeration initNames = config.getInitParameterNames();
    while (initNames.hasMoreElements()) {
      String initName = (String) initNames.nextElement();
      if (initName.startsWith(PACKAGE)) {
        packages.put(initName.substring(PACKAGE.length()), config.getInitParameter(initName));
      }
    }
  }

  /**
   * @see AbstractHttpFilter#doFilter(HttpServletRequest, HttpServletResponse, FilterChain)
   */
  public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
    throws IOException, ServletException {
    String contentType = req.getContentType();
    if ((contentType == null) || (contentType.equals(WWW_FORM_URLENCODED))) {
      if ((req.getParameter(parameterName) != null) && (version != null)) {
        resp.setContentType(TEXT_PLAIN);
        Writer writer = resp.getWriter();
        writer.write("Version: " + version + "\n");
        if (packages.size() != 0) {
          writer.write("Packages (specification version/implementation version):\n");
        }
        Iterator packageKeys = packages.keySet().iterator();
        while (packageKeys.hasNext()) {
          String packageKey = (String) packageKeys.next();
          String packageName = (String) packages.get(packageKey);
          Package patzkage = Package.getPackage(packageName);
          if (patzkage != null) {
            writer.write(" " + packageKey + ": " + patzkage.getSpecificationTitle() + " " + patzkage.getSpecificationVersion() + "/" + patzkage.getImplementationVersion());
          } else {
            writer.write(" " + packageKey + ": " + null);
          }
          writer.write("\n");
        }
        return;
      }
    }
    chain.doFilter(req, resp);
  }
}
