package pl.aislib.util.template;

import java.awt.Font;

/**
 * Represents single template field.
 *
 * Has the JavaBeans functionality (get/set methods)
 * because it is created automatically from XML
 * files by Digester package.
 *
 * @author Daniel Rychcik, AIS.PL
 * @author Tomasz Pik, AIS.PL
 */
public class Field {

  /**
   * For horizontal and vertical alignment.
   */
  public static final int DEFAULT_ALIGN = 0;

  /**
   * Define horizontal alignment.
   */
  public static final int ALIGN_LEFT    = 1;

  /**
   * Define horizontal alignment.
   */
  public static final int ALIGN_CENTER  = 2;

  /**
   * Define horizontal alignment.
   */
  public static final int ALIGN_RIGHT   = 3;

  /**
   * Define vertical alignment.
   */
  public static final int ALIGN_TOP     = 4;

  /**
   * Define vertical alignment.
   */
  public static final int ALIGN_MIDDLE  = 5;

  /**
   * Define vertical alignment.
   */
  public static final int ALIGN_BOTTOM  = 6;

  /**
   *
   */
  public static final int DEFAULT_PAGE = -1;

  private TemplateFont templateFont;
  private String fontName;

  private int positionX;
  private int positionY;

  private int pageNumber;

  private int alignX;
  private int alignY;

  private String name;

  /**
   *
   * @return name of field.
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @param name name of field.
   */
  public void setName(String name) {
    this.name = name;
  }

  public Field(int _positionX, int _positionY, int _alignX, int _alignY, int _pageNumber, TemplateFont _font) {
    positionX  = _positionX;
    positionY  = _positionY;

    alignX     = _alignX;
    alignY     = _alignY;

    pageNumber = _pageNumber;

    templateFont = _font;
  }

  public Field(int _positionX, int _positionY, int _pageNumber, TemplateFont _font) {
    this(_positionX, _positionY, DEFAULT_ALIGN, DEFAULT_ALIGN, _pageNumber, _font);
  }

  public Field(int _positionX, int _positionY, int _alignX, int _alignY, TemplateFont _font) {
    this(_positionX, _positionY, _alignX, _alignY, DEFAULT_PAGE, _font);
  }

  public Field(int _positionX, int _positionY, TemplateFont _font) {
    this(_positionX, _positionY, DEFAULT_ALIGN, DEFAULT_ALIGN, DEFAULT_PAGE, _font);
  }

  /**
   * Empty constructor for Digester automatic object creation
   */
  public Field() {
  }

  public String toString() {
    return "FIELD: name=" + name + ", fontName=" + fontName +
           ", positionX=" + positionX + ", positionY=" + positionY +
           ", alignX=" + alignX + ", alignY=" + alignY +
           ", pageNumber=" + pageNumber + ", templateFont=" + templateFont;
  }

  public TemplateFont getTemplateFont() {
    return templateFont;
  }

  public void setTemplateFont(TemplateFont templateFont) {
    this.templateFont = templateFont;
  }

  /**
   *
   * @return name of font.
   */
  public String getFontName() {
    return fontName;
  }

  /**
   * Create <code>java.awt.Font</code> based on <code>TemplateFont</code>.
   *
   * @return created {@link Font}.
   */
  public Font getFont() {
    return new Font(templateFont.getFontName(),
                    templateFont.getStyle() | templateFont.getWeight(),
                    templateFont.getSize());
  }

  /**
   *
   * @return X coordinate of field.
   */
  public int getPositionX() {
    return positionX;
  }

  /**
   *
   * @param positionX X coordinate of field.
   */
  public void setPositionX(int positionX) {
    this.positionX = positionX;
  }

  /**
   *
   * @return Y coordinate of field.
   */
  public int getPositionY() {
    return positionY;
  }

  /**
   *
   * @param positionY Y cooridinate of field.
   */
  public void setPositionY(int positionY) {
    this.positionY = positionY;
  }

  /**
   *
   * @return X-align setting of field.
   */
  public int getAlignX() {
    return alignX;
  }

  /**
   *
   * @param alignX X-align setting of field.
   */
  public void setAlignX(int alignX) {
    this.alignX = alignX;
  }

  /**
   *
   * @return Y-align setting of field.
   */
  public int getAlignY() {
    return alignY;
  }

  /**
   *
   * @param alignY Y-align setting of field.
   */
  public void setAlignY(int alignY) {
    this.alignY = alignY;
  }

  /**
   * Returns page number.
   *
   * @return number of page.
   */
  public int getPageNumber() {
    return pageNumber;
  }

  /**
   * Sets page number.
   *
   * @param pageNumber page number.
   */
  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }
}

