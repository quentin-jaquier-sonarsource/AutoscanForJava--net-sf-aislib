package pl.aislib.util.messages;

import pl.aislib.fm.messages.IMessage;
import pl.aislib.fm.messages.IMessageConverter;

/**
 * Standard message converter class.
 * 
 * It does nothing but puts the message through.
 * 
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.3 $
 */
public class StandardMessageConverter implements IMessageConverter {

  // Public methods
  
  /**
   * @see pl.aislib.fm.messages.IMessageConverter#convert(IMessage, String, Object, Object)
   */
  public IMessage convert(IMessage message, String language, Object keys, Object contents) {
    return message;
  }

} // StandardMessageConverter class
