package torumpca.pl.gut.mt.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import torumpca.pl.gut.mt.algorithm.ProblemResolver;
import torumpca.pl.gut.mt.forecast.DataNotAvailableException;
import torumpca.pl.gut.mt.forecast.ForecastDataAdapter;
import torumpca.pl.gut.mt.forecast.ForecastDataAdapterFactory;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SolutionController.class)
class SolutionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProblemResolver problemResolver;

    @MockBean
    private ForecastDataAdapterFactory adapterFactory;

    @Test
    void givenRequestForUnavailableData_returnsErrorResponse() throws Exception {

        ForecastDataAdapter forecastDataAdapter = mock(ForecastDataAdapter.class);
        given(forecastDataAdapter.getWindForecast(any())).willThrow(new DataNotAvailableException("No data"));
        given(adapterFactory.getDataAdapter(anyBoolean())).willReturn(forecastDataAdapter);

        mvc.perform(post("/api/solve")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dXNlcjpwYXNz"))
                .andExpect(status().isBadRequest());
    }

}