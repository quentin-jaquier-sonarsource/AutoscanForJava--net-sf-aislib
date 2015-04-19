package pl.aislib.text.html.input;

import pl.aislib.text.html.AbstractHTMLObject;
import pl.aislib.text.html.attrs.AttributesSet;
import pl.aislib.text.html.attrs.CDataAttribute;
import pl.aislib.text.html.attrs.CoreAttributesSet;
import pl.aislib.text.html.attrs.EnumeratedAttribute;
import pl.aislib.text.html.attrs.EventsAttributesSet;

/**
 * Represents HTML Input element.
 * Right now Input may have:
 * <ul>
 * <li>following attributes:
 *   <ul>
 *     <li>CoreAttrs</li>
 *     <li>EventsAttrs</li>
 *     <li>accept</li>
 *     <li>accesskey</li>
 *     <li>alt</li>
 *     <li>checked</li>
 *     <li>disabled</li>
 *     <li>ismap</li>
 *     <li>maxlength</li>
 *     <li>name</li>
 *     <li>onblur</li>
 *     <li>onchange</li>
 *     <li>onfocus</li>
 *     <li>onselect</li>
 *     <li>readonly</li>
 *     <li>size</li>
 *     <li>src</li>
 *     <li>tabindex</li>
 *     <li>type</li>
 *     <li>usemap</li>
 *     <li>value</li>
 *   </ul>
 * </li>
 * <li>any content</li>
 * </ul>
 * </p>
 *
 * @author Michal Jastak
 * @see CoreAttributesSet
 * @see EventsAttributesSet
 * @since AISLIB 0.2
 */
public class Input extends AbstractHTMLObject {

  /**
   * Constructor.
   */
  public Input() {
    super("input");
    addAttributesSet(new CoreAttributesSet());
    addAttributesSet(new EventsAttributesSet());
    AttributesSet inputAttrs = new AttributesSet();
    try {
      inputAttrs.add(new CDataAttribute("accept"));
      inputAttrs.add(new CDataAttribute("accesskey"));
      inputAttrs.add(new CDataAttribute("alt"));

      EnumeratedAttribute enAttr = new EnumeratedAttribute("checked");
      enAttr.addAllowedValue("checked");
      inputAttrs.add(enAttr);

      enAttr = new EnumeratedAttribute("disabled");
      enAttr.addAllowedValue("disabled");
      inputAttrs.add(enAttr);

      enAttr = new EnumeratedAttribute("readonly");
      enAttr.addAllowedValue("readonly");
      inputAttrs.add(enAttr);

      inputAttrs.add(new CDataAttribute("ismap"));
      inputAttrs.add(new CDataAttribute("maxlength"));
      inputAttrs.add(new CDataAttribute("name"));
      inputAttrs.add(new CDataAttribute("onblur"));
      inputAttrs.add(new CDataAttribute("onchange"));
      inputAttrs.add(new CDataAttribute("onfocus"));
      inputAttrs.add(new CDataAttribute("onselect"));
      inputAttrs.add(new CDataAttribute("size"));
      inputAttrs.add(new CDataAttribute("src"));
      inputAttrs.add(new CDataAttribute("tabindex"));
      inputAttrs.add(new CDataAttribute("usemap"));

      enAttr = new EnumeratedAttribute("type");
      enAttr.addAllowedValue("button");
      enAttr.addAllowedValue("checkbox");
      enAttr.addAllowedValue("file");
      enAttr.addAllowedValue("hidden");
      enAttr.addAllowedValue("image");
      enAttr.addAllowedValue("password");
      enAttr.addAllowedValue("radio");
      enAttr.addAllowedValue("reset");
      enAttr.addAllowedValue("submit");
      enAttr.addAllowedValue("text");
      inputAttrs.add(enAttr);

      inputAttrs.add(new CDataAttribute("value"));
    } catch (ClassNotFoundException cnfe) {
    }
    addAttributesSet(inputAttrs);
  }

} // class
