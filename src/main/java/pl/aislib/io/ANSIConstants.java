package pl.aislib.io;

/**
 * @author Tomasz Pik, AIS.PL
 * @author Robert Blaszczyk, AIS.PL
 * @since AISLIB 0.2
 */
public interface ANSIConstants {

  /**
   * <b><code>\033[0m</code></b> constant.
   */
  public static final ANSIConstant RET      = new ANSIConstantImpl("\033[0m");

  /**
   * <b><code>\033[00;01m</code></b> constant.
   */
  public static final ANSIConstant BOLD     = new ANSIConstantImpl("\033[00;01m");

  /**
   * <b><code>\033[00;04m</code></b> constant.
   */
  public static final ANSIConstant UND      = new ANSIConstantImpl("\033[00;04m");

  /**
   * <b><code>\033[00;07m</code></b> constant.
   */
  public static final ANSIConstant REV      = new ANSIConstantImpl("\033[00;07m");

  /**
   * <b><code>\033[00;05m</code></b> constant.
   */
  public static final ANSIConstant BLINK    = new ANSIConstantImpl("\033[00;05m");

  /**
   * <b><code>\033[00;30m</code></b> constant.
   */
  public static final ANSIConstant BLACK    = new ANSIConstantImpl("\033[00;30m");

  /**
   * <b><code>\033[00;31m</code></b> constant.
   */
  public static final ANSIConstant RED      = new ANSIConstantImpl("\033[00;31m");

  /**
   * <b><code>\033[00;32m</code></b> constant.
   */
  public static final ANSIConstant GREEN    = new ANSIConstantImpl("\033[00;32m");

  /**
   * <b><code>\033[00;33m</code></b> constant.
   */
  public static final ANSIConstant YELLOW   = new ANSIConstantImpl("\033[00;33m");

  /**
   * <b><code>\033[00;34m</code></b> constant.
   */
  public static final ANSIConstant BLUE     = new ANSIConstantImpl("\033[00;34m");

  /**
   * <b><code>\033[00;35m</code></b> constant.
   */
  public static final ANSIConstant MAGENTA  = new ANSIConstantImpl("\033[00;35m");

  /**
   * <b><code>\033[00;36m</code></b> constant.
   */
  public static final ANSIConstant CYAN     = new ANSIConstantImpl("\033[00;36m");

  /**
   * <b><code>\033[00;37m</code></b> constant.
   */
  public static final ANSIConstant WHITE    = new ANSIConstantImpl("\033[00;37m");

  /**
   * <b><code>\033[00;40m</code></b> constant.
   */
  public static final ANSIConstant BOLD_BLACK    = new ANSIConstantImpl("\033[00;40m");

  /**
   * <b><code>\033[00;41m</code></b> constant.
   */
  public static final ANSIConstant BOLD_RED      = new ANSIConstantImpl("\033[00;41m");

  /**
   * <b><code>\033[00;42m</code></b> constant.
   */
  public static final ANSIConstant BOLD_GREEN    = new ANSIConstantImpl("\033[00;42m");

  /**
   * <b><code>\033[00;43m</code></b> constant.
   */
  public static final ANSIConstant BOLD_YELLOW   = new ANSIConstantImpl("\033[00;43m");

  /**
   * <b><code>\033[00;44m</code></b> constant.
   */
  public static final ANSIConstant BOLD_BLUE     = new ANSIConstantImpl("\033[00;44m");

  /**
   * <b><code>\033[00;45m</code></b> constant.
   */
  public static final ANSIConstant BOLD_MAGENTA  = new ANSIConstantImpl("\033[00;45m");

  /**
   * <b><code>\033[00;46m</code></b> constant.
   */
  public static final ANSIConstant BOLD_CYAN     = new ANSIConstantImpl("\033[00;46m");

  /**
   * <b><code>\033[00;47m</code></b> constant.
   */
  public static final ANSIConstant BOLD_WHITE    = new ANSIConstantImpl("\033[00;47m");

  /**
   * Interface represents ANSI Control Sequence.
   */
  public interface ANSIConstant {

    /**
     * @return String representation of ANSI Control Sequence.
     */
    public String getRepresentation();
  }

} // interface
