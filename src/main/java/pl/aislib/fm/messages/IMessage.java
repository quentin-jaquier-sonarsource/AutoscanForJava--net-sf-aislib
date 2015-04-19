package pl.aislib.fm.messages;

import java.util.HashMap;

/**
 * Message interface.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public interface IMessage {

  /**
   * Constant describing unknown type of message.
   */
  int UNKNOWN = 0;

  /**
   * Constant describing error type of message.
   */
  int ERROR = 1;

  /**
   * Constant describing fatal type of message.
   */
  int FATAL = 2;

  /**
   * Constant describing warning type of message.
   */
  int WARNING = 3;

  /**
   * Constant describing information type of message.
   */
  int INFO = 4;

  /**
   * Constant describing custom type of message.
   */
  int CUSTOM = 5;

  /**
   * String constant describing error type of message.
   */
  String STR_ERROR = "error";

  /**
   * String constant describing fatal type of message.
   */
  String STR_FATAL = "fatal";

  /**
   * String constant describing warning type of message.
   */
  String STR_WARNING = "warning";

  /**
   * String constant describing information type of message.
   */
  String STR_INFO = "info";

  /**
   * String constant describing custom type of message.
   */
  String STR_CUSTOM = "custom";


  // Interface classes

  /**
   * Class of map between type string constants and type integer constants.
   *
   * @author Wojciech Swiatek, AIS.PL
   */
  final class TypeMap extends HashMap {

    // Constructors

    /**
     * Creates the map.
     */
    TypeMap() {
      super();

      put(STR_ERROR, new Integer(ERROR));
      put(STR_FATAL, new Integer(FATAL));
      put(STR_WARNING, new Integer(WARNING));
      put(STR_INFO, new Integer(INFO));
      put(STR_CUSTOM, new Integer(CUSTOM));
    }


    // Public methods

    /**
     * @param strError string type.
     * @return integer type.
     */
    int get(String strError) {
      Object obj = super.get(strError);
      return obj != null ? ((Integer) obj).intValue() : UNKNOWN;
    }

  } // TypeMap class


  // Interface fields

  /**
   * Map between type string constants and type integer constants.
   */
  TypeMap typeMap = new TypeMap();


  // Interface methods

  /**
   * @return identification code of the message.
   */
  int getCode();

  /**
   * @return key for the message.
   */
  String getKey();

  /**
   * Gets string content of the message.
   * It normally should be used to get string content in default language.
   *
   * @return string content of the message.
   */
  String getContent();

  /**
   * @param language language for the message.
   * @return string content of the message.
   */
  String getContent(String language);

  /**
   * @return content of the message.
   */
  IMessageContent getContentObject();

  /**
   * @return default language for the message.
   */
  String getDefaultLanguage();

  /**
   * @return type of the message.
   */
  int getType();

} // IMessage interface
