package pl.aislib.util.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple servlet which can be used as a superclass.
 *
 * Overriden <code>doPost</code> and <code>doGet</code> methods calls
 * <code>doPostOrGet</code> method.
 *
 * @author Tomasz Pik, AIS.PL
 * @since 0.5.2
 */
public abstract class BaseServlet extends HttpServlet {

  /**
   * Call {@link #doPostOrGet}.
   *
   * @param request request the client made of the servlet.
   * @param response response the servlet returns to the client.
   * @throws ServletException if request cannot be handled.
   * @throws IOException if an input or output error occurs while the servlet is handling request.
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPostOrGet(request, response);
  }

  /**
   * Call {@link #doPostOrGet}.
   *
   * @param request request the client made of the servlet.
   * @param response response the servlet returns to the client.
   * @throws ServletException if request cannot be handled.
   * @throws IOException if an input or output error occurs while the servlet is handling request.
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPostOrGet(request, response);
  }

  /**
   * This method should be implemented in subclasses.
   *
   * @param request request the client made of the servlet.
   * @param response response the servlet returns to the client.
   * @throws ServletException if request cannot be handled.
   * @throws IOException if an input or output error occurs while the servlet is handling request.
   */
  public abstract void doPostOrGet(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException;
}

