package pl.aislib.fm.forms;

/**
 * Core interface for validation of array of strings.
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 * @since AISLIB 0.1
 */
public interface ArrayValidator extends BaseValidator {

  /**
   * @param values an array of strings.
   * @return an array of validated objects.
   * @throws ValidateException if the validation was not successful.
   */
  public Object[] validate(String[] values) throws ValidateException;

} // ArrayValidator interface
