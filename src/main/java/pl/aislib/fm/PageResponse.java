package pl.aislib.fm;

import java.util.Map;

/**
 * Class encapsulating response from the {@link Page#getPageResponse} method.
 * 
 * Page Response should be in one of the main forms:
 * <ul>
 * <li>raw byte table, having specified content type, which will be send 
 *     without any modifications to Client</li>
 * <li>(key, value) response, possibly having specified content type, or using
 *     default "text/html"</li>
 * </ul>
 * Second type of response will be processed by {@link Application} class methods
 * (substituted into proper template) and send to Client.
 * <p>Note:<br>
 * {@link Map} given as argument for some of the constructors should always
 * contain pair ('page', Action Key for {@link Page} creating this response).
 * If such a Map will not contain this pair, we will modify it and add
 * appropriate pair.
 * 
 * @author
 * <table>
 *   <tr><td>Tomasz Pik, AIS.PL</td></tr>
 *   <tr><td>Michal Jastak, AIS.PL</td></tr>
 * </table>
 * @version $Revision: 1.3 $
 * @since AISLIB 0.1
 */
public class PageResponse {

  /**
   * Raw response content.
   */
  private byte[] contentByte;
  
  /**
   * Map of response content.
   */
  private Map contentMap;
  
  /**
   * Response content type.
   */
  private String contentType;
  
  /**
   * Page which creates the response.
   */
  private Page page;


  // Constructors
  
  /**
   * Sole constructor for empty response.
   * 
   * @param page {@link Page} creating this response.
   */
  public PageResponse(Page page) {
    this.page = page;
  }

  /**
   * Constructor for raw type response.
   * 
   * @param page {@link Page} creating this response.
   * @param content raw content of response written as byte table.
   * @param contentType content type of response.
   */
  public PageResponse(Page page, byte[] content, String contentType) {
    if ((content == null) || (contentType == null)) {
      throw new NullPointerException("content and contentType cannot be null");
    }

    contentByte = content;
    this.contentType = contentType;
    this.page        = page;
  }

  /**
   * Constructor for (key, value) type response.
   * 
   * @param page {@link Page} creating this response.
   * @param content response written as (key, value) mapping.
   */
  public PageResponse(Page page, Map content) {
    if (content == null) {
      throw new NullPointerException("content cannot be null");
    }

    contentMap  = content;
    this.page        = page;
    if ((contentMap != null) && (null == contentMap.get("page"))) {
      String actionKey = page.pageInfo.getActionKey ();
      contentMap.put ("page", actionKey);
    }
  }
  
  /**
   * Constructor for (key, value) type response, using specified content type for response.
   * 
   * @param page {@link Page} creating this response.
   * @param content response written as (key, value) mapping.
   * @param contentType content type of response.
   */
  public PageResponse(Page page, Map content, String contentType) {
    this(page, content);
    if (contentType == null) {
      throw new NullPointerException("contentType cannot be null");
    }
    this.contentType = contentType;
  }


  // Public methods
  
  /**
   * Returns response content type.
   * 
   * @return response content type (default "text/html").
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Returns response (key, value) mapping if any.
   * 
   * @return response as (key, value) mapping or null if not available
   */
  public Map getContentMap() {
    return contentMap;
  }

  /**
   * Returns raw response content if any.
   * 
   * @return raw response content as byte table or null if not available
   */
  public byte[] getContentByte() {
    return contentByte;
  }

  /**
   * Returns page which creates this response.
   * 
   * @return Page which creates this response.
   */
  Page getPage() {
    return page;
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append("Response from page: " + page + "\n");
    if (contentMap != null) {
      result.append("Response Map: " + contentMap + "\n");    
    }
    if (contentByte != null) {
      result.append("Response Byte Array: " + contentByte + "\n");    
    }
    return result.toString();
  }

} // PageResponse class
