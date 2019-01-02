package com.lbg.ib.api.sales.common.servlet;

/**
 * This servlet is being used to serve font files of EOT extension to CWA. This is being done to
 * mitigate the issue in IIS server which does not serve back EOT extension
 * @author Debashish Bhattacharjee | 17-November-2016

 */

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.lloydstsb.ea.logging.factory.LoggingServiceFactory;
import com.lloydstsb.ea.logging.interfaces.LoggingService;

public class FontServlet extends HttpServlet {
    private static final String                  CACHE_MAX_AGE   = "cacheMaxAge";
    private static final String                  CONTENT_TYPE    = "application/octet-stream";
    private static final HashMap<String, byte[]> FONT_COLLECTION = new HashMap<String, byte[]>();
    private static final LoggingService          LOGGER          = LoggingServiceFactory.getLoggingService();
    private String                               cacheMaxAge;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cacheMaxAge = config.getInitParameter(CACHE_MAX_AGE);
        if (StringUtils.isEmpty(cacheMaxAge)) {
            cacheMaxAge = "86400";
        }
    }

    /**
     * Process the HTTP doGet request.
     */

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String fontPath = request.getServletPath() + request.getPathInfo();

        byte[] binaryContent = FONT_COLLECTION.get(fontPath);

        if (null == binaryContent) {
            InputStream inputStream = null;
            try {
                inputStream = this.getClass().getResourceAsStream(fontPath);
                if (null != inputStream) {
                    binaryContent = IOUtils.toByteArray(inputStream);
                    if (null != binaryContent) {
                        FONT_COLLECTION.put(fontPath, binaryContent);
                    }
                }
            } catch (Exception exception) {
                LOGGER.logException(exception);
            } finally {
                if (null != inputStream) {
                    inputStream.close();
                }
            }
        }

        if (null != binaryContent) {
            response.setContentType(CONTENT_TYPE);
            response.setHeader(HttpHeaders.CACHE_CONTROL, cacheMaxAge);
            ServletOutputStream out = response.getOutputStream();
            out.write(binaryContent);
            out.flush();
            out.close();
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }
}