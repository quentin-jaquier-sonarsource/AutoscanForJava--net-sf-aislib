package pl.aislib.io;

/**
 * @author Tomasz Pik
 * @version $Revision: 1.1.1.1 $
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
