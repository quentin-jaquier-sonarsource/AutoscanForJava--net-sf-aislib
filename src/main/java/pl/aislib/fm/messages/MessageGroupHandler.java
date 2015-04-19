package pl.aislib.fm.messages;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import pl.aislib.fm.MessagesHandler;
import pl.aislib.fm.forms.config.Handler;
import pl.aislib.fm.forms.config.PartialHandler;

/**
 * @author Wojciech Swiatek, AIS.PL
 */
public class MessageGroupHandler extends PartialHandler {

  /**
   * Group of messages being used.
   */
  protected List currentMessageGroup;

  /**
   * Group code of messages being used.
   */
  protected Integer currentMessageGroupCode;

  /**
   * Message reference being used.
   */
  protected IMessage currentMessageRef;


  // Constructors

  /**
   * @param parentHandler <code>Handler</code> object.
   */
  public MessageGroupHandler(Handler parentHandler) {
    super(parentHandler);
  }


  // Public methods

  /**
   * @see org.xml.sax.ContentHandler#endElement(String, String, String)
   */
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if ("message-group".equals(localName)) {
      addMessageGroup(currentMessageGroupCode, currentMessageGroup);
      currentMessageGroup = null;
      return;
    }
  }

  /**
   * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
   */
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    if ("message-group".equals(localName)) {
      currentObject = currentMessageGroup = processMessageGroup(atts);
      return;
    }
    if ("message-ref".equals(localName)) {
      currentObject = currentMessageRef = processMessageRef(atts);
      return;
    }
  }


  // Protected methods

  /**
   * @param messageGroupCode identification code for a message.
   * @param messageGroup group of messages.
   */
  protected void addMessageGroup(Integer messageGroupCode, List messageGroup) {
    ((MessagesHandler) parentHandler).addMessageGroup(messageGroupCode.intValue(), messageGroup);
  }

  /**
   * @param atts <code>Attributes</code> object.
   * @return group of messages.
   * @throws SAXException if an error occurs in parsing.
   */
  protected List processMessageGroup(Attributes atts) throws SAXException {
    try {
      currentMessageGroupCode = Integer.valueOf(atts.getValue("code"));
    } catch (NumberFormatException nfe) {
      throw new SAXException("Message group code must be integer number: " + atts.getValue("code"));
    }
    return new ArrayList();
  }

  /**
   * @param atts <code>Attributes</code> object.
   * @return message reference.
   * @throws SAXException if an error occurs in parsing.
   */
  protected IMessage processMessageRef(Attributes atts) throws SAXException {
    try {
      int messageRefCode = Integer.parseInt(atts.getValue("ref-code"));
      IMessage messageRef = ((MessagesHandler) parentHandler).getMessage(messageRefCode);
      if (messageRef == null) {
        throw new SAXException("No message with code: " + messageRefCode);
      }
      if (!currentMessageGroup.contains(messageRef)) {
        currentMessageGroup.add(messageRef);
      }
      return messageRef;
    } catch (NumberFormatException nfe) {
      throw new SAXException(
        "Message reference (message-ref) code must be integer number: " + atts.getValue("ref-code")
      );
    }
  }

} // MessageGroupHandler class
