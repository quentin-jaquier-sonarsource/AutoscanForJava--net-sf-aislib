package pl.aislib.fm.forms;

/**
 * Core interface for validation of strings.
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.3 $
 * @since AISLIB 0.1
 */
public interface Validator extends BaseValidator {

  /**
   * @param value a string.
   * @return a validated object.
   * @throws ValidateException if the validation was not successful.
   */
  public Object validate(String value) throws ValidateException;

} // Validator interface
