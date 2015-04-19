package pl.aislib.text.html.input;

import pl.aislib.text.html.attrs.AbstractHTMLAttribute;

/**
 * Represents HTML Checkbox (input element with type set to 'checkbox').
 *
 * @author Michal Jastak, AIS.PL
 * @see Input
 * @since AISLIB 0.2
 */
public class Checkbox extends Input {

  /**
   * Constructor.
   */
  public Checkbox() {
    super();
    AbstractHTMLAttribute attr = getAttribute("type");
    attr.setValue("checkbox");
    attr.lockValue();
  }

} // class
