package pl.aislib.fm.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import pl.aislib.fm.Application;
import pl.aislib.fm.Constants;
import pl.aislib.fm.TemplateEngine;
import pl.aislib.fm.TemplateEngineException;

/**
 * Support class for multi language functionality.
 *
 * @author Andrzej Luszczyk, AIS.PL
 * @author Tomasz Pik, AIS.PL
 */
public class I18NSupport implements Serializable {

  private String defaultLanguage;
  private List acceptedLanguages;

  /**
   * Constructor for LanguageSupport.
   *
   * @param defaultLanguage default language
   * @param acceptedLanguages list of accepted languages
   */
  public I18NSupport(String defaultLanguage, List acceptedLanguages) {
    // TODO:
    // 1 do accepted contains only strings
    // 2 do accepted contains default
    // 3 copy accepted as immutable
    this.defaultLanguage = defaultLanguage;
    this.acceptedLanguages = acceptedLanguages;
  }

  /**
   * Method processes setting the {@link Constants#LANG} attribute on the {@link HttpServletRequest}.
   * First it tries to get <code>lang</code> parameter from {@link HttpServletRequest}.
   * If it failed then it tries to get {@link Constants#LANG} attribute from
   * {@link javax.servlet.http.HttpSession}. If it also failed then it tries to recognize language
   * from header <code>Accept-Language</code>. If all methods doesn't get any
   * result, it sets the {@link Constants#LANG} attribute to <code>space</code>.
   *
   * @param request {@link HttpServletRequest}.
   */
  public void process(HttpServletRequest request) {
    String lang = request.getParameter("lang");
    if (lang == null) {
      lang = (String) request.getSession().getAttribute(Constants.LANG);
    }
    if (lang == null) {
      String headerLang = request.getHeader("Accept-Language");
      if (headerLang != null) {
        int i = headerLang.indexOf(",");
        if (i > 0) {
          lang = headerLang.substring(0, i);
        } else {
          lang = headerLang;
        }
      }
    }
    if (lang == null) {
      lang = defaultLanguage;
    }

    request.setAttribute(Constants.LANG, lang);
    request.getSession().setAttribute(Constants.LANG, lang);
  }

  /**
   * Decorate given engine with i18n support.
   *
   * Use following method of loading templates
   * <ol>
   *   <li>get <code>lang</code> from request</li>
   *   <li>try to load template with name concatenated with <code>_</code>
   *      and value of <code>lang</code></li>
   *   <li>if it fails, load template without value of <code>lang</code></li>
   * </ol>
   *
   * @param engine to decorate
   * @return TemplateEngine with i18n support
   */
  public TemplateEngine localize(TemplateEngine engine) {
    return new LocalizedTemplateEngine(engine);
  }

  class LocalizedTemplateEngine extends TemplateEngine implements Serializable {

    private TemplateEngine encapsulatedEngine;

    LocalizedTemplateEngine(TemplateEngine encapsuatedEngine) {
      this.encapsulatedEngine = encapsuatedEngine;
    }

    /**
     * @see pl.aislib.fm.TemplateEngine#load(Application, HttpServletRequest, HttpServletResponse, String)
     */
    public Object load(Application application, HttpServletRequest request,
        HttpServletResponse response, String templateName)
      throws TemplateEngineException {
      Object result = null;
      String lang = (String) request.getAttribute(Constants.LANG);
      try {
        templateName = StringUtils.replace(templateName, "${lang}", lang);
        result = encapsulatedEngine.load(application, request, response, templateName);
      } catch (TemplateEngineException tee) {
        ; // ignore this
      }
      if (result == null) {
        result = encapsulatedEngine.load(application, request, response, templateName);
      }
      return result;
    }

    /**
     * @see pl.aislib.fm.TemplateEngine#evaluate(java.lang.Object, java.util.Map)
     */
    public String evaluate(Object template, Map parameters) throws TemplateEngineException {
      return encapsulatedEngine.evaluate(template, parameters);
    }

    /**
     * @see pl.aislib.fm.TemplateEngine#isTemplate(java.lang.Object)
     */
    public boolean isTemplate(Object template) {
      return encapsulatedEngine.isTemplate(template);
    }
  }

}
