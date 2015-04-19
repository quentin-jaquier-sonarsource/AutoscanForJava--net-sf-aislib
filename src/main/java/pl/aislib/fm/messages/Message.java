package pl.aislib.fm.messages;

import java.util.HashMap;
import java.util.Map;

/**
 * Core implementation of message class.
 *
 * @author
 * <table>
 *   <tr><td>Andrzej Luszczyk, AIS.PL</td></tr>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 */
public class Message implements IMessage {

  /**
   * Type of the message.
   */
  protected int type;

  /**
   * Identification code for the message.
   */
  protected int code;

  /**
   * Key for the message.
   */
  protected String key;

  /**
   * Content of the message.
   */
  protected IMessageContent content;

  /**
   * Default language for the message.
   */
  protected String defaultLanguage;


  // Constructors

  /**
   * @param type type of the message.
   * @param code message code.
   * @param key message key.
   * @param content message content object.
   * @param defaultLanguage default language for the message.
   */
  public Message(int type, int code, String key, IMessageContent content, String defaultLanguage) {
    this.type = type;
    this.code = code;
    this.key = key;
    this.content = content;
    this.defaultLanguage = defaultLanguage;
  }


  // Public methods

  /**
   * Converts the message using an <code>IMessageConverter</code> implementation.
   *
   * @see IMessageConverter#convert(IMessage, String, Object, Object)
   */
  public IMessage convert(IMessageConverter messageConverter, String language, Object keys, Object values) {
    return messageConverter.convert(this, language, keys, values);
  }

  /**
   * @see pl.aislib.fm.messages.IMessage#getType()
   */
  public int getType() {
    return type;
  }

  /**
   * @see pl.aislib.fm.messages.IMessage#getCode()
   */
  public int getCode() {
    return code;
  }

  /**
   * @see pl.aislib.fm.messages.IMessage#getKey()
   */
  public String getKey() {
    return key;
  }

  /**
   * @see pl.aislib.fm.messages.IMessage#getContent()
   */
  public String getContent() {
    return getContent(defaultLanguage);
  }

  /**
   * @param language language in which the message should give its content.
   * @return content of the message, as string.
   */
  public String getContent(String language) {
    Object obj = language != null ? content.getContent(language) : content.getContent(defaultLanguage);
    return obj != null ? obj.toString() : null;
  }

  /**
   * @see pl.aislib.fm.messages.IMessage#getContentObject()
   */
  public IMessageContent getContentObject() {
    return content;
  }

  /**
   * @see pl.aislib.fm.messages.IMessage#getDefaultLanguage()
   */
  public String getDefaultLanguage() {
    return defaultLanguage;
  }

  /**
   * Puts message into map.
   *
   * @param map <code>Map</code> object to fill.
   * Throws NullPointerException if argument (map) is null.
   */
  public void putToMap(Map map) {
    if (map == null) {
      throw new NullPointerException("map cannot be null");
    }
    map.put(getKey(), getContent());
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    Message message = (Message) obj;
    return message.getCode() == code;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return code + key.hashCode() + defaultLanguage.hashCode();
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return key.toString();
  }


  // Public classes

  /**
   * Default message content class.
   *
   * Objects of this class can contain contents in different languages.
   *
   * @author Andrzej Luszczyk, AIS.PL
   * @author Tomasz Pik, AIS.PL
   * @author Wojciech Swiatek, AIS.PL
   */
  public static class Content implements IMessageContent {

    /**
     * Map of contents.
     *
     * Languages are keys and content objects are values.
     */
    protected Map items = new HashMap();


    // Constructors

    /**
     * @param items map of contents.
     */
    public Content(Map items) {
      this.items.putAll(items);
    }


    // Public methods

    /**
     * @see pl.aislib.fm.messages.IMessageContent#getContent(java.lang.String)
     */
    public Object getContent(String lang) {
      return items.get(lang);
    }

  } // Content class

} // Message class
