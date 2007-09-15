package pl.aislib.util.xml;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;

/**
 * Implementation of {@link ErrorListener} using Jakarta Commons Logging Component.
 *
 * @author Tomasz Pik
 * @author Michal Jastak
 * @version $Revision: 1.3 $
 * @since AISLIB 0.4
 * @see <a href='http://jakarta.apache.org/commons/logging.html'>Jakarta Commons Logging Component</a>
 */
public class LogErrorListener implements ErrorListener {

 /**
  * Descibes if parsing should be stopped on all errors.
  * Setting this flag to <code>true</code> will stop parsing on error and fatal error, while
  * setting it to <code>false</code> (default value) will stop parsing only on fatal error.
  */
  protected boolean allErrorsAreFatal;

  /**
   * Jakarta Commons Logging Component used to store logs.
   */
  protected Log log;

  /**
   * Constructs LogErrorListener using specified {@link Log} as storage.
   * @param log Log which we want to use as storage
   * @exception NullPointerException when <code>log</code> is null
   */
  public LogErrorListener(Log log) {
    if (log == null) {
      throw new NullPointerException("log cannot be null");
    }
    this.log = log;
    allErrorsAreFatal = false;
  }

  /**
   * Modifies class behaviour on errors, see {@link LogErrorHandler#allErrorsAreFatal}
   *
   * @param allErrorsAreFatal flag describing new behaviour 
   */
  public void setAllErrorsAreFatal(boolean allErrorsAreFatal) {
    this.allErrorsAreFatal = allErrorsAreFatal;
  }

  /**
   * Receive notification of a recoverable error.
   *
   * Sends appropriate message to log storage (as <code>error</code>) and if it
   * is not forbidden by {@link LogErrorListener#allErrorsAreFatal} flag returns
   * back to SAX parser to continue document parsing.
   *
   * @param te The error information encapsulated in a SAX parse exception.
   *            force us to stop document parsing.
   * @throws TransformerException passed exception (if <code>allErrorsAreFatal</code>
   *          property is set to <code>true</code>.
   */
  public void error(TransformerException te) throws TransformerException {
    if (log.isErrorEnabled()) {
      log.error(te.getMessageAndLocation(), te);
    }
    if (allErrorsAreFatal) {
      throw te;
    }
  }
 
  /**
   * Receive notification of a non-recoverable error.
   * 
   * Sends appropriate message to log storage (as <code>fatal</code>)
   * and stops document parsing definitely.
   *
   * @param te The error information encapsulated in a SAX parse exception.
   * @exception TransformerException always when called
   */
  public void fatalError(TransformerException te) throws TransformerException {
    if (log.isFatalEnabled()) {
      log.fatal(te.getMessageAndLocation(), te);
    }
    throw te;
  }
 
  /**
   * Receive notification of a warning.
   *
   * Sends appropriate message to log storage (as <code>warn</code>) and
   * continue document parsing.
   *
   * @param te The warning information encapsulated in a TrAX parse exception.
   */
  public void warning(TransformerException te) {
    if (log.isWarnEnabled()) {
      log.warn(te.getMessageAndLocation(), te);
    }
  }
} // class

