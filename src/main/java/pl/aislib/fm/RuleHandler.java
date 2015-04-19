package pl.aislib.fm;

import java.util.Map;

import org.xml.sax.SAXException;

import pl.aislib.fm.forms.Rule;
import pl.aislib.fm.forms.config.Handler;
import pl.aislib.fm.forms.config.IRule;

/**
 * XML handler for handling framework's rules.
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class RuleHandler extends pl.aislib.fm.forms.config.RuleHandler {

  // Constructors

  /**
   * @see pl.aislib.fm.forms.config.PartialHandler#PartialHandler(Handler)
   */
  public RuleHandler(Handler parentHandler) {
    super(parentHandler);
  }


  // Protected methods

  /**
   * @see pl.aislib.fm.forms.config.RuleHandler#addRule(IRule, Map)
   */
  protected void addRule(IRule rule, Map atts) throws SAXException {
    Rule formRule = (Rule) rule;

    formRule.setName((String) atts.get("name"));

    int msgCode = 0;
    try {
      msgCode = Integer.parseInt((String) atts.get("msg-code"));
    } catch (NumberFormatException nfe) {
      throw new SAXException("Cannot parse " + atts.get("msg-code") + " as number", nfe);
    }
    formRule.setMsgCode(msgCode);

    populate(formRule, currentProperties);
    formRule.setMapping(currentMapping);
    formRule.createFieldsValidatedConditionally();
    formRule.setDynamic(((FormsHandler) parentHandler).currentForm);

    ((FormsHandler) parentHandler).currentForm.addRule(formRule);
  }

} // RuleHandler class
