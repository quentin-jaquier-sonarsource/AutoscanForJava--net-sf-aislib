package pl.aislib.util.builders;

import java.util.Map;

import pl.aislib.fm.forms.FieldBuilder;

/**
 * Field builder for strings.
 *
 * Properties:
 * <ul>
 *   <li>
 *     <code>separator</code> - to separate parts one from another.
 *   </li>
 * </ul>
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class StringBuilder extends FieldBuilder {

  // Fields

  /**
   * Separator for string parts.
   */
  protected String separator = " ";


  // Public methods

  /**
   * Sets separator for string parts.
   *
   * @param separator separator for string parts.
   */
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  /**
   * @see pl.aislib.fm.forms.FieldBuilder#join(java.util.Map)
   */
  public String join(Map values) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0, n = values.size(); i < n; i++) {
      Object obj = values.get(String.valueOf(i));
      String str = null;
      if (obj instanceof String[]) {
        str = ((String[]) obj)[0];
      } else if (obj != null) {
        str = obj.toString();
      }
      if (str != null && str.trim().length() > 0) {
        if (i > 0) {
          buf.append(separator);
        }
        buf.append(str);
      }
    }
    return buf.toString();
  }

  /**
   * @see pl.aislib.fm.forms.FieldBuilder#split(java.lang.Object)
   */
  public Map split(Object value) {
    return null;
  }

} // pl.aislib.util.builders.StringBuilder class
