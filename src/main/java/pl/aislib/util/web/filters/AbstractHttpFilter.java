package pl.aislib.util.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base class for http-aware filters.
 *
 * @author Tomasz Pik, AIS.PL
 */
public abstract class AbstractHttpFilter implements Filter {

  /**
   * <code>FilterConfig</code> instance, passed in {@link #init(FilterConfig)}
   */
  protected FilterConfig config;

  /**
   * Store <code>config</code> for future use and call {@link #init()}.
   *
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public final void init(FilterConfig _config) throws ServletException {
    this.config = _config;
    init();
  }

  /**
   * Calls {@link #doFilter(HttpServletRequest, HttpServletResponse, FilterChain)}.
   *
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  public final void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
    throws IOException, ServletException {
    HttpServletRequest request = null;
    HttpServletResponse response = null;
    try {
      request = (HttpServletRequest) req;
    } catch (ClassCastException cce) {
      throw new ServletException("request is not HttpServletRequest", cce);
    }
    try {
      response = (HttpServletResponse) resp;
    } catch (ClassCastException cce) {
      throw new ServletException("response is not HttpServletResponse", cce);
    }
    doFilter(request, response, chain);
  }

  /**
   * Handle filter logic.
   *
   * @param req request the client made of the filter.
   * @param resp response the servlet filter to the client.
   * @param chain application execution flow.
   * @throws ServletException if request cannot be handled.
   * @throws IOException if an input or output error occurs while the servlet is handling request.
   */
  public abstract void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
    throws IOException, ServletException;

  /**
   * Empty method to be ovverriden.
   *
   * @throws ServletException if filter cannot be initialized.
   */
  public void init() throws ServletException {
  }

  /**
   * Empty method to be ovverriden.
   *
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
  }
}
