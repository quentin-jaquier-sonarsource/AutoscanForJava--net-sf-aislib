package pl.aislib.text.html.render;

/**
 * Implements {@link OptionRenderer} using Flyweight pattern.
 * 
 * <p>Subclasses of this class are not thread safe.</p>
 * <p>Implementation of this class relly on order, that
 * <code>ListRenderer</code> calls <code>OptionRenderer</code>
 * methods. This is documented <a href='ListRenderer.html#order'>here</a>.</p>
 * 
 * <p>Following sequence of method calls is used during renering:
 * <pre>
 *   getValue(Object object) calls setObject(object) and getValue())
 *   getContent(Object object) calls getContent()
 * </pre>
 * 
 * @author Tomasz Pik, AIS.PL
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.5
 */
public abstract class FlyweightOptionRenderer implements OptionRenderer {

  private Object object;

  /**
   * @see pl.aislib.text.html.render.OptionRenderer#getValue(Object)
   */
  public final String getValue(Object object) {
    this.object = object;
    setObject(object);
    return getValue();
  }

  /**
   * @see pl.aislib.text.html.render.OptionRenderer#getContent(Object)
   * @throws IllegalStateException if <code>getContent</code> is called
   *         before <code>getValue</code> on given object as argument.
   */
  public final String getContent(Object object) {
    if (object != this.object) {
      throw new IllegalStateException("getValue should be called before getContent");
    }
    return getContent();
  }

  /**
   * Sets <code>object</code> which will be rendered.
   * 
   * @param object object to set
   */
  public abstract void setObject(Object object);

  /**
   * @return <em>HTML Option</em> value attribute
   */
  protected abstract String getValue();
  
  /**
   * @return <em>HTML Option</em> content
   */
  protected abstract String getContent();
}
