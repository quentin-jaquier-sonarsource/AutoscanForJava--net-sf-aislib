package pl.aislib.util.template;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @since 0.2
 * @author Tomasz Pik, AIS.PL
 * @author Daniel Rychcik, AIS.PL
 */
public class TemplateFont {

  protected String name;
  protected String fontName;

  protected int size;
  protected int style;
  protected int weight;

  protected int red;
  protected int green;
  protected int blue;

  /**
   *
   * @param _fontName name of the <em>Font</em>.
   * @param _size of the font.
   * @param _style one of {@link java.awt.Font#PLAIN} or {@link java.awt.Font#ITALIC}.
   * @param _weight one of {@link java.awt.Font#PLAIN} or {@link java.awt.Font#BOLD}.
   * @param _color Color of the font.
   */
  public TemplateFont(String _fontName, int _size, int _style, int _weight, Color _color) {
    fontName = _fontName;
    size     = _size;
    style    = _style;
    weight   = _weight;

    red = _color.getRed();
    green = _color.getGreen();
    blue = _color.getBlue();
  }

  /**
   *
   * @param _fontName name of the <em>Font</em>.
   * @param _size of the font.
   * @param _style one of {@link java.awt.Font#PLAIN} or {@link java.awt.Font#ITALIC}.
   * @param _weight one of {@link java.awt.Font#PLAIN} or {@link java.awt.Font#BOLD}.
   */
  public TemplateFont(String _fontName, int _size, int _style, int _weight) {
    this(_fontName, _size, _style, _weight, Color.black);
  }

  public TemplateFont() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFontName() {
    return fontName;
  }

  public void setFontName(String fontName) {
    this.fontName = fontName;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getStyle() {
    return style;
  }

  public void setStyle(String style) {
    if (style.toUpperCase().equals("ITALIC")) {
      this.style = Font.ITALIC;
    } else {
      this.style = Font.PLAIN;
    }
  }

  public void setStyle(int style) {
    this.style = style;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(String weight) {
    if (weight.toUpperCase().equals("BOLD")) {
      this.weight = Font.BOLD;
    } else {
      this.weight = Font.PLAIN;
    }
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public void setRed(int red) {
    this.red = red;
  }

  public void setGreen(int green) {
    this.green = green;
  }

  public void setBlue(int blue) {
    this.blue = blue;
  }

  /**
   * Return the font color as java.awt.Color object. Internally, the
   * color is represented as RGB values.
   *
   * @return color represened internally as RGB values
   */
  public Color getColor() {
    return new Color(red, green, blue);
  }

  /**
   * Set font color from java.awt.Color object.
   *
   * @param color
   */
  public void setColor(Color color) {
    this.red = color.getRed();
    this.green = color.getGreen();
    this.blue = color.getBlue();
  }

  public String toString() {
    return "TemplateFont: " + fontName + ", " + size + "/" + style + "/" + weight;
  }
}

