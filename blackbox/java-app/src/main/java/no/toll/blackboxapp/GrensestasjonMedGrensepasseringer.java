package no.toll.blackboxapp;

import java.util.List;

public class GrensestasjonMedGrensepasseringer {

    private Grensestasjon grensestasjon;
    private List<Lastebilpassering> lastebilPasseringer;

    public GrensestasjonMedGrensepasseringer(Grensestasjon grensestasjon,
            List<Lastebilpassering> lastebilPasseringer) {
        this.grensestasjon = grensestasjon;
        this.lastebilPasseringer = lastebilPasseringer;
    }
    public Grensestasjon getGrensestasjon() {
        return grensestasjon;
    }
    public void setGrensestasjon(Grensestasjon grensestasjon) {
        this.grensestasjon = grensestasjon;
    }
    public List<Lastebilpassering> getLastebilPasseringer() {
        return lastebilPasseringer;
    }
    public void setLastebilPasseringer(List<Lastebilpassering> lastebilPasseringer) {
        this.lastebilPasseringer = lastebilPasseringer;
    }

    
    
}
