package pl.aislib.util.template.image;

import java.awt.Font;
import java.awt.Graphics;

import javax.swing.SwingUtilities;

import pl.aislib.util.template.Field;
import pl.aislib.util.template.TemplateFont;

/**
 * Base implementation of {@link FieldRenderer}.
 *
 * This class is used if there's no registered renderer for a given field or value class.
 *
 * Handles all classes (simply calls <code>value.toString()</code>.
 * Does not handle vertical alignment different than {@link Field#DEFAULT_ALIGN}.
 *
 * @author Tomasz Pik, AIS.PL
 * @since 0.3
 */
public class SimpleFieldRenderer implements FieldRenderer {

  public void render(Graphics graphics, Field field, Object value) {
    Font originalFont = graphics.getFont();
    Font font = createFont(field.getTemplateFont());
    graphics.setFont(font);
    if ((field.getTemplateFont() != null) && (field.getTemplateFont().getColor() != null)) {
      graphics.setColor(field.getTemplateFont().getColor());
    }
    if (field.getAlignY() != Field.DEFAULT_ALIGN) {
      throw new UnsupportedOperationException("only DEFAULT vertical alignment is supported");
    }
    drawString(graphics, value.toString(), field.getPositionX(), field.getPositionY(), field.getAlignX());
    graphics.setFont(originalFont);
  }

  protected void drawString(Graphics graphics, String value, int positionX, int positionY, int alignX) {
    if ((alignX == Field.ALIGN_CENTER) || (alignX == Field.ALIGN_RIGHT)) {
      int width = SwingUtilities.computeStringWidth(graphics.getFontMetrics(), value);
      if (alignX == Field.ALIGN_CENTER) {
        positionX = positionX - (int) (width / 2);
      } else {
        positionX = positionX - width;
      }
    }
    graphics.drawString(value, positionX, positionY);
  }
  
  protected Font createFont(TemplateFont templateFont) {
    if (templateFont == null) {
      return new Font(null, Font.PLAIN, 10);
    }
    int style = Font.PLAIN;
    if (templateFont.getStyle() == Font.ITALIC) {
      style = style | Font.ITALIC;
    }
    if (templateFont.getWeight() == Font.BOLD) {
      style = style | Font.BOLD;
    }
    return new Font(templateFont.getFontName(), style, templateFont.getSize());
  }
}
