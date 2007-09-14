package pl.aislib.fm.forms;

/**
 * Entity interface.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.2 $
 */
public interface IEntity {

  /**
   * @return true if entity is dynamic.
   */
  boolean isDynamic();

  /**
   * @return name of the entity.
   */
  String getName();

} // IEntity interface
