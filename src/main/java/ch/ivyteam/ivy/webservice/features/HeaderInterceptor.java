package ch.ivyteam.ivy.webservice.features;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * An Out-Interceptor that adds a <code>Content-MD5</code> field to the http header after message content has been written.
 */
class HeaderInterceptor extends AbstractPhaseInterceptor<Message>
{
  public HeaderInterceptor()
  {
    super(null, Phase.PRE_PROTOCOL_ENDING, false);
  }

  @Override
  public void handleMessage(Message message)
  {
    // Properties configured for the Web Service Client can be accessed the following way:
    // String myPropertyValue = (String) message.getContextualProperty("myPropertyName");

    @SuppressWarnings("unchecked")
    Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
    OutputStream out = message.getContent(OutputStream.class);
    if (out instanceof CachedOutputStream)
    {
      String md5 = createMD5Hash((CachedOutputStream) out);
      headers.put("Content-MD5", Arrays.asList(new String[] {md5}));
    }
  }

  private String createMD5Hash(CachedOutputStream cout)
  {
    try
    {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      return Base64.getEncoder().encodeToString(md5.digest(cout.getBytes()));
    }
    catch (NoSuchAlgorithmException | IOException ex)
    {
      throw new RuntimeException("Error creating content message digest", ex);
    }
  }
}
