package pl.aislib.text.html;

import org.jdom.Element;
import org.jdom.EntityRef;

/**
 * Encapsulates entity.
 * <p>This is very simple class encapsulating entity.<p/>
 * @author Michal Jastak
 * @since AISLIB 0.2
 */
public class EntityNode extends AbstractHTMLObject {

  /**
   * Contains XML entity.
   */
  protected EntityRef entityRef;

  /**
   * Constructor. Creates {@link EntityRef} element encapsulating entity given by name.
   * @param entityName entity name
   */
  public EntityNode(String entityName) {
    super("_EntityNode");
    this.entityRef = new EntityRef(entityName);
  }

  /**
   * Used internally to reconstruct XML tree using relations between elements.
   * @param parent XML node which should act as parent for this entity.
   * @return null
   */
  protected Element toXML(Element parent) {
    if ((entityRef != null) && (parent != null)) {
      parent.addContent(entityRef);
    }
    return null;
  }

} // class
