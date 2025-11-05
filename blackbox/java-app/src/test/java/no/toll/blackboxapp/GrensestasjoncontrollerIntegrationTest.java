package no.toll.blackboxapp;

import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GrensestasjoncontrollerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Grensestasjoncontroller controller;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        ReflectionTestUtils.setField(controller, "hardenedLastebilApiBaseUrl", mockWebServer.url("/").toString());
        Mockito.reset(jdbcTemplate);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getAllGrensestasjonerReturnsRowsFromDatabase() throws Exception {
        List<Grensestasjon> grensestasjoner = List.of(new Grensestasjon(1, "Svinesund", "Halden"));

        Mockito.when(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(grensestasjoner);

        mockMvc.perform(get("/grensestasjoner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].navn").value("Svinesund"))
                .andExpect(jsonPath("$[0].kommune").value("Halden"));
    }

    @Test
    void getGrensestasjonMedPasseringerReturnsFilteredPasseringer() throws Exception {
        Grensestasjon grensestasjon = new Grensestasjon(1, "Svinesund", "Halden");

        Mockito.when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(grensestasjon);

        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""
                        [
                          {"id":1,"kommersiell_aktor":true,"registreringsnummer":"FD91232","selskap":"SOR Cargo","passert_tollstasjon":"2023-10-02T16:32:00+00:00","tollstasjon_passert":1},
                          {"id":2,"kommersiell_aktor":false,"registreringsnummer":"AB12345","selskap":"Nord Cargo","passert_tollstasjon":"2023-10-01T16:32:00+00:00","tollstasjon_passert":2}
                        ]
                        """));

        withEnvironmentVariable("hardened_lastebil_api_read_token", "token").execute(() -> {
            mockMvc.perform(get("/grensestasjoner/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.grensestasjon.id").value(1))
                    .andExpect(jsonPath("$.grensestasjon.navn").value("Svinesund"))
                    .andExpect(jsonPath("$.lastebilPasseringer", hasSize(1)))
                    .andExpect(jsonPath("$.lastebilPasseringer[0].tollstasjon_passert").value(1));
        });

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath()).isEqualTo("/api/lastebiler");
        assertThat(request.getHeader("Authorization")).isEqualTo("Bearer token");
    }

    @Test
    void getGrensestasjonMedPasseringerReturnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/grensestasjoner/42"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getGrensestasjonMedPasseringerReturnsInternalServerErrorWhenDatabaseFails() throws Exception {
        Mockito.when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class)))
                .thenThrow(new DataAccessResourceFailureException("Database unavailable"));

        withEnvironmentVariable("hardened_lastebil_api_read_token", "token").execute(() ->
                mockMvc.perform(get("/grensestasjoner/1"))
                        .andExpect(status().isInternalServerError()));
    }

    @Test
    void getGrensestasjonMedPasseringerReturnsServiceUnavailableWhenUpstreamFails() throws Exception {
        Grensestasjon grensestasjon = new Grensestasjon(1, "Svinesund", "Halden");

        Mockito.when(jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class)))
                .thenReturn(grensestasjon);

        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        withEnvironmentVariable("hardened_lastebil_api_read_token", "token").execute(() ->
                mockMvc.perform(get("/grensestasjoner/1"))
                        .andExpect(status().isServiceUnavailable()));

        mockWebServer.takeRequest();
    }
}
