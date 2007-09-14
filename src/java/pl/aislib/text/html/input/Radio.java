package pl.aislib.text.html.input;

import pl.aislib.text.html.attrs.AbstractHTMLAttribute;

/**
 * Represents HTML Radiobutton (input element with type set to 'radio').
 * @author Michal Jastak
 * @version $Revision: 1.1.1.1 $
 * @see Input
 * @since AISLIB 0.2
 */
public class Radio extends Input {

  /**
   * Constructor.
   */
  public Radio() {
    super();
    AbstractHTMLAttribute attr = getAttribute("type");
    attr.setValue("radio");
    attr.lockValue();
  }
  
} // class
