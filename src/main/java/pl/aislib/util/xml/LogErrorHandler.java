package pl.aislib.util.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.apache.commons.logging.Log;

/**
 * Implementation of {@link ErrorHandler} using Jakarta Commons Logging Component.
 *
 * @author Tomasz Pik
 * @author Michal Jastak
 * @since AISLIB 0.1
 * @see <a href='http://jakarta.apache.org/commons/logging.html'>Jakarta Commons Logging Component</a>
 */
public class LogErrorHandler implements ErrorHandler {

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
   * Constructs LogErrorHandler using specified {@link Log} as storage.
   * @param log Log which we want to use as storage
   * @exception NullPointerException when <code>log</code> is null
   */
  public LogErrorHandler(Log log) {
    if (log == null) {
      throw new NullPointerException("log cannot be null");
    }
    this.log = log;
    allErrorsAreFatal = false;
  }

  /**
   * Modifies class behaviour on errors, see {@link LogErrorHandler#allErrorsAreFatal}
   * @param allErrorsAreFatal flag describing new behaviour
   */
  public void setAllErrorsAreFatal(boolean allErrorsAreFatal) {
    this.allErrorsAreFatal = allErrorsAreFatal;
  }

  /**
   * Receive notification of a recoverable error.
   *
   * Sends appropriate message to log storage (as <code>errror</code>) and
   * if it is not forbidden by {@link LogErrorHandler#allErrorsAreFatal}
   * flag returns back to SAX parser to continue document parsing.
   *
   * @param spe The error information encapsulated in a SAX parse exception.
   * @exception SAXException when {@link LogErrorHandler#allErrorsAreFatal}
   * force us to stop document parsing
   */
  public void error(SAXParseException spe) throws SAXException {
    if (log.isErrorEnabled()) {
      log.error(format(spe), spe);
    }
    if (allErrorsAreFatal) {
      throw spe;
    }
  }

  /**
   * Receive notification of a non-recoverable error.
   *
   * Sends appropriate message to log storage (as <code>fatal</code>)
   * and stops document parsing definitely.
   *
   * @param spe The error information encapsulated in a SAX parse exception.
   * @exception SAXException always when called
   */
  public void fatalError(SAXParseException spe) throws SAXException {
    if (log.isFatalEnabled()) {
      log.fatal(format(spe), spe);
    }
    throw spe;
  }

  /**
   * Receive notification of a warning.
   *
   * Sends appropriate message to log storage (as <code>warn</code>)
   * and continue document parsing.
   *
   * @param spe The warning information encapsulated in a SAX parse exception.
   */
  public void warning(SAXParseException spe) throws SAXParseException {
    if (log.isWarnEnabled()) {
      log.warn(format(spe), spe);
    }
    if (allErrorsAreFatal) {
      throw spe;
    }
  }

  /**
   * Add line and column numbers to exception message.
   *
   * Numbers are added only if they are different from <code>-1</code>.
   *
   * @param spe to format
   * @return String contains line and numbers concatenated with exception message.
   */
  private String format(SAXParseException spe) {
    int lineNo = spe.getLineNumber();
    int colNo  = spe.getColumnNumber();
    return ((lineNo != -1) ? "Line: " + lineNo : "")
         + ((colNo != -1) ? " Column: " + colNo : "")
         + " -- " + spe.getMessage();
  }

} // class
