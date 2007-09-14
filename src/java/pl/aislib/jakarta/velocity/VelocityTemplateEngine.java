package pl.aislib.jakarta.velocity;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;

import org.apache.velocity.app.VelocityEngine;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;


import pl.aislib.fm.Application;
import pl.aislib.fm.TemplateEngine;
import pl.aislib.fm.TemplateEngineException;

/**
 * TemplateEngine implementation using Velocity.
 *
 * This class uses <em>Separate Instance</em> model of using Velocity.
 *
 * @author Tomasz Pik
 */
public class VelocityTemplateEngine extends TemplateEngine {

  private Map predefinedValues;
  private VelocityEngine engine;

  /**
   * Initialize the engine using given <code>Map</code> as
   * configuration.
   */
  public VelocityTemplateEngine(Map configuration) throws TemplateEngineException {
    initVelocity(configuration);
  }

  /**
   * Initialize the engine to load templates through <code>ServletContextResourceLoader</code>.
   *
   * @param context ServletContext to use
   * @param prefix directory within web application root directory
   */
  public VelocityTemplateEngine(ServletContext context, String prefix) throws TemplateEngineException {
    Map configuration = new HashMap();
    VelocityConfigHelper.configureServletContextLoader(configuration, context, prefix);
    initVelocity(configuration);
  }

  /**
   * Appends pair key and values from <code>predefinedValues</code> map to main
   * map which is used to evaluate velocity template.
   * WARNING: These keys/values (from <code>predefinedValues</code>) will be allways present in
   * evaluation map.
   *
   * @param predefinedValues map with predefined values. This map should be created with pl.aislib.lang.ClassConstUtils
   */
  public void appendPredefinedValues(Map predefinedValues) {
    if(this.predefinedValues == null) {
      this.predefinedValues = new HashMap();
    }

    this.predefinedValues.putAll(predefinedValues);
  }

  public String evaluate(Object o, Map map) {
    VelocityTemplate template = (VelocityTemplate) o;
    if (map != null) {
      template.setValues(map);
    }

    if(predefinedValues != null) {
      // check if there are any conflicts
      if( map != null ) {
        for (Iterator iter = predefinedValues.keySet().iterator(); iter.hasNext();) {
          Object key = (Object) iter.next();
          if(map.containsKey(key)) {
            engine.warn("Conflict found! key: "+key+", predefined value: "+ predefinedValues.get(key)+", map value: "+map.get(key));
          }
        }
      }

      template.setValues(predefinedValues);
    }

    return template.toString();
  }

  public boolean isTemplate(Object o) {
    return o instanceof VelocityTemplate;
  }

  public Object load(Application a, HttpServletRequest req, HttpServletResponse res, String name)
  throws TemplateEngineException {
    try {
      Template template = engine.getTemplate(name);
      return new VelocityTemplate(template);
    } catch (ResourceNotFoundException rnfe) {
      throw new TemplateEngineException(rnfe);
    } catch (ParseErrorException pef) {
      throw new TemplateEngineException(pef);
    } catch (Exception e) {
      throw new TemplateEngineException(e);
    }
  }

  private void initVelocity(Map configuration) throws TemplateEngineException {
    engine = new VelocityEngine();
    Iterator keys = configuration.keySet().iterator();
    while (keys.hasNext()) {
      Object key = keys.next();
      Object value = configuration.get(key);
      if (value != null) {
        engine.addProperty((String) key, value);
      }
    }
    try {
      engine.init();
    } catch (Exception e) {
      throw new TemplateEngineException("Problems during Velocity initialization", e);
    }
  }
}

