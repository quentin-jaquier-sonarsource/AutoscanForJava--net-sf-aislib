package pl.aislib.fm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.apache.commons.logging.Log;

import pl.aislib.fm.forms.config.Handler;
import pl.aislib.fm.messages.IMessage;
import pl.aislib.fm.messages.IMessageContent;
import pl.aislib.fm.messages.MessageGroupHandler;
import pl.aislib.fm.messages.MessageHandler;

/**
 * XML handler for handling framework's messages.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class MessagesHandler extends Handler {

  /**
   * Map of messages.
   */
  protected Map messages;

  /**
   * Map of groups of messages.
   */
  protected Map messageGroups;

  /**
   * Default language for messages.
   */
  protected String defaultLanguage = "en-us";


  // Constructors

  /**
   * @param log logging object.
   */
  public MessagesHandler(Log log) {
    super(log);

    messages = new HashMap();
    messageGroups = new HashMap();
  }


  // Public methods

  /**
   * @see pl.aislib.fm.forms.config.IXMLHandler#processEndElement(java.lang.String, java.lang.String, java.lang.String)
   */
  public void processEndElement(String namespaceURI, String localName, String qName) throws SAXException {
  }

  /**
   * @see pl.aislib.fm.forms.config.IXMLHandler#processStartElement(String, String, String, Attributes)
   */
  public Object processStartElement(String namespaceURI, String localName, String qName, Attributes atts)
    throws SAXException {
    if ("messages".equals(localName)) {
      String language = atts.getValue("lang");
      if (language != null) {
        defaultLanguage = language;
      }
    }
    return localName;
  }

  /**
   * @param messageType type of a message.
   * @param messageCode identification code for the message.
   * @param key key for the message.
   * @param content message content object.
   * @param defaultLanguage default language for the message.
   */
  public void addMessage(
    int messageType, int messageCode, String key, IMessageContent content, String defaultLanguage
  ) {
    messages.put(new Integer(messageCode), new Message(messageType, messageCode, key, content, defaultLanguage));
  }

  /**
   * @param messageGroupCode identification code for a group of messages.
   * @param messageGroup group of messages.
   */
  public void addMessageGroup(int messageGroupCode, List messageGroup) {
    messageGroups.put(new Integer(messageGroupCode), messageGroup);
  }

  /**
   * @return a copy of map of messages.
   */
  public Map cloneMessages() {
    return new HashMap(messages);
  }

  /**
   * @return a copy of map of group of messages.
   */
  public Map cloneMessageGroups() {
    return new HashMap(messageGroups);
  }

  /**
   * @return default language for messages.
   */
  public String getDefaultLanguage() {
    return defaultLanguage;
  }

  /**
   * @param messageCode identification code for a message.
   * @return the message with all of its contents.
   */
  public IMessage getFullMessage(int messageCode) {
    Object obj = messages.get(new Integer(messageCode));
    return obj != null ? (IMessage) obj : null;
  }

  /**
   * @param messageCode identification code for a message.
   * @return the message with content in default language.
   */
  public Message getMessage(int messageCode) {
    return getMessage(messageCode, defaultLanguage);
  }

  /**
   * @param messageCode identification code for a message.
   * @param language language in which the message should be given.
   * @return the message with content in given language.
   */
  public Message getMessage(int messageCode, String language) {
    pl.aislib.fm.messages.Message message = (pl.aislib.fm.messages.Message) getFullMessage(messageCode);
    if (message == null) {
      return null;
    }
    return new Message(message.getCode(), message.getKey(), message.getContent(language), language);
  }

  /**
   * @param messageGroupCode group code for messages.
   * @return group of messages.
   */
  public List getMessageGroup(int messageGroupCode) {
    Object obj = messageGroups.get(new Integer(messageGroupCode));
    return obj != null ? (List) obj : null;
  }


  // Protected methods

  /**
   * @see pl.aislib.fm.forms.config.Handler#createPartialHandlers()
   */
  protected void createPartialHandlers() {
    addPartialHandler("message", new MessageHandler(this));
    addPartialHandler("message-group", new MessageGroupHandler(this));
  }

} // MessagesHandler class
