package com.lbg.ib.api.sales.address.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sales.address.domain.Postcode;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.postcode.PostcodeSearchDAO;
import com.lbg.ib.api.sales.dto.postcode.PostalAddressDTO;
import com.lbg.ib.api.sales.dto.postcode.PostcodeDTO;

public class AddressServiceImplTest {

    public static final List<PostalAddress>    LIST_OF_POSTAL_ADDRESSES     = asList(
            new PostalAddress("d", "t", "c", "o", "sb", "b", "h", asList("als"), "p", "d"));
    public static final List<PostalAddressDTO> LIST_OF_POSTAL_ADDRESSES_DTO = asList(
            new PostalAddressDTO("d", "t", "p", "c", "o", "sb", "b", "h", asList("als"), "d"));
    public static final List<PostalAddressDTO> EMPTY_LIST_DTO               = emptyList();
    public static final ResponseError          RESPONSE_ERROR               = new ResponseError(
            ResponseErrorConstants.SERVICE_UNAVAILABLE, "Service Unavailable");
    public final PostcodeSearchDAO             postcodeSearchDao            = mock(PostcodeSearchDAO.class);
    public final GalaxyErrorCodeResolver       galaxyErrorCodeResolver      = mock(GalaxyErrorCodeResolver.class);
    public final LoggerDAO                     logger                       = mock(LoggerDAO.class);
    private PostcodeDTO                        POSTCODE_DTO                 = new PostcodeDTO("E1", "8EP");
    private Postcode                           POSTCODE;

    @Before
    public void setUp() throws Exception {
        POSTCODE = new Postcode("e18ep");
    }

    @Test
    public void shouldReturnListOfAddressWhenDaoReturnsPostalAddressDtos() throws Exception {
        when(postcodeSearchDao.search(POSTCODE_DTO)).thenReturn(withResult(LIST_OF_POSTAL_ADDRESSES_DTO));
        AddressServiceImpl addressService = new AddressServiceImpl(postcodeSearchDao, galaxyErrorCodeResolver, logger);

        assertThat(addressService.check(POSTCODE), is(LIST_OF_POSTAL_ADDRESSES));
    }

    @Test
    public void shouldReturnEmptyListOfAddressesWhenThereIsNoPostalAddressReturned() throws Exception {
        when(postcodeSearchDao.search(POSTCODE_DTO)).thenReturn(withResult(EMPTY_LIST_DTO));
        AddressServiceImpl addressService = new AddressServiceImpl(postcodeSearchDao, galaxyErrorCodeResolver, logger);

        assertTrue(addressService.check(POSTCODE).isEmpty());

    }

    @Test
    public void shouldReturnEmptyListOfAddressesWhenPostcodeDaoReturnsNullForPostalAddresses() throws Exception {
        when(postcodeSearchDao.search(POSTCODE_DTO)).thenReturn(withResult((List<PostalAddressDTO>) null));
        AddressServiceImpl addressService = new AddressServiceImpl(postcodeSearchDao, galaxyErrorCodeResolver, logger);

        assertTrue(addressService.check(POSTCODE).isEmpty());
    }

    @Test
    public void shouldThrowServiceExceptionWithMappedGalaxyResponseErrorWhenPostcodeSearchDaoReturnsWithError()
            throws Exception {
        when(postcodeSearchDao.search(POSTCODE_DTO))
                .thenReturn(DAOResponse.<List<PostalAddressDTO>> withError(new DAOError("hostErrorCode", "message")));
        when(galaxyErrorCodeResolver.resolve("hostErrorCode")).thenReturn(RESPONSE_ERROR);
        AddressServiceImpl addressService = new AddressServiceImpl(postcodeSearchDao, galaxyErrorCodeResolver, logger);

        try {
            addressService.check(POSTCODE);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(RESPONSE_ERROR));
        }
    }

    @Test
    public void shouldLogAndThrowServiceExceptionWhenPostcodeSearchDAOThrowsException() throws Exception {
        IOException ioException = new IOException("No connection");
        when(postcodeSearchDao.search(POSTCODE_DTO)).thenThrow(ioException);
        AddressServiceImpl addressService = new AddressServiceImpl(postcodeSearchDao, galaxyErrorCodeResolver, logger);

        try {
            addressService.check(POSTCODE);
            fail();
        } catch (ServiceException e) {
            verify(logger).logException(AddressServiceImpl.class, ioException);
        }

    }

}