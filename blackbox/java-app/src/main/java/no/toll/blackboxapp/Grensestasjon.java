package no.toll.blackboxapp;

public class Grensestasjon {

    public int id;
    public String navn;
    public String kommune;
    
    public Grensestasjon(){}

    public Grensestasjon(int id, String navn, String kommune) {
        this.id = id;
        this.navn = navn;
        this.kommune = kommune;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getKommune() {
        return kommune;
    }

    public void setKommune(String kommune) {
        this.kommune = kommune;
    }

    
}
