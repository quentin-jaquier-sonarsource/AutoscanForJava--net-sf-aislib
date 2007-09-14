package pl.aislib.fm;

import pl.aislib.fm.messages.IMessageContent;


/**
 * Represents a message configured in XML file.
 *
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Wojciech Swiatek, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.4 $
 * @since AISLIB 0.1
 */
public class Message extends pl.aislib.fm.messages.Message {

  // Constructors

  /**
   * @see pl.aislib.fm.messages.Message#Message(int, int, String, IMessageContent, String)
   */
  public Message(int type, int code, String key, IMessageContent content, String defaultLanguage) {
    super(type, code, key, content, defaultLanguage);
  }

  /**
   * Creates an error message.
   *
   * @see pl.aislib.fm.messages.Message#Message(int, int, String, IMessageContent, String)
   */
  public Message(int code, String key, IMessageContent content, String defaultLanguage) {
    super(ERROR, code, key, content, defaultLanguage);
  }

  /**
   * Creates a message with one specific-language content item.
   *
   * @see pl.aislib.fm.messages.Message#Message(int, int, String, IMessageContent, String)
   */
  public Message(int type, int code, String key, String content, String defaultLanguage) {
    super(type, code, key, new Content(content), defaultLanguage);
  }

  /**
   * Creates an error message with one specific-language content item.
   *
   * @see pl.aislib.fm.messages.Message#Message(int, int, String, IMessageContent, String)
   */
  public Message(int code, String key, String content, String defaultLanguage) {
    super(ERROR, code, key, new Content(content), defaultLanguage);
  }

  /**
   * Creates a message with no specific-language content item.
   *
   * @see pl.aislib.fm.messages.Message#Message(int, int, String, IMessageContent, String)
   * @deprecated Default language should be given.
   */
  public Message(int code, String key, String content) {
    super(ERROR, code, key, new Content(content), null);
  }


  // Protected classes

  /**
   * Default framework message content class.
   *
   * @author Wojciech Swiatek, AIS.PL
   */
  protected static class Content implements IMessageContent {

    /**
     * Content of the message.
     */
    private String content;


    // Constructors

    /**
     * @param content content of the message.
     */
    public Content(String content) {
      this.content = content;
    }


    // Public methods

    /**
     * @see pl.aislib.fm.messages.IMessageContent#getContent(java.lang.String)
     */
    public Object getContent(String lang) {
      return content;
    }

  } // Content class

} // Message class
