package com.lbg.ib.api.sales.product.resources;


import com.lbg.ib.api.sales.product.service.WorkingDaysService;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by 8796528 on 09/04/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class WorkingDaysResourceTest {

    @Mock
    private ReferenceDataServiceDAO referenceData;

    @Mock
    private WorkingDaysService fetchNextWorkingDaysService;

    @InjectMocks
    WorkingDaysResource fetchNextWorkingDaysResource;


    @Test
    public void testFetchWorkingDays() {
        Map<String,String> map = new HashMap<String,String>();
        Map<String,String> bankHolidaysMap = new HashMap<String,String>();
        bankHolidaysMap.put("2018-04-11 00:00:00.0",null);
        when(referenceData.getBankHolidays()).thenReturn(map);

        List<String> workingDays = new ArrayList<String>();
        workingDays.add("2018-04-10 13:35:13.923");
        workingDays.add("2018-04-11 13:35:13.923");
        workingDays.add("2018-04-12 13:35:13.923");

        when(fetchNextWorkingDaysService.fetchNextWorkingDays(any(Set.class),any(Integer.class),any(Date.class))).thenReturn(workingDays);
        Response result = fetchNextWorkingDaysResource.fetchNextWorkingDaysAfterWeek(new Integer(30));
        assertThat(result.getStatus(), is(Response.Status.OK.getStatusCode()));
    }
}
