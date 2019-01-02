package integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.Response;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.lbg.ib.api.sales.dao.content.ContentDAOImpl;
import com.lbg.ib.api.sales.dao.content.ContentTargetUrlResolver;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * This test is to show us how to configure our content server and fetch content
 * from teamsite.
 ***********************************************************************/
public class ContentDAOImplIT {

    private final ApiServiceProperties     properties = mock(ApiServiceProperties.class);
    private final ContentTargetUrlResolver resolver   = mock(ContentTargetUrlResolver.class);
    private final LoggerDAO                logger     = mock(LoggerDAO.class);

    private final ContentDAOImpl           contentDAO = new ContentDAOImpl(properties, resolver, logger);

    @Before
    public void setup() {
        when(properties.contentServiceBackendCallTimeoutInMillis()).thenReturn(5000);
    }

    @Test
    public void shouldBeAbleToDownloadContentForBos() throws Exception {
        // http://appduv005d-bp.machine.test.group:3003/sales-api/v2/content/pca/PCA001/P_CLASSIC/card_image_and_account_benefits_panel001?_=1453463557935

        String cwaSuppliedContentKey = "pca/PCA001/P_CLASSIC/card_image_and_account_benefits_panel001";
        String brand = "BOS";
        String contentServerUrlResourceBundleNsp = "testpreview_teamsite_bos_personal_banking";
        String contentServerUrlBrandNsp = "bospersonalbanking/cwa";

        givenTheFullyQualifedEndpoint(brand, contentServerUrlResourceBundleNsp, contentServerUrlBrandNsp,
                cwaSuppliedContentKey);

        String responseAsString = whenWeMakeASuccessfulRequestToTeamsiteServer(cwaSuppliedContentKey, brand);

        assertThat(responseAsString, containsString("classic_silver_card_padding_flat_x1-d0d7f47b67.png"));
    }

    @Test
    public void shouldBeAbleToDownloadContentForLloyds() throws Exception {
        // http://appduv005d-lp.machine.test.group:3003/sales-api/v2/content/pca/PCA001/P_CLASSIC/card_image_and_account_benefits_panel001?_=1453464023543
        String cwaSuppliedContentKey = "pca/PCA001/P_CLASSIC/card_image_and_account_benefits_panel001";

        String brand = "LLOYDS";
        String contentServerUrlResourceBundleNsp = "testpreview_teamsite_lloyds_personal_banking";
        String contentServerUrlBrandNsp = "LloydsPersonalBanking/cwa";

        givenTheFullyQualifedEndpoint(brand, contentServerUrlResourceBundleNsp, contentServerUrlBrandNsp,
                cwaSuppliedContentKey);

        String responseAsString = whenWeMakeASuccessfulRequestToTeamsiteServer(cwaSuppliedContentKey, brand);
        assertThat(responseAsString, containsString("DC-Classic-8ecfe40d2d.png"));
    }

    @Test
    public void shouldBeAbleToDownloadContentForHalifax() throws Exception {
        // http://appduv005d-hp.machine.test.group:3003/sales-api/v2/content/pca/PCA001/P_STANDARD/card_image_and_account_benefits_panel001?_=1453463789018
        String cwaSuppliedContentKey = "pca/PCA001/P_STANDARD/card_image_and_account_benefits_panel001";
        String brand = "HALIFAX";
        String contentServerUrlResourceBundleNsp = "testpreview_teamsite_halifax_personal_banking";
        String contentServerUrlBrandNsp = "HalifaxPersonalBanking/cwa";

        givenTheFullyQualifedEndpoint(brand, contentServerUrlResourceBundleNsp, contentServerUrlBrandNsp,
                cwaSuppliedContentKey);

        String responseAsString = whenWeMakeASuccessfulRequestToTeamsiteServer(cwaSuppliedContentKey, brand);
        assertThat(responseAsString, containsString("ca_card_flat_1x-4ae37222b9.PNG"));
    }

    private String whenWeMakeASuccessfulRequestToTeamsiteServer(String cwaSuppliedContentKey, String brand)
            throws IOException {
        Response response = contentDAO.content(brand, cwaSuppliedContentKey).getResult();

        assertThat(response.getStatus(), is(200));
        return IOUtils.toString((InputStream) response.getEntity());
    }

    private void givenTheFullyQualifedEndpoint(String brand, String contentServerUrlResourceBundleNsp,
            String contentServerUrlBrandNsp, String cwaSuppliedContentKey) throws URISyntaxException {
        String host = "sit-ib-pres-svr-content.service.test.group";
        String iisCmsServerUrlNsp = "http://" + host + "/wps/wcm/connect";
        String contentKeyLookupUrl = iisCmsServerUrlNsp + "/" + contentServerUrlResourceBundleNsp + "/en/"
                + contentServerUrlBrandNsp + "/" + cwaSuppliedContentKey;

        when(resolver.contentPath(brand, cwaSuppliedContentKey)).thenReturn(new URI(contentKeyLookupUrl));
        when(resolver.hostPath(brand)).thenReturn(host);
    }
}