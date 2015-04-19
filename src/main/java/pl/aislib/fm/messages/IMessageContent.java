package pl.aislib.fm.messages;

/**
 * Message content core interface.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public interface IMessageContent {

  /**
   * @param language content's language.
   * @return <code>Object</code>.
   */
  Object getContent(String language);

} // IMessageContent interface
