package pl.aislib.test.text.html.render;

import java.util.ArrayList;
import java.util.List;

import pl.aislib.text.html.render.FlyweightOptionRenderer;
import pl.aislib.text.html.render.ListRenderer;

import junit.framework.TestCase;

/**
 *
 * @author Tomasz Pik, AIS.PL
 */
public class ListRendererTest extends TestCase {

  private List sourceList;

  /**
   * Constructor for ListRendererTest.
   * @param arg0
   */
  public ListRendererTest(String arg0) {
    super(arg0);
  }

  /**
   * Prepare internally used structures
   */
  public void setUp() {
    int size = 10;
    sourceList = new ArrayList(size);
    for (int i = 0; i < size; i++) {
      sourceList.add(new Integer(i));
    }
  }

  /**
   * Test documented order of method calls during <code>render</code> invocation.
   */
  public void testOrderOfMethodCalls() {
    ListRenderer.renderSelect("name", sourceList, new SimpleOptionRenderer(), null);
  }

  class SimpleOptionRenderer extends FlyweightOptionRenderer {
    private Object object;

    public void setObject(Object object) {
      this.object = object;
    }

    public String getContent() {
      return object.toString();
    }

    public String getValue() {
      return object.toString();
    }
  }
}
