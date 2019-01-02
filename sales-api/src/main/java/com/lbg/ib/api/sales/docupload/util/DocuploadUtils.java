package com.lbg.ib.api.sales.docupload.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class DocuploadUtils {

    private DocuploadUtils() {

    }

    public static URL appendURLPath(String uri, String appendPath) throws URISyntaxException, MalformedURLException {

        URI oldUri = new URI(uri);
        String query = oldUri.getQuery();
        String newPath = oldUri.getPath() + '/' + appendPath;
        URI tmpUri = oldUri.resolve(newPath);
        URI newUri = new URI(tmpUri.getScheme(), tmpUri.getAuthority(), tmpUri.getPath(), query, tmpUri.getFragment());
        URL url = null;
        url = newUri.toURL();
        return url;
    }

}
