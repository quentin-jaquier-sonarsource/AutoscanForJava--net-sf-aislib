package pl.aislib.lang;

/**
 * @author Tomasz Sandecki
 * @version $Revision: 1.1.1.1 $
 * @since AISLIB 0.3
 */
public interface ExtendedTaskListener extends TaskListener {

 /**
  * This method is called if Task calls {@link Task#updateProgress}
  *
  * @param progress a float number.
  */
  public void taskProgress(float progress);

 /**
  * This method is called if Task calls {@link Task#fireTaskEvent}
  *
  * @param event an object.
  */
  public  void taskNotify(Object event);

} // interface
