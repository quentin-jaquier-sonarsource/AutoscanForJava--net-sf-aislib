package pl.aislib.text.html;

import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.CDataAttribute;
import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.EnumeratedAttribute;
import pl.aislib.text.html.attrs.EventsAttributesSet;

/**
 * Represents HTML 'select' element.
 * Right now select may have:
 * <ul>
 * <li>following attributes:
 *   <ul>
 *     <li>CoreAttrs</li>
 *     <li>EventsAttrs</li>
 *     <li>disabled</li>
 *     <li>multiple</li>
 *     <li>name</li>
 *     <li>onblur</li>
 *     <li>onchange</li>
 *     <li>onfocus</li>
 *     <li>tabindex</li>
 *   </ul>
 * </li>
 * <li>any content</li>
 * </ul>
 * <p><b>Change Log:</b><br />
 * <dl>
 *   <dt>AISLIB 0.1</dt>
 *   <dd>allows only <code>CoreAttrs</code> and <code>name</code> attributes<br /></dd>
 *   <dt>AISLIB 0.2</dt>
 *   <dd>adds <code>EventsAttrs</code>, <code>onblur</code>, <code>onchange</code> and
 *       <code>onfocus</code> attributes</dd>
 *   <dt>AISLIB 0.3</dt>
 *   <dd>adds <code>disabled</code>, <code>multiple</code> and <code>tabindex</code> attributes</dd>
 * </dl>
 * </p>
 * @author Michal Jastak, AIS.PL
 * @see CoreAttributesSet
 * @see EventsAttributesSet
 * @since AISLIB 0.1
 */
public class Select extends AbstractHTMLObject {

  /**
   * Constructor.
   */
  public Select() {
    super("select");
    addAttributesSet(new CoreAttributesSet());
    addAttributesSet(new EventsAttributesSet());
    AttributesSet selectAttrs = new AttributesSet();
    try {
      EnumeratedAttribute attr = new EnumeratedAttribute("disabled");
      attr.addAllowedValue("disabled");
      selectAttrs.add(attr);

      attr = new EnumeratedAttribute("multiple");
      attr.addAllowedValue("multiple");
      selectAttrs.add(attr);

      selectAttrs.add(new CDataAttribute("name"));
      selectAttrs.add(new CDataAttribute("onblur"));
      selectAttrs.add(new CDataAttribute("onchange"));
      selectAttrs.add(new CDataAttribute("onfocus"));
      selectAttrs.add(new CDataAttribute("size"));
      selectAttrs.add(new CDataAttribute("tabindex"));
    } catch (ClassNotFoundException cnfe) {
    }
    addAttributesSet(selectAttrs);
  }

} // class
