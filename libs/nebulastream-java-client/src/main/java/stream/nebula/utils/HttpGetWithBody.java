package stream.nebula.utils;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

public class HttpGetWithBody extends HttpEntityEnclosingRequestBase {
    public final static String METHOD_NAME = "GET";
    public HttpGetWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }
    public HttpGetWithBody(final URI uri) {
        super();
        setURI(uri);
    }
    public HttpGetWithBody() { super(); }
    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}