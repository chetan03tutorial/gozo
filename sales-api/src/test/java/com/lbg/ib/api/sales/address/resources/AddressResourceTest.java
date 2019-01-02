package com.lbg.ib.api.sales.address.resources;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sales.address.domain.Postcode;
import com.lbg.ib.api.sales.address.domain.RawPostCode;
import com.lbg.ib.api.sales.address.service.AddressService;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

@SuppressWarnings("unchecked")
public class AddressResourceTest {
    public static final List<PostalAddress> POSTAL_ADDRESSES    = asList(new PostalAddress());
    public static final List<PostalAddress> NO_POSTAL_ADDRESSES = asList();
    private AddressService                  addressService      = mock(AddressService.class);
    private RequestBodyResolver             resolver            = mock(RequestBodyResolver.class);

    @Test
    public void shouldReturnOkWhenTheAddressServiceReturnsWithPostalAddresses() throws Exception {
        when(addressService.check(new Postcode("E18ep"))).thenReturn(POSTAL_ADDRESSES);
        RawPostCode rawPostCode = new RawPostCode();
        rawPostCode.setPostCode("E18ep");
        when(resolver.resolve("E18ep", RawPostCode.class)).thenReturn(rawPostCode);
        Response result = new AddressResource(addressService, resolver).addressSearch("E18ep");

        assertThat(result.getStatus(), is(Status.OK.getStatusCode()));
        assertThat((List<PostalAddress>) result.getEntity(), is(POSTAL_ADDRESSES));
    }

    @Test
    public void shouldReturnNotFoundWhenTheAddressServiceReturnsEmptyListOfPostcodes() throws Exception {
        when(addressService.check(new Postcode("E18ep"))).thenReturn(NO_POSTAL_ADDRESSES);
        RawPostCode rawPostCode = new RawPostCode();
        rawPostCode.setPostCode("E18ep");
        when(resolver.resolve("E18ep", RawPostCode.class)).thenReturn(rawPostCode);
        Response result = new AddressResource(addressService, resolver).addressSearch("E18ep");
        assertThat(result.getStatus(), is(Status.OK.getStatusCode()));
    }

    @Test
    public void shouldLoadDefaultAddress() throws Exception {
        AddressResource res = new AddressResource();

        when(addressService.check(new Postcode("E18ep"))).thenReturn(NO_POSTAL_ADDRESSES);
        RawPostCode rawPostCode = new RawPostCode();
        rawPostCode.setPostCode("E18ep");
        when(resolver.resolve("E18ep", RawPostCode.class)).thenReturn(rawPostCode);
        Response result = new AddressResource(addressService, resolver).addressSearch("E18ep");
        assertThat(result.getStatus(), is(Status.OK.getStatusCode()));
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldReturnThrowInvalidFormatExceptionWhenPostcodeIsNotWellFormatted() throws Exception {
        RawPostCode rawPostCode = new RawPostCode();
        rawPostCode.setPostCode("E18epxxxxxx");
        when(resolver.resolve("E18epxxxxxx", RawPostCode.class)).thenReturn(rawPostCode);
        Response result = new AddressResource(addressService, resolver).addressSearch("E18epxxxxxx");
        assertThat(result.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
    }
}