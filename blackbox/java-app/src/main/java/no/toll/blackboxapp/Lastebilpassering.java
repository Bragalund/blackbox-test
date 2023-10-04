package no.toll.blackboxapp;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lastebilpassering {
    @JsonProperty("id")
    private int id;

    @JsonProperty("kommersiell_aktor")
    private boolean kommersiellAktor;

    @JsonProperty("registreringsnummer")
    private String registreringsnummer;

    @JsonProperty("selskap")
    private String selskap;

    @JsonProperty("passert_tollstasjon")
    private String passertTollstasjon;

    @JsonProperty("tollstasjon_passert")
    private int tollstasjon;
    

    public int getTollstasjon() {
        return tollstasjon;
    }

    public void setTollstasjon(int tollstasjon) {
        this.tollstasjon = tollstasjon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isKommersiellAktor() {
        return kommersiellAktor;
    }

    public void setKommersiellAktor(boolean kommersiellAktor) {
        this.kommersiellAktor = kommersiellAktor;
    }

    public String getRegistreringsnummer() {
        return registreringsnummer;
    }

    public void setRegistreringsnummer(String registreringsnummer) {
        this.registreringsnummer = registreringsnummer;
    }

    public String getSelskap() {
        return selskap;
    }

    public void setSelskap(String selskap) {
        this.selskap = selskap;
    }

    public String getPassertTollstasjon() {
        return passertTollstasjon;
    }

    public void setPassertTollstasjon(String passertTollstasjon) {
        this.passertTollstasjon = passertTollstasjon;
    }
}