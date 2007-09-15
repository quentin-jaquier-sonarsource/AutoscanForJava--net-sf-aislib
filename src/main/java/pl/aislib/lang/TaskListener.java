package pl.aislib.lang;

import java.util.EventListener;

/**
 * The listener interface for receiving task events.
 *
 * @author Tomasz Sandecki
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.3
 */
public interface TaskListener extends EventListener {

  /**
   * This method is called after {@link Thread#start} but before {@link Task#process}.
   */
  public void taskStarted();

  /**
   * This method is called after {@link Task#process} if {@link Task#process} returns
   * result object.
   *
   * @param result result object.
   */
  public void taskFinished(Object result);

  /**
   * This method is called after {@link Task#process} if {@link Task#process} throws
   * exception.
   *
   * @param e thrown exception.
   */
  public void taskFailed(TaskProcessingException e);

} // interface
