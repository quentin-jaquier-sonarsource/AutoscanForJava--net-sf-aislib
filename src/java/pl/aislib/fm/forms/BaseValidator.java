package pl.aislib.fm.forms;

/**
 * Core interface for validation.
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.4 $
 * @since AISLIB 0.1
 */
public interface BaseValidator {

  /**
   * Applies various data to implementations of this class.
   *
   * @param objName name of the object.
   * @param obj value of the object.
   * @return return applied object.
   */
  Object applyObject(String objName, Object obj);

} // BaseValidator interface
