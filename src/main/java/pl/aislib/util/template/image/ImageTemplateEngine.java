package pl.aislib.util.template.image;
/*
 * @(#)GIFEncoder.java    0.90 4/21/96 Adam Doppelt
 */
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Frame;
import java.awt.MediaTracker;

import java.awt.image.PixelGrabber;

/**
 * Default implementation of {@link ImageTemplateProducer} and {@link ImageTemplateOutputter}.
 *
 * <p>This implementation uses {@link Toolkit#createImage(byte[])} to load images and
 * code of <a href='http://www.cs.brown.edu/people/amd/'>Adam Doppelt</a>
 * (<a href="http://www.gurge.com/amd/old/java/GIFEncoder/">code here</a>)
 * to serialize images as GIF files.</p>
 *
 * <p>GIF serializing code is based upon gifsave.c, which was written and released
 * by:<p>
 * <center>
 *  Sverre H. Huseby<br>
 *  Bjoelsengt. 17<br>
 *  N-0468 Oslo<br>
 *  Norway<br>
 *  Phone: +47 2 230539<br>
 *  sverrehu@ifi.uio.no
 * </center>
 * @author Tomasz Pik, AIS.PL
 * @since AISLIB 0.4
 */
public class ImageTemplateEngine extends ImageTemplateProducer implements ImageTemplateOutputter {

  /**
   * Sets this object as an outputter.
   */
  public ImageTemplateEngine() {
    setOutputter(this);
  }

  /**
   * Create {@link Image} instaces using {@link Toolkit#createImage(byte[])} method.
   *
   * Using default JVM method for creating images. Reads given stream
   * into array of bytes and call {@link Toolkit#createImage(byte[])} method.
   *
   * @param stream contains image.
   * @return loaded image.
   * @throws NullPointerException if <code>stream</code> is <em>null</em>.
   * @throws IOException during I/O operations.
   */
  public Image loadImage(InputStream stream) throws IOException {
    if (stream == null) {
      throw new NullPointerException("stream cannot be null");
    }

    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int tmp;
    while ((tmp = stream.read()) != -1) {
      buffer.write(tmp);
    }
    byte[] byteArray = buffer.toByteArray();

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Image result = toolkit.createImage(byteArray);
    Frame frame = new Frame();
    MediaTracker tracker = new MediaTracker(frame);
    tracker.addImage(result, 0);
    try {
      tracker.waitForAll();
    } catch (InterruptedException ie) {
      throw new IOException("InterruptedException: " + ie.getMessage());
    }
    result.getWidth(frame);
    result.getHeight(frame);
    return result;
  }

  /**
   * Write image to stream as using GIF Format.
   *
   * @param image to be written.
   * @param stream where image should be written.
   * @throws IOException if there's a problem while writting.
   * @throws RuntimeException if given image has more than 255 colors or
   *         if there's not enough memory to load image.
   */
  public void writeImage(Image image, OutputStream stream) throws IOException {
    GIFEncoder encoder = new GIFEncoder(image);
    encoder.write(stream);
  }

  private class GIFEncoder {
    private short width, height;
    private int numColors;
    private byte pixels[], colors[];

    /**
     * Construct a GIFEncoder. The constructor will convert the image to
     * an indexed color array. <B>This may take some time.</B><P>
     *
     * @param image The image to encode. The image <B>must</B> be
     * completely loaded.
     */
    GIFEncoder(Image image) {
      width = (short) image.getWidth(null);
      height = (short) image.getHeight(null);

      int values[] = new int[width * height];
      PixelGrabber grabber = new PixelGrabber(
          image, 0, 0, width, height, values, 0, width);

      try {
        if (grabber.grabPixels() != true) {
          throw new RuntimeException("Grabber returned false: " + grabber.status());
        }
      } catch (InterruptedException e) {
        ;
      }

      byte r[][] = new byte[width][height];
      byte g[][] = new byte[width][height];
      byte b[][] = new byte[width][height];
      int index = 0;
      for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
          r[x][y] = (byte) ((values[index] >> 16) & 0xFF);
          g[x][y] = (byte) ((values[index] >> 8) & 0xFF);
          b[x][y] = (byte) ((values[index]) & 0xFF);
          ++index;
        }
      }
      toIndexedColor(r, g, b);
    }

    /**
     * Construct a GIFEncoder. The constructor will convert the image to
     * an indexed color array. <B>This may take some time.</B><P>
     *
     * Each array stores intensity values for the image. In other words,
     * r[x][y] refers to the red intensity of the pixel at column x, row
     * y.<P>
     *
     * @param r An array containing the red intensity values.
     * @param g An array containing the green intensity values.
     * @param b An array containing the blue intensity values.
     *
     * @exception AWTException Will be thrown if the image contains more than
     * 256 colors.
     */
    GIFEncoder(byte r[][], byte g[][], byte b[][]) {
      width = (short) (r.length);
      height = (short) (r[0].length);

      toIndexedColor(r, g, b);
    }

    /**
     * Writes the image out to a stream in the GIF file format. This will
     * be a single GIF87a image, non-interlaced, with no background color.
     * <B>This may take some time.</B><P>
     *
     * @param output The stream to output to. This should probably be a
     * buffered stream.
     *
     * @exception IOException Will be thrown if a write operation fails.
     * */
    void write(OutputStream output) throws IOException {
      BitUtils.writeString(output, "GIF87a");

      ScreenDescriptor sd = new ScreenDescriptor(width, height, numColors);
      sd.write(output);

      output.write(colors, 0, colors.length);

      ImageDescriptor id = new ImageDescriptor(width, height, ',');
      id.write(output);

      byte codesize = BitUtils.bitsNeeded(numColors);
      if (codesize == 1) {
        ++codesize;
      }
      output.write(codesize);

      LZWCompressor.doLZWCompress(output, codesize, pixels);
      output.write(0);

      id = new ImageDescriptor((byte) 0, (byte) 0, ';');
      id.write(output);
      output.flush();
    }

    void toIndexedColor(byte r[][], byte g[][], byte b[][]) {
      pixels = new byte[width * height];
      colors = new byte[256 * 3];
      int colornum = 0;
      for (int x = 0; x < width; ++x) {
        for (int y = 0; y < height; ++y) {
          int search;
          for (search = 0; search < colornum; ++search) {
            if (colors[search * 3]     == r[x][y] &&
                colors[search * 3 + 1] == g[x][y] &&
                colors[search * 3 + 2] == b[x][y]) {
              break;
            }
          }

          if (search > 255) {
            throw new RuntimeException("Too many colors.");
          }

          pixels[y * width + x] = (byte) search;

          if (search == colornum) {
            colors[search * 3]     = r[x][y];
            colors[search * 3 + 1] = g[x][y];
            colors[search * 3 + 2] = b[x][y];
            ++colornum;
          }
        }
      }
      numColors = 1 << BitUtils.bitsNeeded(colornum);
      byte copy[] = new byte[numColors * 3];
      System.arraycopy(colors, 0, copy, 0, numColors * 3);
      colors = copy;
    }
  }

  private static class BitFile {
    private OutputStream output;
    private byte buffer[];
    private int index, bitsLeft;

    public BitFile(OutputStream output) {
      this.output = output;
      buffer = new byte[256];
      index = 0;
      bitsLeft = 8;
    }

    public void flush() throws IOException {
      int numBytes = index + (bitsLeft == 8 ? 0 : 1);
      if (numBytes > 0) {
        output.write(numBytes);
        output.write(buffer, 0, numBytes);
        buffer[0] = 0;
        index = 0;
        bitsLeft = 8;
      }
    }

    public void writeBits(int bits, int numbits) throws IOException {
      int bitsWritten = 0;
      int numBytes = 255;
      do {
        if ((index == 254 && bitsLeft == 0) || index > 254) {
          output.write(numBytes);
          output.write(buffer, 0, numBytes);

          buffer[0] = 0;
          index = 0;
          bitsLeft = 8;
        }

        if (numbits <= bitsLeft) {
          buffer[index] |= (bits & ((1 << numbits) - 1)) << (8 - bitsLeft);
          bitsWritten += numbits;
          bitsLeft -= numbits;
          numbits = 0;
        } else {
          buffer[index] |= (bits & ((1 << bitsLeft) - 1)) << (8 - bitsLeft);
          bitsWritten += bitsLeft;
          bits >>= bitsLeft;
          numbits -= bitsLeft;
          buffer[++index] = 0;
          bitsLeft = 8;
        }
      } while (numbits != 0);
    }
  }

  private static class LZWStringTable {
    private static final int RES_CODES = 2;
    private static final short HASH_FREE = (short) 0xFFFF;
    private static final short NEXT_FIRST = (short) 0xFFFF;
    private static final int MAXBITS = 12;
    private static final int MAXSTR = (1 << MAXBITS);
    private static final short HASHSIZE = 9973;
    private static final short HASHSTEP = 2039;

    private byte strChr[];
    private short strNxt[];
    private short strHsh[];
    private short numStrings;

    LZWStringTable() {
      strChr = new byte[MAXSTR];
      strNxt = new short[MAXSTR];
      strHsh = new short[HASHSIZE];
    }

    int addCharString(short index, byte b) {
      int hshidx;

      if (numStrings >= MAXSTR) {
        return 0xFFFF;
      }

      hshidx = hash(index, b);
      while (strHsh[hshidx] != HASH_FREE) {
        hshidx = (hshidx + HASHSTEP) % HASHSIZE;
      }

      strHsh[hshidx] = numStrings;
      strChr[numStrings] = b;
      strNxt[numStrings] = (index != HASH_FREE) ? index : NEXT_FIRST;

      return numStrings++;
    }

    short findCharString(short index, byte b) {
      int hshidx, nxtidx;

      if (index == HASH_FREE) {
        return b;
      }

      hshidx = hash(index, b);
      while ((nxtidx = strHsh[hshidx]) != HASH_FREE) {
        if (strNxt[nxtidx] == index && strChr[nxtidx] == b) {
          return (short) nxtidx;
        }
        hshidx = (hshidx + HASHSTEP) % HASHSIZE;
      }

      return (short) 0xFFFF;
    }

    void clearTable(int codesize) {
      numStrings = 0;

      for (int q = 0; q < HASHSIZE; q++) {
        strHsh[q] = HASH_FREE;
      }

      int w = (1 << codesize) + RES_CODES;
      for (int q = 0; q < w; q++) {
        addCharString((short) 0xFFFF, (byte) q);
      }
    }

    static int hash(short index, byte lastbyte) {
      return ((int) ((short) (lastbyte << 8) ^ index) & 0xFFFF) % HASHSIZE;
    }
  }

  private static class LZWCompressor {

    static void doLZWCompress(OutputStream output, int codesize, byte toCompress[]) throws IOException {
      byte c;
      short index;
      int clearcode, endofinfo, numbits, limit;
      //int  errcode;
      short prefix = (short) 0xFFFF;

      BitFile bitFile = new BitFile(output);
      LZWStringTable strings = new LZWStringTable();

      clearcode = 1 << codesize;
      endofinfo = clearcode + 1;

      numbits = codesize + 1;
      limit = (1 << numbits) - 1;

      strings.clearTable(codesize);
      bitFile.writeBits(clearcode, numbits);

      for (int loop = 0; loop < toCompress.length; ++loop) {
        c = toCompress[loop];
        if ((index = strings.findCharString(prefix, c)) != -1) {
          prefix = index;
        } else {
          bitFile.writeBits(prefix, numbits);
          if (strings.addCharString(prefix, c) > limit) {
            if (++numbits > 12) {
              bitFile.writeBits(clearcode, numbits - 1);
              strings.clearTable(codesize);
              numbits = codesize + 1;
            }
            limit = (1 << numbits) - 1;
          }

          prefix = (short) ((short) c & 0xFF);
        }
      }

      if (prefix != -1) {
        bitFile.writeBits(prefix, numbits);
      }
      bitFile.writeBits(endofinfo, numbits);
      bitFile.flush();
    }
  }

  private class ScreenDescriptor {
    private short localScreenWidth, localScreenHeight;
    private byte byteVar;
    private byte backgroundColorIndex, pixelAspectRatio;

    ScreenDescriptor(short width, short height, int numColors) {
      localScreenWidth = width;
      localScreenHeight = height;
      setGlobalColorTableSize((byte) (BitUtils.bitsNeeded(numColors) - 1));
      setGlobalColorTableFlag((byte) 1);
      setSortFlag((byte) 0);
      setColorResolution((byte) 7);
      backgroundColorIndex = 0;
      pixelAspectRatio = 0;
    }

    void write(OutputStream output) throws IOException {
      BitUtils.writeWord(output, localScreenWidth);
      BitUtils.writeWord(output, localScreenHeight);
      output.write(byteVar);
      output.write(backgroundColorIndex);
      output.write(pixelAspectRatio);
    }

    void setGlobalColorTableSize(byte num) {
      byteVar |= (num & 7);
    }

    void setSortFlag(byte num) {
      byteVar |= (num & 1) << 3;
    }

    void setColorResolution(byte num) {
      byteVar |= (num & 7) << 4;
    }

    void setGlobalColorTableFlag(byte num) {
      byteVar |= (num & 1) << 7;
    }
  }

  private class ImageDescriptor {
    private byte separator;
    private short leftPosition, topPosition, width, height;
    private byte byteVar;

    ImageDescriptor(short width, short height, char separator) {
      this.separator = (byte) separator;
      leftPosition = 0;
      topPosition = 0;
      this.width = width;
      this.height = height;
      setLocalColorTableSize((byte) 0);
      setReserved((byte) 0);
      setSortFlag((byte) 0);
      setInterlaceFlag((byte) 0);
      setLocalColorTableFlag((byte) 0);
    }

    void write(OutputStream output) throws IOException {
      output.write(separator);
      BitUtils.writeWord(output, leftPosition);
      BitUtils.writeWord(output, topPosition);
      BitUtils.writeWord(output, width);
      BitUtils.writeWord(output, height);
      output.write(byteVar);
    }

    void setLocalColorTableSize(byte num) {
      byteVar |= (num & 7);
    }

    void setReserved(byte num) {
      byteVar |= (num & 3) << 3;
    }

    void setSortFlag(byte num) {
      byteVar |= (num & 1) << 5;
    }

    void setInterlaceFlag(byte num) {
      byteVar |= (num & 1) << 6;
    }

    void setLocalColorTableFlag(byte num) {
      byteVar |= (num & 1) << 7;
    }
  }

  private static class BitUtils {
    static byte bitsNeeded(int n) {
      byte ret = 1;

      if (n-- == 0) {
        return 0;
      }

      while ((n >>= 1) != 0) {
        ++ret;
      }

      return ret;
    }

    static void writeWord(OutputStream output, short w) throws IOException {
      output.write(w & 0xFF);
      output.write((w >> 8) & 0xFF);
    }

    static void writeString(OutputStream output, String string) throws IOException {
      for (int loop = 0; loop < string.length(); ++loop) {
        output.write((byte) (string.charAt(loop)));
      }
    }
  }
}

