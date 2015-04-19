package pl.aislib.fm;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Generic Template Engine class.
 *
 * This class should be extended to implement template engine for specific environment.
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Michal Jastak, AIS.PL</td></tr>
 * </table>
 * @since AISLIB 0.1
 */
public abstract class TemplateEngine {

  // Abstract methods

  /**
   * Gets specified template.
   *
   * @param application Parent {@link Application} description.
   * @param request {@link HttpServletRequest} object.
   * @param response {@link HttpServletResponse} object.
   * @param templateName Template which we want to load.
   * @return specified template object.
   * @throws TemplateEngineException exception.
   */
  public abstract Object load(
    Application application, HttpServletRequest request, HttpServletResponse response, String templateName
  ) throws TemplateEngineException;

  /**
   * Evaluates template.
   *
   * @param template <code>Template</code> object.
   * @param parameters {@link Map} containing evaluation parameters.
   * @return {@link String} created during evaluation process.
   * @throws TemplateEngineException exception.
   */
  public abstract String evaluate(Object template, Map parameters) throws TemplateEngineException;

  /**
   * Checks if given object is a template.
   *
   * @param object Object which should be checked.
   * @return <code>true</code> if object given as argument is implementation of Template, <code>false</code> otherwise.
   */
  public abstract boolean isTemplate(Object object);

} // TemplateEngine class
