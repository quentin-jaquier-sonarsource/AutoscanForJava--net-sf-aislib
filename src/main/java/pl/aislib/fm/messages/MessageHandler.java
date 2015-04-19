package pl.aislib.fm.messages;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import pl.aislib.fm.MessagesHandler;
import pl.aislib.fm.forms.config.Handler;
import pl.aislib.fm.forms.config.PartialHandler;

/**
 * @author Wojciech Swiatek, AIS.PL
 */
public class MessageHandler extends PartialHandler {

  /**
   * Message type being used.
   */
  protected int currentMessageType;

  /**
   * Message code being used.
   */
  protected Integer currentMessageCode;

  /**
   * Message key being used.
   */
  protected StringBuffer currentKey;

  /**
   * Map of contents being used.
   */
  protected Map currentContentItems;

  /**
   * Content's language being used.
   */
  protected String currentContentLanguage;

  /**
   * Default language.
   */
  protected String defaultLanguage;


  // Constructors

  /**
   * @param parentHandler <code>Handler</code> object.
   */
  public MessageHandler(Handler parentHandler) {
    super(parentHandler);
  }


  // Public methods

  /**
   * @see org.xml.sax.ContentHandler#endElement(String, String, String)
   */
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if ("message".equals(localName)) {
      addMessage(currentMessageType, currentMessageCode, currentKey, currentContentItems, defaultLanguage);
      currentContentItems = null;
      return;
    }
    if ("key".equals(localName)) {
      currentBuffer = null;
      return;
    }
    if ("content".equals(localName)) {
      addContent(currentContentLanguage, currentBuffer);
      currentBuffer = null;
      return;
    }
  }

  /**
   * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
   */
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if ("message".equals(localName)) {
      currentObject = currentMessageCode = processMessageCode(atts);
      return;
    }
    if ("key".equals(localName)) {
      currentObject = currentKey = processKey(atts);
      return;
    }
    if ("content".equals(localName)) {
      currentObject = currentBuffer = processContentItem(atts);
      return;
    }
  }


  // Protected methods

  /**
   * @param contentLanguage language for a message content item.
   * @param contentItem the message content item.
   */
  protected void addContent(String contentLanguage, Object contentItem) {
    currentContentItems.put(contentLanguage, contentItem.toString());
  }

  /**
   * @param messageType type of a message.
   * @param messageCode identification code for the message.
   * @param key key for the message.
   * @param contentItems map of contents.
   * @param defaultLanguage default language for the message.
   */
  protected void addMessage(
    int messageType, Integer messageCode, Object key, Map contentItems, String defaultLanguage
  ) {
    ((MessagesHandler) parentHandler).
      addMessage(
        messageType,
        messageCode.intValue(),
        key.toString(),
        new Message.Content(contentItems),
        defaultLanguage
      );
  }

  /**
   * @param atts <code>Attributes</code> object.
   * @return identification code for a message.
   * @throws SAXException if an error occurs while parsing.
   */
  protected Integer processMessageCode(Attributes atts) throws SAXException {
    try {
      currentMessageCode = Integer.valueOf(atts.getValue("code"));
    } catch (NumberFormatException nfe) {
      throw new SAXException("Message code must be integer number: " + atts.getValue("code"));
    }

    String messageTypeStr = atts.getValue("type");
    currentMessageType = messageTypeStr != null ? Message.typeMap.get(messageTypeStr): Message.ERROR;

    if (Message.UNKNOWN == currentMessageType) {
      throw new SAXException("Type of message is unknown: " + messageTypeStr);
    }

    currentContentItems = new HashMap();

    String language = atts.getValue("lang");
    if (language != null) {
      defaultLanguage = language;
    } else {
      defaultLanguage = ((MessagesHandler) parentHandler).getDefaultLanguage();
    }

    return currentMessageCode;
  }

  /**
   * @param atts <code>Attributes</code> object.
   * @return key for a message, as <code>StringBuffer</code> object.
   * @throws SAXException if an error occurs while parsing.
   */
  protected StringBuffer processKey(Attributes atts) throws SAXException {
    return (currentBuffer = new StringBuffer());
  }

  /**
   * @param atts <code>Attributes</code> object.
   * @return single message content, as <code>StringBuffer</code> object.
   * @throws SAXException if an error occurs while parsing.
   */
  protected StringBuffer processContentItem(Attributes atts) throws SAXException {
    currentContentLanguage = atts.getValue("lang");
    if (currentContentLanguage == null) {
      currentContentLanguage = defaultLanguage;
    }
    return new StringBuffer();
  }

} // MessageHandler class
