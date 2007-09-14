package pl.aislib.fm.messages;

/**
 * Core interface for converting messages.
 *
 * The idea is to allow messages to be modified before being used later.
 * This technique has been implemented for messages generated from forms' validation.
 *
 * @author Wojciech Swiatek, AIS.PL
 * @version $Revision: 1.3 $
 */
public interface IMessageConverter {

  /**
   * @param message message to be converted.
   * @param language language in which the message will be converted.
   * @param keys <code>Object</code> used to convert message keys.
   * @param contents <code>Object</code> used to convert message contents.
   * @return converted message.
   */
  IMessage convert(IMessage message, String language, Object keys, Object contents);

} // IMessageConverter interface
