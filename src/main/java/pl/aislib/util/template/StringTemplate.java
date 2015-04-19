package pl.aislib.util.template;

import java.io.Writer;
import java.io.IOException;

/**
 * Interface represens dynamic output which may be treat as <code>String</code>.
 *
 * @author Tomasz Pik, AIS.PL
 */
public interface StringTemplate extends Template {

  /**
   * Process template content and write to writer.
   *
   * @param writer writer for write result of template processing.
   * @throws IOException in case of problems.
   */
  public void writeTo(Writer writer) throws IOException;

  /**
   * Process template content and return result as <code>String</code>.
   *
   * @return String contains result of template processing.
   */
  public String toString();
}
