package pl.aislib.io;

/**
 * @author Tomasz Pik
 * @since AISLIB 0.2
 */
class ANSIConstantImpl implements ANSIConstants.ANSIConstant {
  private String representation;

  ANSIConstantImpl(String _representation) {
    representation = _representation;
  }

  /**
   *
   */
  public String getRepresentation() {
    return representation;
  }

} // class
