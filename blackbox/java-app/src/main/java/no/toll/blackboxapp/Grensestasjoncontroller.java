package no.toll.blackboxapp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@RestController
public class Grensestasjoncontroller {

    @Value("${internal_h_last_api_url}")
    public String hardenedLastebilApiBaseUrl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/grensestasjoner")
    public Object getAllGrensestasjonerEndepunkt() {
        String sql = "SELECT * from api.grensestasjoner;";
        Object someobject;
        try {
            someobject = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Grensestasjon.class));
        } catch (Exception e) {
            someobject = e;
        }
        return someobject;
    }

    @GetMapping("/grensestasjoner/{grensestasjonId}")
    public Object getGrensestasjonMedPasseringer(@PathVariable(value = "grensestasjonId") int id) {

        if (id == 1 || id == 2) {

            String sql = "SELECT * from api.grensestasjoner where id =" + id + ";";
            Grensestasjon grensestasjon;
            try {
                grensestasjon = jdbcTemplate.queryForObject(sql,
                        BeanPropertyRowMapper.newInstance(Grensestasjon.class));
            } catch (DataAccessException e) {
                return new ResponseEntity(HttpStatusCode.valueOf(500));
            }

            try {

                WebClient client = WebClient.builder()
                        .baseUrl(hardenedLastebilApiBaseUrl)
                        .build();

                ResponseSpec responseSpec = client.get().uri("/api/lastebiler")
                        .headers(h -> h.setBearerAuth(System.getenv("hardened_lastebil_api_read_token")))
                        .accept(MediaType.APPLICATION_JSON).retrieve();

                List<Lastebilpassering> lastebilPasseringer = responseSpec.toEntityList(Lastebilpassering.class).log()
                        .block().getBody();
                List<Lastebilpassering> filteredLastebilPasseringer = lastebilPasseringer.stream()
                        .filter(l -> l.getTollstasjon() == id).collect(Collectors.toList());

                return new GrensestasjonMedGrensepasseringer(grensestasjon, filteredLastebilPasseringer);

            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatusCode.valueOf(503));
            }
        } else {
            return new ResponseEntity(HttpStatusCode.valueOf(404));
        }

    }

    @GetMapping("/health")
    public String health() {
        return "Healthy";
    }
}
