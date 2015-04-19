package pl.aislib.util.validators;

/**
 * US states and Canada provinces validation class.
 *
 * <p>
 * Implemented additional properties:
 * <ul>
 *   <li><code>allowUsa</code></li>
 *   <li><code>allowUsArmy</code></li>
 *   <li><code>allowUsTerritories</code></li>
 *   <li><code>allowCanada</code></li>
 *   <li><code>allowOther</code></li>
 * </ul>
 *
 * Here are the abbreviations:
 * <table>
 *   <tr><td>XX</td><td>Outside US / Canada</td></tr>
 *   <tr><td>AL</td><td>Alabama</td></tr>
 *   <tr><td>AK</td><td>Alaska</td></tr>
 *   <tr><td>AB</td><td>Alberta</td></tr>
 *   <tr><td>AS</td><td>American Samoa</td></tr>
 *   <tr><td>AZ</td><td>Arizona</td></tr>
 *   <tr><td>AR</td><td>Arkansas</td></tr>
 *   <tr><td>AA</td><td>Armed Forces Americas</td></tr>
 *   <tr><td>AE</td><td>Armed Forces Europe</td></tr>
 *   <tr><td>AP</td><td>Armed Forces Pacific</td></tr>
 *   <tr><td>BC</td><td>British Columbia</td></tr>
 *   <tr><td>CA</td><td>California</td></tr>
 *   <tr><td>CO</td><td>Colorado</td></tr>
 *   <tr><td>CT</td><td>Connecticut</td></tr>
 *   <tr><td>DE</td><td>Delaware</td></tr>
 *   <tr><td>DC</td><td>District Of Columbia</td></tr>
 *   <tr><td>FL</td><td>Florida</td></tr>
 *   <tr><td>GA</td><td>Georgia</td></tr>
 *   <tr><td>GU</td><td>Guam</td></tr>
 *   <tr><td>HI</td><td>Hawaii</td></tr>
 *   <tr><td>ID</td><td>Idaho</td></tr>
 *   <tr><td>IL</td><td>Illinois</td></tr>
 *   <tr><td>IN</td><td>Indiana</td></tr>
 *   <tr><td>IA</td><td>Iowa</td></tr>
 *   <tr><td>KS</td><td>Kansas</td></tr>
 *   <tr><td>KY</td><td>Kentucky</td></tr>
 *   <tr><td>LA</td><td>Louisiana</td></tr>
 *   <tr><td>ME</td><td>Maine</td></tr>
 *   <tr><td>MB</td><td>Manitoba</td></tr>
 *   <tr><td>MD</td><td>Maryland</td></tr>
 *   <tr><td>MA</td><td>Massachusetts</td></tr>
 *   <tr><td>MI</td><td>Michigan</td></tr>
 *   <tr><td>MN</td><td>Minnesota</td></tr>
 *   <tr><td>MS</td><td>Mississippi</td></tr>
 *   <tr><td>MO</td><td>Missouri</td></tr>
 *   <tr><td>MT</td><td>Montana</td></tr>
 *   <tr><td>NE</td><td>Nebraska</td></tr>
 *   <tr><td>NV</td><td>Nevada</td></tr>
 *   <tr><td>NB</td><td>New Brunswick</td></tr>
 *   <tr><td>NH</td><td>New Hampshire</td></tr>
 *   <tr><td>NJ</td><td>New Jersey</td></tr>
 *   <tr><td>NM</td><td>New Mexico</td></tr>
 *   <tr><td>NY</td><td>New York</td></tr>
 *   <tr><td>NF</td><td>Newfoundland</td></tr>
 *   <tr><td>NC</td><td>North Carolina</td></tr>
 *   <tr><td>ND</td><td>North Dakota</td></tr>
 *   <tr><td>MP</td><td>Northern Mariana Is</td></tr>
 *   <tr><td>NT</td><td>Northwest Territories</td></tr>
 *   <tr><td>NS</td><td>Nova Scotia</td></tr>
 *   <tr><td>OH</td><td>Ohio</td></tr>
 *   <tr><td>OK</td><td>Oklahoma</td></tr>
 *   <tr><td>ON</td><td>Ontario</td></tr>
 *   <tr><td>OR</td><td>Oregon</td></tr>
 *   <tr><td>PW</td><td>Palau</td></tr>
 *   <tr><td>PA</td><td>Pennsylvania</td></tr>
 *   <tr><td>PE</td><td>Prince Edward Island</td></tr>
 *   <tr><td>PQ</td><td>Province du Quebec</td></tr>
 *   <tr><td>PR</td><td>Puerto Rico</td></tr>
 *   <tr><td>RI</td><td>Rhode Island</td></tr>
 *   <tr><td>SK</td><td>Saskatchewan</td></tr>
 *   <tr><td>SC</td><td>South Carolina</td></tr>
 *   <tr><td>SD</td><td>South Dakota</td></tr>
 *   <tr><td>TN</td><td>Tennessee</td></tr>
 *   <tr><td>TX</td><td>Texas</td></tr>
 *   <tr><td>UT</td><td>Utah</td></tr>
 *   <tr><td>VT</td><td>Vermont</td></tr>
 *   <tr><td>VI</td><td>Virgin Islands</td></tr>
 *   <tr><td>VA</td><td>Virginia</td></tr>
 *   <tr><td>WA</td><td>Washington</td></tr>
 *   <tr><td>WV</td><td>West Virginia</td></tr>
 *   <tr><td>WI</td><td>Wisconsin</td></tr>
 *   <tr><td>WY</td><td>Wyoming</td></tr>
 *   <tr><td>YT</td><td>Yukon Territory</td></tr>
 * </table>
 *
 * @author Wojciech Swiatek, AIS.PL
 */
public class StatesValidator extends StringValidator {

  /**
   * Constant describing type of US states.
   */
  protected static final String KEY_STATES_USA = "Usa";

  /**
   * Constant describing type of US Army.
   */
  protected static final String KEY_STATES_US_ARMY = "US Army";

  /**
   * Constant describing type of US territories.
   */
  protected static final String KEY_STATES_US_TERRITORIES = "US Territories";

  /**
   * Constant describing type of Canada provinces.
   */
  protected static final String KEY_STATES_CANADA = "Canada";

  /**
   * Constant describing type of outside US/Canada regions.
   */
  protected static final String KEY_STATES_OTHER = "Other";


  /**
   * Constant string describing US states.
   */
  protected static final String STR_STATES_USA =
    "AK, AL, AR, AZ, CA, CO, CT, DC, DE, FL, GA, HI, IA, ID, IL, IN, KS, KY, " +
    "LA, MA, MD, ME, MI, MN, MO, MS, MT, NC, ND, NE, NH, NJ, NM, NV, NY, " +
    "OH, OK, OR, PA, RI, SC, SD, TN, TX, UT, VA, VT, WA, WI, WV, WY, ";

  /**
   * Constant string describing US Army.
   */
  protected static final String STR_STATES_US_ARMY =
    "AA, AE, AP, ";

  /**
   * Constant string describing US territories.
   */
  protected static final String STR_STATES_US_TERRITORIES =
    "AS, GU, MP, PR, PW, VI, ";

  /**
   * Constant string describing Canada provinces.
   */
  protected static final String STR_STATES_CANADA =
    "AB, BC, MB, NB, NF, NS, NT, ON, PE, PQ, SK, YT, ";

  /**
   * Constant string describing outside US/Canada regions.
   */
  protected static final String STR_STATES_OTHER =
    "XX, ";


  /**
   * Region property map.
   */
  protected PropertyMap stateTypes;


  // Constructors

  /**
   * Base constructor.
   */
  public StatesValidator() {
    super();

    stateTypes = new PropertyMap();
    stateTypes.put(KEY_STATES_USA, new BooleanProperty("allowUsa", 4, Property.DEFAULT, true));
    stateTypes.put(KEY_STATES_US_ARMY, new BooleanProperty("allowUsArmy", 4, Property.DEFAULT, true));
    stateTypes.put(KEY_STATES_US_TERRITORIES, new BooleanProperty("allowUsTerritories", 4, Property.DEFAULT, true));
    stateTypes.put(KEY_STATES_CANADA, new BooleanProperty("allowCanada", 4, Property.DEFAULT, true));
    stateTypes.put(KEY_STATES_OTHER, new BooleanProperty("allowOther", 4, Property.DEFAULT, true));

    configStateTypes();

    ignoreCase.setValue(true);
  }


  // Public property methods

  /**
   * @param value flag for allowing US states.
   */
  public void setAllowUsa(boolean value) {
    setAllowStateType(KEY_STATES_USA, value);
    setAllowStateType(KEY_STATES_US_ARMY, value);
    setAllowStateType(KEY_STATES_US_TERRITORIES, value);
  }

  /**
   * @param value flag for allowing US Army places.
   */
  public void setAllowUsArmy(boolean value) {
    setAllowStateType(KEY_STATES_US_ARMY, value);
  }

  /**
   * @param value flag for allowing other US territories.
   */
  public void setAllowUsTerritories(boolean value) {
    setAllowStateType(KEY_STATES_US_TERRITORIES, value);
  }
  /**
   * @param value flag for allowing Canada provinces.
   */
  public void setAllowCanada(boolean value) {
    setAllowStateType(KEY_STATES_CANADA, value);
  }

  /**
   * @param value flag for allowing outside US/Canada regions.
   */
  public void setAllowOther(boolean value) {
    setAllowStateType(KEY_STATES_OTHER, value);
  }


  // Protected methods

  /**
   * Configure region types.
   */
  protected void configStateTypes() {
    boolean bUsa = ((BooleanProperty) stateTypes.getProperty(KEY_STATES_USA)).isTrue();
    boolean bUsArmy = ((BooleanProperty) stateTypes.getProperty(KEY_STATES_US_ARMY)).isTrue();
    boolean bUsTerritories = ((BooleanProperty) stateTypes.getProperty(KEY_STATES_US_TERRITORIES)).isTrue();
    boolean bCanada = ((BooleanProperty) stateTypes.getProperty(KEY_STATES_CANADA)).isTrue();
    boolean bOther = ((BooleanProperty) stateTypes.getProperty(KEY_STATES_OTHER)).isTrue();

    StringBuffer buf = new StringBuffer();
    if (bUsa) {
      buf.append(STR_STATES_USA);
    }
    if (bUsArmy) {
      buf.append(STR_STATES_US_ARMY);
    }
    if (bUsTerritories) {
      buf.append(STR_STATES_US_TERRITORIES);
    }
    if (bCanada) {
      buf.append(STR_STATES_CANADA);
    }
    if (bOther) {
      buf.append(STR_STATES_OTHER);
    }

    setAllowedValuesOnly(buf.toString(), true);
  }

  /**
   * @param stateType type of region.
   * @param allow true if the type is allowed to be validated successfully.
   */
  protected void setAllowStateType(String stateType, boolean allow) {
    BooleanProperty property = (BooleanProperty) stateTypes.getProperty(stateType);
    property.set(allow);
    configStateTypes();
  }

} // StatesValidator class
