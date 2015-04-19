package pl.aislib.io;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;

/**
 * <code>PrintStream</code> supporting ANSI Control Sequences.
 *
 * <code>ANSIPrintStream</code> decorates {@link PrintStream}
 * providing support for ANSI Control Sequences.
 * Support for Control Sequeces is controlled by
 * <code>isFullANSI</code> argument of {@link #ANSIPrintStream(OutputStream, boolean)}
 * constructor.
 *
 * @author Tomasz Pik
 * @since AISLIB 0.2
 */
public class ANSIPrintStream extends PrintStream {

  private boolean isFullANSI = true;

  /**
   * Decorate <code>OutputStream</code> with full ANSI support.
   *
   * @param stream {@link OutputStream} to decorate.
   */
  public ANSIPrintStream(OutputStream stream) {
    super(stream);
  }

  /**
   * Decorate <code>OutputStream</code> with possible full ANSI support.
   *
   * @param stream {@link OutputStream} to decorate.
   * @param isFullANSI if <code>false</code>, ANSI Control Sequences
   *         will be ignored.
   */
  public ANSIPrintStream(OutputStream stream, boolean isFullANSI) {
    super(stream);
    this.isFullANSI = isFullANSI;
  }

  /**
   * Prints given Control Sequence.
   *
   * Prints given Control Sequence if <code>isFullANSI</code>
   * is enabled. Otherwise this method does nothing.
   *
   * @param mark ANSI Control Sequence.
   * @throws IOException in case of I/O problems.
   */
  public void print(ANSIConstants.ANSIConstant mark) throws IOException {
    if (isFullANSI) {
      super.print(mark.getRepresentation());
    }
  }

  /**
   * Prints given Control Sequence.
   *
   * Prints given Control Sequence  and new line character
   * if <code>isFullANSI</code> is enabled.
   * Otherwise this method prints only new line character.
   *
   * @param mark ANSI Control Sequence.
   * @throws IOException in case of I/O problems.
   */
  public void println(ANSIConstants.ANSIConstant mark) throws IOException {
    if (isFullANSI) {
      super.println(mark.getRepresentation());
    } else {
      super.println();
    }
  }

} // class
