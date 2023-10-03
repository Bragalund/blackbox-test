package no.toll.blackboxapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Grensestasjoncontroller {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/grensestasjoner")
    public Object getAllGrensestasjonerEndepunkt() {
        String sql = "SELECT * from api.grensestasjoner;";
        Object someobject;
        try {
            someobject = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Grensestasjon.class));
        }catch(Exception e){
            someobject = e;
        }
        return someobject;
    }

    @GetMapping("/health")
    public String health(){
        return "<3";
    }
}
