package my.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.code.model.GameResult;
import my.code.service.GameResultsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LeaderboardController.class)
class LeaderboardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private GameResultsService service;

    @Test
    void testGetUserInfo() throws Exception {
        GameResult result = new GameResult(1, 1, 55);
        when(service.getTopUserResults(1)).thenReturn(Arrays.asList(result));

        mockMvc.perform(get("/api/userinfo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].levelId").value(1))
                .andExpect(jsonPath("$[0].result").value(55));
    }

    @Test
    void testGetLevelInfo() throws Exception {
        GameResult result = new GameResult(1, 3, 15);
        when(service.getTopLevelResults(3)).thenReturn(Arrays.asList(result));

        mockMvc.perform(get("/api/levelinfo/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].levelId").value(3))
                .andExpect(jsonPath("$[0].result").value(15));
    }

    @Test
    void testSetInfo() throws Exception {
        String json = "{\"user_id\":1,\"level_id\":1,\"result\":55}";
        mockMvc.perform(put("/api/setinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        verify(service).setResult(1, 1, 55);
    }

    @Test
    void testSetInfoInvalidInput() throws Exception {
        String json = "{\"user_id\":1}";
        mockMvc.perform(put("/api/setinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSetInfoMissingFields() throws Exception {
        String json = "{\"user_id\":1}";
        mockMvc.perform(put("/api/setinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.levelId").value("level_id must be positive"));
    }
}