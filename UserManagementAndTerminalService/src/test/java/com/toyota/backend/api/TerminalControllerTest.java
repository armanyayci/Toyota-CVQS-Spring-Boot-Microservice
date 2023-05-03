package com.toyota.backend.api;

import com.toyota.backend.TestUtils;
import com.toyota.backend.service.Concrete.TerminalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Arman Yaycı
 * @since 01.05.2023
 * Unit tests for the {@link TerminalController} class.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TerminalController.class)
class TerminalControllerTest extends TestUtils {
    private final static String CONTENT_TYPE = "application/json";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TerminalServiceImpl terminalService;

    @Test
    public void activeTerminals_whenCalled_itShouldReturnOk() throws Exception{
        int page = 0;
        int size = 5;
        String sortBy = "id";
        String filterCategory = "";
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/terminal/list")
                        .contentType(CONTENT_TYPE))
                .andExpect(status().isOk());
        verify(terminalService, times(1)).getTerminals(page,size,sortBy,filterCategory);
        actions.andExpect(status().isOk());
    }

}