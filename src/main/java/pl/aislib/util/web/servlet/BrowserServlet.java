package pl.aislib.util.web.servlet;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Vector;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;

import java.text.SimpleDateFormat;

import javax.servlet.ServletConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <font color='red'>Work in progress</font>
 *
 * @author Tomasz Sandecki, AIS.PL
 */
public class BrowserServlet extends HttpServlet {
  static final String STYLE_URL = "style_url";
  private String context;
  private String styleUrl;

  public void init() {
    context = "/ais/www/www/";
    ServletConfig config = getServletConfig();

    styleUrl = config.getInitParameter(STYLE_URL);
    if (styleUrl != null && styleUrl.trim().length() == 0) {
      styleUrl = null;
    }
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    HttpServletRequest request = req;

    String pathInContext = request.getServletPath();
    if (request.getPathInfo() != null) {
      pathInContext = pathInContext + request.getPathInfo();
    }

    String requestFileSystemPath = context + pathInContext;

    File requestedFile = new File(requestFileSystemPath);

    if ((requestedFile.exists()) && (requestedFile.isFile())) {
      String contentType = "application/octet-stream";
      if (requestFileSystemPath.indexOf(".") != -1) {
        String extension =
          requestFileSystemPath.substring(requestFileSystemPath.lastIndexOf("."), requestFileSystemPath.length());
        if (defaultMimeTypes.get(extension) != null) {
          contentType = (String) defaultMimeTypes.get(extension);
        }
      }
      res.setContentType(contentType);
      InputStream iStream = new BufferedInputStream(new FileInputStream(requestedFile));
      OutputStream oStream = res.getOutputStream();
      byte[] buffer = new byte[2048];
      int tmp = 0;
      while ((tmp = iStream.read(buffer)) != -1) {
        oStream.write(buffer, 0, tmp);
      }
      oStream.flush();
      iStream.close();
      return;
    }

    res.setContentType("text/html");

    PrintWriter pw = res.getWriter();

    boolean desc = (request.getParameter("desc") != null);

    String uri = req.getRequestURI();
    pw.println("<html>");
    pw.println("<head>");
    pw.println("<title>Directory of " + uri + "</title>");
    if (styleUrl != null) {
      pw.println("<link rel='stylesheet' type='text/css' href='" + styleUrl + "'>");
    }
    pw.println("</head>");
    pw.println("<body bgcolor=\"#ffffff\"  leftmargin=\"0\" topmargin=\"0\" marginheight=\"0\" marginwidth=\"0\" >\n");

    pw.println("<table width='100%' border='0' cellspacing='1' cellpadding='2' align='center'>");
    pw.println(
      "<tr  class='topmost' bgcolor='#ddddee'>"
        + "   <td colspan='2'>"
        + "       <font size='+2'>"
        + "       <strong>"
        + "Directory Listing for:"
        + "       <tt>"
        + uri
        + "</tt></strong>"
        + "   </td>"
        + "   <td align='right' width='5%'> "
        + "       <font size='-6'><input type='text' name='cn_search'></font>"
        + "   </td>"
        + "</tr>");

    Comparator sorter = new FileInfoComparator(request);
    Iterator i = process(requestFileSystemPath, sorter);

    pw.println(
      "<tr><td align='left'><font size='+1'><a href='"
        + req.getRequestURI()
        + "?byName"
        + descParam(!desc)
        + "'>Name</a></font></td>");
    pw.println(
      "<td align='right'><font size='+1'><a href='"
        + req.getRequestURI()
        + "?bySize"
        + descParam(!desc)
        + "'>Size</a></font></th>");
    pw.println(
      "<td align='right'><font size='+1'><a href='"
        + req.getRequestURI()
        + "?byLMD"
        + descParam(!desc)
        + "'>Last mod.</a></font></th></tr>");

    if (uri.length() > 1) {
      String mypath = uri.substring(1, uri.length());
      if ((mypath.length() > 0) && (!("/" + mypath).equals(req.getContextPath() + "/"))) {
        if (mypath.lastIndexOf('/') != -1) {
          mypath = "/" + mypath.substring(0, mypath.lastIndexOf('/')) + "/";
        } else {
          mypath = "/";
        }
        pw.println(
          "<tr><td class='uprow' colspan='3' bgcolor='#aaaaaa'><font size='+2'>&nbsp;&nbsp;&nbsp;[ <a href='"
            + mypath
            + "'>..</a> ]</font></td></tr>");
      }
    }

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    String[] colors = { "bgcolor='#dddddd'", "bgcolor='#eeeeee'" };
    int counter = 0;

    while (i.hasNext()) {
      FileInfo fileInfo = (FileInfo) i.next();
      String name = fileInfo.getName();

      if ((name.equalsIgnoreCase("web-inf") || name.equalsIgnoreCase("meta-inf"))) {
        continue;
      }

      String enc = request.getContextPath() + pathInContext + "/" + name;

      String dir;
      String style;
      if (fileInfo.isDirectory()) {
        dir = "dir";
        style = "dirrow";
      } else {
        dir = String.valueOf(fileInfo.getSize());
        style = "filerow";
      }

      pw.println(
        "<tr><td class='"
          + style
          + "' "
          + colors[counter % 2]
          + ">&nbsp;&nbsp;&nbsp;<a href='"
          + enc
          + "'><tt>"
          + name
          + "</tt></a></td><td "
          + colors[counter % 2]
          + "  align='right'><tt>"
          + dir
          + "</tt></td><td "
          + colors[counter % 2]
          + " align='right'><tt>"
          + formatter.format(new Date(fileInfo.getLMD()))
          + "</tt></td></tr>");

      counter++;
    }

    pw.println("</table>");
    pw.println("</body>");
    pw.println("</html>");
    pw.close();
  }

  String showLine(String l, int pos, int searchedLen) {
    int ll = 80;
    StringBuffer s = new StringBuffer(l);
    int len = l.length();

    if (l.length() > ll) {
      int sp = pos - 5;
      if (sp < 0) {
        sp = 0;
      }
      s = new StringBuffer(l.substring(sp, (sp + ll > len) ? len - 1 : sp + ll));
    }

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c < ' ' || c > 'z') {
        s.setCharAt(i, '.');
      }
    }

    return protectHTML(s.toString());
  }

  String protectHTML(String whatCheck) {
    StringBuffer sBuf = new StringBuffer();

    for (int i = 0, length = whatCheck.length(); i < length; i++) {
      char ch = whatCheck.charAt(i);
      if (ch == '"') {
        sBuf.append("&quot;");
      } else if (ch == '<') {
        sBuf.append("&lt;");
      } else if (ch == '>') {
        sBuf.append("&gt;");
      } else if (ch == '&') {
        sBuf.append("&amp;");
      } else {
        sBuf.append(ch);
      }
    }

    return sBuf.toString();
  }

  String descParam(boolean desc) {
    return desc ? "&amp;desc" : "";
  }

  public Iterator process(String path, Comparator sorter) throws IOException {
    Vector directories = new Vector();
    Vector files = new Vector();
    File file = new File(path);
    if (!file.exists()) {
      throw new IOException("404");
    }
    File[] filess = file.listFiles();
    for (int i = 0; i < filess.length; i++) {
      File name = filess[i];
      if (name.isDirectory()) {
        directories.add(new FileInfo(name.getName(), name.lastModified(), 0, true, null));
      } else {
        files.add(new FileInfo(name.getName(), name.lastModified(), name.length(), false, name));
      }
    }

    Collections.sort(files, sorter);
    Collections.sort(directories, sorter);

    directories.addAll(files);

    return directories.iterator();
  }

  public class FileInfo {
    protected String name;
    protected long lmd;
    protected long size;
    protected boolean isDirectory;
    protected File path = null;

    public FileInfo(String name, long lmd, long size, boolean isDirectory, File p) {
      this.name = name;
      this.lmd = lmd;
      this.size = size;
      this.isDirectory = isDirectory;
      path = p;
    }

    public File getPath() {
      return path;
    }

    public String getName() {
      return name;
    }

    public long getLMD() {
      return lmd;
    }

    public boolean isDirectory() {
      return isDirectory;
    }

    public long getSize() {
      return size;
    }
  }

  public class FileInfoComparator implements Comparator {

    private int which = 0; // 0 - name, 1 - size, 2 - lmd
    private int desc;

    public FileInfoComparator(HttpServletRequest request) {
      if (request.getParameter("bySize") != null) {
        which = 1;
      } else if (request.getParameter("byLMD") != null) {
        which = 2;
      } else {
        which = 0;
      }

      desc = request.getParameter("desc") != null ? -1 : 1;
    }

    public int compare(Object o1, Object o2) {
      FileInfo f1 = (FileInfo) o1;
      FileInfo f2 = (FileInfo) o2;
      switch (which) {
        case 0 :
          return desc * f1.getName().compareToIgnoreCase(f2.getName());
        case 1 :
          return desc
            * ((f1.isDirectory())
              ? (f1.getName().compareToIgnoreCase(f2.getName()))
              : ((int) (f1.getSize() - f2.getSize())));
        case 2 :
          return desc * ((int) (f1.getLMD() - f2.getLMD()));
      }
      return 0;
    }
  }

  private static Map defaultMimeTypes = new HashMap();

  static {
    defaultMimeTypes.put(".aif", "audio/x-aiff");
    defaultMimeTypes.put(".aiff", "audio/x-aiff");
    defaultMimeTypes.put(".aifc", "audio/x-aiff");
    defaultMimeTypes.put(".ai", "application/postscript");
    defaultMimeTypes.put(".au", "audio/basic");
    defaultMimeTypes.put(".asc", "text/plain");
    defaultMimeTypes.put(".asf", "video/x-ms-asf");
    defaultMimeTypes.put(".asx", "video/x-ms-asf");
    defaultMimeTypes.put(".avi", "video/x-msvideo");

    defaultMimeTypes.put(".bin", "application/octet-stream");
    defaultMimeTypes.put(".bcpio", "application/x-bcpio");
    defaultMimeTypes.put(".bmp", "image/bmp");

    defaultMimeTypes.put(".class", "application/octet-stream");
    defaultMimeTypes.put(".cpt", "application/mac-compactpro");
    defaultMimeTypes.put(".css", "text/css");
    defaultMimeTypes.put(".cpio", "application/x-cpio");
    defaultMimeTypes.put(".csh", "application/x-csh");
    defaultMimeTypes.put(".cdf", "application/x-netcdf");

    defaultMimeTypes.put(".dms", "application/octet-stream");
    defaultMimeTypes.put(".doc", "application/msword");
    defaultMimeTypes.put(".dcr", "application/x-director");
    defaultMimeTypes.put(".dir", "application/x-director");
    defaultMimeTypes.put(".dxr", "application/x-director");
    defaultMimeTypes.put(".dvi", "application/x-dvi");

    defaultMimeTypes.put(".exe", "application/octet-stream");
    defaultMimeTypes.put(".eps", "application/postscript");
    defaultMimeTypes.put(".etx", "text/x-setext");

    defaultMimeTypes.put(".gtar", "application/x-gtar");
    defaultMimeTypes.put(".gif", "image/gif");
    defaultMimeTypes.put(".gz", "application/octet-stream");

    defaultMimeTypes.put(".hdml", "text/x-hdml");
    defaultMimeTypes.put(".hqx", "application/mac-binhex40");
    defaultMimeTypes.put(".html", "text/html");
    defaultMimeTypes.put(".htm", "text/html");
    defaultMimeTypes.put(".hdf", "application/x-hdf");

    defaultMimeTypes.put(".ief", "image/ief");
    defaultMimeTypes.put(".ice", "x-conference/x-cooltalk");

    defaultMimeTypes.put(".js", "application/x-javascript");
    defaultMimeTypes.put(".jpeg", "image/jpeg");
    defaultMimeTypes.put(".jpg", "image/jpeg");
    defaultMimeTypes.put(".jpe", "image/jpeg");

    defaultMimeTypes.put(".kar", "audio/midi");

    defaultMimeTypes.put(".latex", "application/x-latex");
    defaultMimeTypes.put(".lha", "application/octet-stream");
    defaultMimeTypes.put(".lhz", "application/octet-stream");

    defaultMimeTypes.put(".mid", "audio/midi");
    defaultMimeTypes.put(".mpeg", "video/mpeg");
    defaultMimeTypes.put(".mpg", "video/mpeg");
    defaultMimeTypes.put(".mpe", "video/mpeg");
    defaultMimeTypes.put(".mov", "video/quicktime");
    defaultMimeTypes.put(".movie", "video/x-sgi-movie");
    defaultMimeTypes.put(".mpga", "audio/mpeg");
    defaultMimeTypes.put(".mp2", "audio/mpeg");
    defaultMimeTypes.put(".mp3", "audio/mpeg");
    defaultMimeTypes.put(".man", "application/x-troff-man");
    defaultMimeTypes.put(".me", "application/x-troff-me");
    defaultMimeTypes.put(".ms", "application/x-troff-ms");

    defaultMimeTypes.put(".nc", "application/x-netcdf");

    defaultMimeTypes.put(".oda", "application/oda");

    defaultMimeTypes.put(".pdf", "application/pdf");
    defaultMimeTypes.put(".ps", "application/postscript");
    defaultMimeTypes.put(".ppt", "application/vnd.ms-powerpoint");
    defaultMimeTypes.put(".png", "image/png");
    defaultMimeTypes.put(".pgn", "application/x-chess-pgn");
    defaultMimeTypes.put(".pnm", "image/x-portable-anymap");
    defaultMimeTypes.put(".pbm", "image/x-portable-bitmap");
    defaultMimeTypes.put(".pgm", "image/x-portable-graymap");
    defaultMimeTypes.put(".ppm", "image/x-portable-pixmap");

    defaultMimeTypes.put(".qt", "video/quicktime");

    defaultMimeTypes.put(".rtf", "application/rtf");
    defaultMimeTypes.put(".ram", "audio/x-pn-realaudio");
    defaultMimeTypes.put(".rm", "audio/x-pn-realaudio");
    defaultMimeTypes.put(".rpm", "audio/x-pn-realaudio-plugin");
    defaultMimeTypes.put(".ra", "audio/x-realaudio");
    defaultMimeTypes.put(".ras", "image/x-cmu-raster");
    defaultMimeTypes.put(".rgb", "image/x-rgb");
    defaultMimeTypes.put(".rtx", "text/richtext");
    defaultMimeTypes.put(".rtf", "text/rtf");

    defaultMimeTypes.put(".smi", "application/smil");
    defaultMimeTypes.put(".smil", "application/smil");
    defaultMimeTypes.put(".sml", "application/smil");
    defaultMimeTypes.put(".skp", "application/x-koan");
    defaultMimeTypes.put(".skd", "application/x-koan");
    defaultMimeTypes.put(".skt", "application/x-koan");
    defaultMimeTypes.put(".skm", "application/x-koan");
    defaultMimeTypes.put(".src", "application/x-wais-source");
    defaultMimeTypes.put(".sh", "application/x-sh");
    defaultMimeTypes.put(".shar", "application/x-shar");
    defaultMimeTypes.put(".swf", "application/x-shockwave-flash");
    defaultMimeTypes.put(".sit", "application/x-stuffit");
    defaultMimeTypes.put(".spl", "application/x-futuresplash");
    defaultMimeTypes.put(".sv4cpio", "application/x-sv4cpio");
    defaultMimeTypes.put(".sv4crc", "application/x-sv4crc");
    defaultMimeTypes.put(".snd", "audio/basic");
    defaultMimeTypes.put(".sgml", "text/sgml");
    defaultMimeTypes.put(".sgm", "text/sgml");

    defaultMimeTypes.put(".tgz", "application/octet-stream");
    defaultMimeTypes.put(".tar", "application/x-tar");
    defaultMimeTypes.put(".tcl", "application/x-tcl");
    defaultMimeTypes.put(".tex", "application/x-tex");
    defaultMimeTypes.put(".texinfo", "application/x-texinfo");
    defaultMimeTypes.put(".texi", "application/x-texinfo");
    defaultMimeTypes.put(".t", "application/x-troff");
    defaultMimeTypes.put(".tr", "application/x-troff");
    defaultMimeTypes.put(".roff", "application/x-troff");
    defaultMimeTypes.put(".tiff", "image/tiff");
    defaultMimeTypes.put(".tif", "image/tiff");
    defaultMimeTypes.put(".txt", "text/plain");
    defaultMimeTypes.put(".tsv", "text/tab-separated-values");

    defaultMimeTypes.put(".ustar", "application/x-ustar");

    defaultMimeTypes.put(".vcd", "application/x-cdlink");
    defaultMimeTypes.put(".vrml", "model/vrml");

    defaultMimeTypes.put(".wav", "audio/x-wav");
    defaultMimeTypes.put(".wax", "audio/x-ms-wax");
    defaultMimeTypes.put(".wrl", "model/vrml");
    defaultMimeTypes.put(".wma", "audio/x-ms-wma");
    defaultMimeTypes.put(".wml", "text/vnd.wap.wml");
    defaultMimeTypes.put(".wmls", "text/vnd.wap.wmlscript");
    defaultMimeTypes.put(".wmlc", "application/vnd.wap.wmlc");
    defaultMimeTypes.put(".wmlsc", "application/vnd.wap.wmlscript");
    defaultMimeTypes.put(".wbmp", "image/vnd.wap.wbmp");

    defaultMimeTypes.put(".xls", "application/vnd.ms-excel");
    defaultMimeTypes.put(".xbm", "image/x-xbitmap");
    defaultMimeTypes.put(".xpm", "image/x-xpixmax");
    defaultMimeTypes.put(".xwd", "image/x-xwindowdump");
    defaultMimeTypes.put(".xml", "text/xml");

    defaultMimeTypes.put(".zip", "application/zip");
    defaultMimeTypes.put(".z", "application/octet-stream");

  }
}
