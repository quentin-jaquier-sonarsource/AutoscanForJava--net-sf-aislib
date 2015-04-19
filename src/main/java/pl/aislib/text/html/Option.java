package pl.aislib.text.html;

import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.EventsAttributesSet;
import pl.aislib.text.html.attrs.CDataAttribute;
import pl.aislib.text.html.attrs.EnumeratedAttribute;

/**
 * Represents HTML 'option' element.
 * Right now options may have:
 * <ul>
 * <li>following attributes:
 *   <ul>
 *     <li>CoreAttrs (see: {@link CoreAttributesSet})</li>
 *     <li>Events (see: {@link EventsAttributesSet})</li>
 *     <li>disabled</li><li>label</li><li>selected</li><li>value</li>
 *   </ul>
 * </li>
 * <li>any content</li>
 * </ul>
 * See {@link EnumeratedAttribute} for <code>disabled</code> and <code>selected</code>.
 * See {@link CDataAttribute} for <code>label</code> and <code>value</code>.
 * @author Michal Jastak
 * @since AISLIB 0.1
 */
public class Option extends AbstractHTMLObject {

  /**
   *
   */
  public Option() {
    super("option");
    addAttributesSet(new CoreAttributesSet());
    addAttributesSet(new EventsAttributesSet());
    AttributesSet selectAttrs = new AttributesSet();
    try {
      selectAttrs.add(new CDataAttribute("label"));
      selectAttrs.add(new CDataAttribute("value"));
      {
        EnumeratedAttribute selected = new EnumeratedAttribute("selected");
        selected.addAllowedValue("selected");
        selectAttrs.add(selected);
      }
      {
        EnumeratedAttribute disabled = new EnumeratedAttribute("disabled");
        disabled.addAllowedValue("disabled");
        selectAttrs.add(disabled);
      }
    } catch (ClassNotFoundException cnfe) {
    }
    addAttributesSet(selectAttrs);
  }

} // class
