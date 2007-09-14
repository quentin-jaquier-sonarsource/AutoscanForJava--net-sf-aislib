package pl.aislib.util.web.servlet;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactory;

/**
 * Servlet for monitoring servlet engine, environment and http session.
 *
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.1 $
 */
public class SpyServlet extends BaseServlet {

  private static final String SESSION = "/session";
  private static final String DATETIME = "/datetime";
  private static final String JAVA_PROPERTIES = "javaproperties";
  private static final String JAXP_FACTORIES = "jaxpfactories";
  private static final String PACKAGES = "packages";

  public void doPostOrGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    response.setContentType("text/html");
    Writer writer = response.getWriter();

    String action = request.getParameter("action");
    String pathInfo = request.getPathInfo();
    if( pathInfo == null ){ 
      pathInfo = "";  
    }
    writer.write(getHeader());

    if (pathInfo.startsWith(SESSION)) {
      writer.write(showSession(request, response));
    } else if (pathInfo.startsWith(DATETIME)) {
      writer.write(datetime(request, response));
    } else if (action == null) {
      writer.write(getUsage(request, response));
    } else if (action.equals(JAVA_PROPERTIES)) {
      writer.write(getJavaProperties(request, response));
    } else if (action.equals(JAXP_FACTORIES)) {
      writer.write(getJAXPFactories(request, response));
    } else if (action.equals(PACKAGES)) {
      writer.write(getPackages(request, response));
    }

    writer.write(getFooter());
  }

  private String datetime(HttpServletRequest request, HttpServletResponse response) {
    StringBuffer result = new StringBuffer();
    result.append("<h2 class='header'>Date/Time settings</h2>\n");
    result.append("<ul>");
    
    TimeZone tZ = TimeZone.getDefault();
    result.append("<li>TimeZone: " + tZ.getDisplayName() + "</li>");
    result.append("<li>Use Daylight savings time: " + tZ.useDaylightTime() + "</li>");
    
    Calendar sysdate = Calendar.getInstance();
    result.append("<li>System date: " + sysdate.getTime().toString() + "</li>");

    result.append("<li><tt>user.timezone</tt> property: " + System.getProperty("user.timezone") + "</li>");
    result.append("</ul>");
    return result.toString();
  }

  /**
   * @param request
   * @param response
   * @return
   */
  private String showSession(HttpServletRequest request, HttpServletResponse response) {
    StringBuffer result = new StringBuffer();
    HttpSession session = request.getSession();

    String action = request.getParameter("action");
    if (action == null)
      action = "";

    if (action.equals("Clear")) {
      session.invalidate();
      session = request.getSession();
    }
    result.append("<form>");
    result.append("<h2 class='header'>Session</h2>\n");
    result.append("<hr width='100%'>");
    result.append("<input type='submit' name='action' value='Show'>");
    result.append("<h1>Session:</h1>");
    result.append("<ul><li>created at: <tt>" + dateToString(session.getCreationTime()) + "</tt></li>");
    result.append("<li>unique id: <tt>" + session.getId() + "</tt></li>");
    result.append("<li>attributes:<ul>");
    Enumeration enum = session.getAttributeNames();
    while (enum.hasMoreElements()) {
      String key = (String) enum.nextElement();
      Object o = session.getAttribute(key);
      result.append("<li><tt>"+key+" -&gt; " + o.toString() + " (" + o.getClass().getName() + ")</tt></li>");
    }

    result.append("</ul></li></ul> <hr width='100%'>");
    result.append("<input type='submit' name='action' value='Clear'>");
    result.append("</form>");

    return result.toString();
  }

  public String dateToString(long dateL) {
    Date date = new Date(dateL);
    DateFormat dateFormat = new SimpleDateFormat("HH/mm/ss/SS");
    return dateFormat.format(date);
  }

  /**
   * Generate information about configuration of JAXP factories.
   */
  public String getJAXPFactories(HttpServletRequest request, HttpServletResponse response) {
    StringBuffer result = new StringBuffer();
    result.append("<h2 class='header'>JAXP Factories</h2>\n");
    result.append("<ul class='list'>\n");
    result.append("  <li class='option'>SAXParserFactory -> ");
    // SAX
    SAXParserFactory saxFactory = SAXParserFactory.newInstance();
    if (saxFactory != null) {
      result.append(saxFactory.getClass().getName());
    } else {
      result.append("null");
    }
    result.append("</li>\n");
    // DOM
    result.append("  <li class='option'>DocumentBuilderFactory -> ");
    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    if (domFactory != null) {
      result.append(domFactory.getClass().getName());
    } else {
      result.append("null");
    }
    result.append("</li>\n");
    // TRAX
    result.append("  <li class='option'>TransformerFactory -> ");
    TransformerFactory traxFactory = TransformerFactory.newInstance();
    if (traxFactory != null) {
      result.append(traxFactory.getClass().getName());
    } else {
      result.append("null");
    }
    result.append("</li>\n");

    result.append("</ul>\n");
    result.append("<a class='menulink' href='" + response.encodeURL(request.getRequestURI()) + "'>Menu</a>\n");
    return result.toString();
  }

  /**
   * Generate a list of Java System Properties.
   */
  public String getJavaProperties(HttpServletRequest request, HttpServletResponse response) {
    StringBuffer result = new StringBuffer();
    StringBuffer javaResult = new StringBuffer();
    StringBuffer otherResult = new StringBuffer();
    Properties systemProperties = System.getProperties();
    result.append("<h2 class='header'>System properties</h2>\n");
    result.append("<ul class'list'>\n");
    Iterator keys = systemProperties.keySet().iterator();
    while (keys.hasNext()) {
      String key = (String) keys.next();
      if (key.startsWith("java.")) {
        javaResult.append("  <li class='option'>" + key + " -> " + systemProperties.getProperty(key) + "</li>\n");
      } else {
        otherResult.append("  <li class='option'>" + key + " -> " + systemProperties.getProperty(key) + "</li>\n");
      }
    }
    result.append(javaResult);
    result.append(otherResult);
    result.append("</ul>\n");
    result.append("<a class='menulink' href='" + response.encodeURL(request.getRequestURI()) + "'>Menu</a>\n");
    return result.toString();
  }

  /**
   * Generate package table.
   */
  private String getPackages(HttpServletRequest request, HttpServletResponse response) {
    StringBuffer result = new StringBuffer();
    result.append("<h2 class='header'>Available Packages</h2>\n");
    result.append("<table class='packages'>\n");
    result.append("<tr><th>Name</th><tr><th>Specification</th><th>Implementation</th><th>Sealed</th></tr>\n");
    Package[] packages = Package.getPackages();
    Arrays.sort(packages, new PackageComparator());
    for (int i = 0; i < packages.length; i++) {
      appendPackage(result, packages[i]);
    }
    result.append("</table>");
    return result.toString();
  }

  /**
   * Generate usage screen (menu).
   */
  public String getUsage(HttpServletRequest request, HttpServletResponse response) {
    StringBuffer result = new StringBuffer();
    result.append("<h2 class='header'>Available options</h2>\n");
    result.append("<ul class='list'>\n");
    // Java System Properties option
    result.append("  <li class='option'><a class='link' href='");
    result.append(response.encodeURL(request.getRequestURI() + "?action=" + JAVA_PROPERTIES));
    result.append("'>Java System Properties</a></li>\n");
    // JAXP Factories
    result.append("  <li class='option'><a class='link' href='");
    result.append(response.encodeURL(request.getRequestURI() + "?action=" + JAXP_FACTORIES));
    result.append("'>JAXP Factories</a></li>\n");
    // Packages
    result.append("  <li class='option'><a class='link' href='");
    result.append(response.encodeURL(request.getRequestURI() + "?action=" + PACKAGES));
    result.append("'>Packages</a></li>\n");

    // Session
    result.append("  <li class='option'><a class='link' href='");
    result.append(response.encodeURL(request.getRequestURI() + "/session"));
    result.append("'>Session</a></li>\n");

    // Date/Time settings
    result.append("  <li class='option'><a class='link' href='");
    result.append(response.encodeURL(request.getRequestURI() + "/datetime"));
    result.append("'>Date/Time settings</a></li>\n");


    result.append("</ul>\n");
    return result.toString();
  }

  /**
   * Generate page header.
   */
  public String getHeader() {
    return "<html><head><title>SpyServlet</title></head><body>\n";
  }

  /**
   * Generate page footer.
   */
  public String getFooter() {
    return "</body></html>";
  }

  private void appendPackage(StringBuffer result, Package _package) {
    result.append("<tr><td>");
    result.append(_package.getName());
    result.append("</td><td>");
    boolean specExists = false;
    if (_package.getSpecificationTitle() != null) {
      specExists = true;
      result.append(_package.getSpecificationTitle());
    }
    if (_package.getSpecificationVendor() != null) {
      if (specExists) {
        result.append(" / ");
      }
      specExists = true;
      result.append(_package.getSpecificationVendor());
    }
    if (_package.getSpecificationVersion() != null) {
      if (specExists) {
        result.append(" / ");
      }
      specExists = true;
      result.append(_package.getSpecificationVersion());
    }
    if (!specExists) {
      result.append("&nbsp;");
    }
    result.append("</td><td>");
    boolean implExists = false;
    if (_package.getImplementationTitle() != null) {
      implExists = true;
      result.append(_package.getImplementationTitle());
    }
    if (_package.getImplementationVendor() != null) {
      if (implExists) {
        result.append(" / ");
      }
      implExists = true;
      result.append(_package.getImplementationVendor());
    }
    if (_package.getImplementationVersion() != null) {
      if (implExists) {
        result.append(" / ");
      }
      implExists = true;
      result.append(_package.getImplementationVersion());
    }
    if (!implExists) {
      result.append("&nbsp;");
    }
    result.append("</td><td>");
    if (_package.isSealed()) {
      result.append("x");
    } else {
      result.append("&nbsp;");
    }
    result.append("</td></tr>\n");
  }

  private class PackageComparator implements Comparator {

    public int compare(Object o1, Object o2) {
      Package p1 = (Package) o1;
      Package p2 = (Package) o2;
      return p1.getName().compareTo(p2.getName());
    }
  }
}
