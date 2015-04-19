package pl.aislib.text.html.render;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.aislib.text.html.Option;
import pl.aislib.text.html.Select;
import pl.aislib.text.html.TextNode;

/**
 * Helper class for rendering Java List to HTML Objects.
 *
 * <p><a id="order">Using <code>OptionRenderer</code> if first call <code>getValue</code>
 * and <code>getContent</code> method later.</p>
 *
 * @author Michal Jastak
 * @since AISLIB 0.5
 */
public class ListRenderer {

  /**
   * Renders HTML Select.
   *
   * @param selectName name of <em>HTML Select</em>
   * @param valuesList List of values which will be used for rendering <em>HTML Options</em> for this select
   * @param contentMap content mapping for <code>valuesList</code> used for rendering <em>HTML Options</em>
   *                   for this select
   * @param selected preselected value for this <em>HTML Select</em>
   * @return rendered <em>HTML Select</em>
   */
  public static Select renderSelect(String selectName, List valuesList, Map contentMap, Object selected) {
    Select result = new Select();

    if (selectName != null) {
      result.setAttributeValue("name", selectName);
    }

    if ((valuesList == null) || (contentMap == null)) {
      return result;
    }

    for (Iterator it = valuesList.iterator(); it.hasNext();) {
      Object obj = it.next();
      if (obj != null) {
        Option option = new Option();

        option.setAttributeValue("value", obj);
        option.addContent(new TextNode(contentMap.get(obj).toString()));

        if ((selected != null) && obj.equals(selected)) {
          option.setAttributeValue("selected", "selected");
        }

        result.addContent(option);
      }
    }

    return result;
  }

  /**
   * Renders HTML Select.
   *
   * @param selectName name of <em>HTML Select</em>
   * @param list List of <em>Objects</em> which will be rendered to <em>HTML Select</em> content,
   *             using <code>renderer</code>
   * @param renderer used for rendering each object from the <code>list</code> to corresponding <em>HTML Option</em>
   * @param selected preselected object for this <em>HTML Select</em>
   * @return rendered <em>HTML Select</em>
   */
  public static Select renderSelect(String selectName, List list, OptionRenderer renderer, Object selected) {
    Select result = new Select();
    OptionRenderer optionRenderer = renderer;

    if (optionRenderer == null) {
      optionRenderer = new DummyOptionRenderer();
    }

    if (selectName != null) {
      result.setAttributeValue("name", selectName);
    }


    if (list == null) {
      return result;
    }

    for (Iterator it = list.iterator(); it.hasNext();) {
      Object obj = it.next();
      if (obj != null) {
        Option option = new Option();

        option.setAttributeValue("value", optionRenderer.getValue(obj));
        option.addContent(new TextNode(optionRenderer.getContent(obj)));

        if ((selected != null) && obj.equals(selected)) {
          option.setAttributeValue("selected", "selected");
        }
        result.addContent(option);
      }
    }
    return result;
  }

} // class ListRenderer
