package pl.aislib.util.messages;

import java.text.MessageFormat;

import java.util.Map;

import pl.aislib.fm.Message;
import pl.aislib.fm.messages.IMessage;
import pl.aislib.fm.messages.IMessageConverter;

/**
 * <code>MessageFormat</code> converter class.
 *
 * It uses the syntax of <code>MessageFormat</code> objects.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.4 $
 */
public class MessageFormatConverter implements IMessageConverter {

  /**
   * Format of message to be used.
   */
  protected MessageFormat messageFormat = new MessageFormat("");


  // Public methods

  /**
   * @see pl.aislib.fm.messages.IMessageConverter#convert(IMessage, String, Object, Object)
   */
  public IMessage convert(IMessage message, String language, Object keys, Object contents) {
    if (keys == null && contents == null) {
      return message;
    }
    boolean keysArray = keys instanceof Object[];
    boolean contentsArray = contents instanceof Object[];
    boolean keysMap = keys instanceof Map;
    boolean contentsMap = contents instanceof Map;
    if (!(keysArray || contentsArray || keysMap || contentsMap)) {
      return message;
    }

    Object[] aKeys =
      keysArray ? (Object[]) keys : (keysMap ? ((Map) keys).values().toArray() : null);
    Object[] aContents =
      contentsArray ? (Object[]) contents : (contentsMap ? ((Map) contents).values().toArray() : null);

    messageFormat.applyPattern(message.getKey());
    String newKey = messageFormat.format(aKeys);
    messageFormat.applyPattern(message.getContent(language));
    String newContent = messageFormat.format(aContents);

    return new Message(message.getCode(), newKey, newContent, language);
  }

} // MessageFormatConverter class
