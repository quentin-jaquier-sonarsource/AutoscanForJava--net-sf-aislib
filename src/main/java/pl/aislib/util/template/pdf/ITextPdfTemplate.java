package pl.aislib.util.template.pdf;

import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.aislib.util.template.Field;
import pl.aislib.util.template.Template;
import pl.aislib.util.template.image.FieldRenderer;
import pl.aislib.util.template.image.SimpleFieldRenderer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Template for generating PDFs.
 *
 * Built upon the iText library. Uses AWT to draw into PDF.
 * Another implementation could use direct-PDF writing methods.<br>
 * Fields with page number set to <code>Field.DEFAULT_PAGE</code>
 * will be printed on every page of the document.
 * <br>
 * <em>Caution:</em> "serif" font
 * doesn't work. Use "Serif" instead.
 *
 * @author Milosz Tylenda, AIS.PL
 */
public class ITextPdfTemplate implements Template {

  private Map fields;
  private Map values;
  private byte[] blankPdf;

  /**
   * @param fields a <code>Map</code> with (name, value) pairs
   * @param inputStream a source of blank PDF document
   */
  public ITextPdfTemplate(Map fields, InputStream inputStream) throws IOException {
    this.fields = fields;
    values = new HashMap();
    byte[] buffer = new byte[32768];
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(32768);
    int bytesRead = 0;
    while (true) {
      bytesRead = inputStream.read(buffer);
      if (bytesRead == -1) { break; }
      outputStream.write(buffer, 0, bytesRead);
    }
    blankPdf = outputStream.toByteArray();
  }

  public void setValue(String key, Object value) {
    values.put(key, value);
  }

  public void setValues(Map map) {
    values.putAll(map);
  }

  /**
   * @see Template#writeTo(java.io.OutputStream)
   */
  public void writeTo(OutputStream stream) throws IOException {
    try {

      PdfReader pdfReader = new PdfReader(blankPdf);
      int numberOfPages = pdfReader.getNumberOfPages();

      // i-th element of this table is a list which holds field names to be drawn on the i-th page.
      // element at index 0 not used.
      List fieldsOnPage[] = new List[numberOfPages + 1];
      for (int i = 0 ; i < fieldsOnPage.length ; i++) {
        fieldsOnPage[i] = new ArrayList();
      }

      // Fill the per-page lists. Walk thru values and add used fields to the per-page lists.
      for (Iterator keys = values.keySet().iterator() ; keys.hasNext() ;) {
        String fieldName = (String) keys.next();
        Field field = (Field) fields.get(fieldName);
        if (field == null) {
          continue; // silently ignore non-existing fields
        }
        int pageNumber = field.getPageNumber();
        if (pageNumber == Field.DEFAULT_PAGE) {
          for (int i = 1 ; i < fieldsOnPage.length ; i++) {
            fieldsOnPage[i].add(fieldName); //fields with DEFAULT_PAGE will be printed on every pg.
          }
        } else {
          try {
            fieldsOnPage[pageNumber].add(fieldName);
          } catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new IllegalArgumentException("Page number " + pageNumber + " not found in the PDF document.");
          }
        }
      }

      // Start PDFing
      Document document = new Document();
      PdfWriter pdfWriter = PdfWriter.getInstance(document, stream);
      document.setPageSize(pdfReader.getPageSize(1));  // Set the size of the first page.
      document.open();
      PdfContentByte cb = pdfWriter.getDirectContent();
      DefaultFontMapper mapper = new DefaultFontMapper();
      FieldRenderer renderer = new SimpleFieldRenderer();

      // The *main* loop
      for (int i = 1 ; i <= numberOfPages ; i++) {
        PdfImportedPage page = pdfWriter.getImportedPage(pdfReader, i);
        cb.addTemplate(page, 0, 0);
        if (!fieldsOnPage[i].isEmpty()) {
          Rectangle pageSize = pdfReader.getPageSize(i);
          Graphics2D g2 = cb.createGraphics(pageSize.width(), pageSize.height(), mapper);

          // The per-page loop
          List fieldsOnThisPage = fieldsOnPage[i];
          for (int j = 0, jsize = fieldsOnThisPage.size() ; j < jsize ; j++) {
            String fieldName = (String) fieldsOnThisPage.get(j);
            Field field = (Field) fields.get(fieldName);
            renderer.render(g2, field, values.get(fieldName));
          }
          g2.dispose();
        }

        // Do a page break and set size of the next page.
        if (i != numberOfPages) {
          document.newPage();
          document.setPageSize(pdfReader.getPageSize(i + 1));
        }
      }

      document.close();
    } catch (DocumentException de) {
      throw new IOException(de.getMessage());
    }
  }

  /**
   * @see Template#toByteArray()
   */
  public byte[] toByteArray() {
    try {
      ByteArrayOutputStream bOut = new ByteArrayOutputStream(32768);
      writeTo(bOut);
      return bOut.toByteArray();
    } catch (IOException ioe) {
      throw new RuntimeException("exception caught during processing: " + ioe.getMessage());
    }
  }
}
