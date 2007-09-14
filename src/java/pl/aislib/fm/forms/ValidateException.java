package pl.aislib.fm.forms;

/**
 * <code>Exception</code> subclass used in validation.
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.3 $
 * @since AISLIB 0.1
 */
public class ValidateException extends Exception {

  // Constructors

  /**
   * @param reason exception's message.
   */
  public ValidateException(String reason) {
    super(reason);
  }

} // ValidateException class
