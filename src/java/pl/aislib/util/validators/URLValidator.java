package pl.aislib.util.validators;

import java.net.URL;

import pl.aislib.fm.forms.ValidateException;

/**
 * URL validation class.
 *
 * <p>
 * Implemented additional properties:
 * <ul>
 *   <li><code>allowHttp</code></li>
 *   <li><code>allowHttps</code></li>
 *   <li><code>allowFtp</code></li>
 *   <li><code>allowFile</code></li>
 * </ul>
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.5 $
 */
public class URLValidator extends StringValidator {

  /**
   * Constant describing http URL.
   */
  protected static final String KEY_URL_PROTOCOL_HTTP = "http";

  /**
   * Constant describing https URL.
   */
  protected static final String KEY_URL_PROTOCOL_HTTPS = "https";

  /**
   * Constant describing ftp URL.
   */
  protected static final String KEY_URL_PROTOCOL_FTP = "ftp";

  /**
   * Constant describing file URL.
   */
  protected static final String KEY_URL_PROTOCOL_FILE = "file";


  /**
   * URL type property map.
   */
  protected PropertyMap protocolTypes;


  // Constructors

  /**
   * Base constructor.
   */
  public URLValidator() {
    super();

    protocolTypes = new PropertyMap();
    protocolTypes.put(KEY_URL_PROTOCOL_HTTP, new BooleanProperty("allowHttp", 4, Property.DEFAULT, false));
    protocolTypes.put(KEY_URL_PROTOCOL_HTTPS, new BooleanProperty("allowHttps", 4, Property.DEFAULT, false));
    protocolTypes.put(KEY_URL_PROTOCOL_FTP, new BooleanProperty("allowFtp", 4, Property.DEFAULT, false));
    protocolTypes.put(KEY_URL_PROTOCOL_FILE, new BooleanProperty("allowFile", 4, Property.DEFAULT, false));
  }


  // Public validation methods

  /**
   * @see pl.aislib.util.validators.Validator#toObject
   */
  public Object toObject(String value) throws ValidateException {

    URL url = null;
    try {
      url = new URL(value);
    } catch (Exception e) {
      return null;
    }

    return url;
  }

  /**
   * @see pl.aislib.util.validators.Validator#validateObject
   */
  public void validateObject(Object value) throws ValidateException {
    super.validateObject(value);

    if (checkProperty(protocolTypes) && !checkProtocol((URL) value)) {
      throw new ValidateException("Protocol type is not allowed.");
    }
  }


  // Public property methods

  /**
   * @param value flag for allowing http protocol.
   */
  public void setAllowHttp(boolean value) {
    setAllowProtocolType(KEY_URL_PROTOCOL_HTTP, value);
  }

  /**
   * @param value flag for allowing https protocol.
   */
  public void setAllowHttps(boolean value) {
    setAllowProtocolType(KEY_URL_PROTOCOL_HTTPS, value);
  }

  /**
   * @param value flag for allowing ftp protocol.
   */
  public void setAllowFtp(boolean value) {
    setAllowProtocolType(KEY_URL_PROTOCOL_FTP, value);
  }

  /**
   * @param value flag for allowing file protocol.
   */
  public void setAllowFile(boolean value) {
    setAllowProtocolType(KEY_URL_PROTOCOL_FILE, value);
  }


  // Protected check methods

  /**
   * @param value an URL object.
   * @return true if URL uses valid protocol.
   */
  protected boolean checkProtocol(URL value) {
    String protocol = value.getProtocol();

    BooleanProperty protocolType = (BooleanProperty) protocolTypes.get(protocol);
    if (protocolType == null) {
      return false;
    }

    return protocolType.isTrue();
  }


  // Protected methods

  /**
   * @param protocolType type of protocol.
   * @param allow flag for allowing entering URL with the protocol of the type.
   */
  protected void setAllowProtocolType(String protocolType, boolean allow) {
    BooleanProperty property = (BooleanProperty) protocolTypes.getProperty(protocolType);
    property.set(allow);
  }

} // URLValidator class
