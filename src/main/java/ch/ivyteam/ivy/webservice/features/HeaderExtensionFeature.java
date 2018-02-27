package ch.ivyteam.ivy.webservice.features;

import org.apache.cxf.Bus;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

/**
 * Web Service feature that adds a <code>Content-MD5</code> field to the http header.
 */
public class HeaderExtensionFeature extends AbstractFeature
{
  @Override
  protected void initializeProvider(InterceptorProvider provider, Bus bus)
  {
    provider.getOutInterceptors().add(new HeaderInterceptor());
  }
}
