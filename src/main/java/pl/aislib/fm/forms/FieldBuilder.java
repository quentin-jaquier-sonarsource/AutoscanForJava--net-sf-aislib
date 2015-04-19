package pl.aislib.fm.forms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class used for building complex fields out of other fields.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public abstract class FieldBuilder {

  /**
   * Field mapping.
   */
  protected Map mapping;


  // Constructors

  /**
   * Constructor for FieldBuilder.
   */
  public FieldBuilder() {
    super();

    mapping = new HashMap();
  }


  // Public methods

  /**
   * @param fieldParameter field builder name.
   * @param fieldName real field name.
   */
  public void addMapping(String fieldParameter, String fieldName) {
    mapping.put(fieldParameter, fieldName);
  }

  /**
   * @return mapping.
   */
  public Map getMapping() {
    return mapping;
  }

  /**
   * @param mapping mapping to be set.
   */
  public void setMapping(Map mapping) {
    this.mapping.putAll(mapping);
  }


  // Abstract methods

  /**
   * @param values values to be joined.
   * @return a string of joined values.
   */
  public abstract String join(Map values);

  /**
   * @param value an object.
   * @return map of objects split from the object.
   */
  public abstract Map split(Object value);


  // Protected methods

  /**
   * @param values a map.
   * @return map.
   */
  protected Map useMapping(Map values) {
    if (mapping == null || values == null) {
      return null;
    }

    Map result = new HashMap();

    for (Iterator i = mapping.entrySet().iterator(); i.hasNext();) {
      Map.Entry me = (Map.Entry) i.next();
      String fieldParameter = (String) me.getKey();
      String fieldName = (String) me.getValue();
      result.put(fieldName, values.get(fieldParameter));
    }

    return result;
  }

} // FieldBuilder class
