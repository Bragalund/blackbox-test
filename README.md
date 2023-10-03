# Black box test  

## Oppgave  

Vi skal lage større automatiserte tester for noen eksisterende applikasjoner for et annet lag. 

Noen rammebetingelser:  

1. Vi har flere applikasjoner i en microservice-arkitektur  
2. Vi benytter docker og kubernetes/openshift som kjøremiljø  
3. Vi benytter frontends i react  


Krav:

1. Vi ønsker at testene skal kjøre selvstendig, hver for seg.
2. At det skal være mulig å trigge testene fra en ci-cd pipeline på en enkel måte.  

Ønskelig:

1. Vi ønsker at du tenker på hvordan du ville automatisert testing av sikkerhet  



Vi ønsker at du:  
Tenker gjennom problemstilingen og kan forklare/lage en skisse over hvordan du vil løse oppgaven.   
* skriver *noe* kode for å løse en del av oppgaven, du velger språk og verktøy selv, så vi kan se litt på den tekniske tilnærmingen.



Endepunktene som skal testes er 

GET /health  
Skal returnere 200 OK


GET /lastebiler
Skal returnere: 200 OK

```shell
curl localhost:3500/api/lastebiler
```

```json

[
  {
    "id":1,"kommersiell_aktor":true,"registreringsnummer":"DP26272","selskap":"NOR Cargo","passert_tollstasjon":"2023-10-02T15:30:00+00:00"
    }, 
    {
      "id":2,"kommersiell_aktor":true,"registreringsnummer":"FD91232","selskap":"SØR Cargo","passert_tollstasjon":"2023-10-02T16:32:00+00:00"
    }, 
    {
      "id":3,"kommersiell_aktor":true,"registreringsnummer":"JL72739","selskap":"WEST Cargo","passert_tollstasjon":"2023-10-02T15:36:02+00:00"
    }
 ]

```

POST /lastebiler

```shell
curl -v --header "Content-Type: application/json" --request POST localhost:3500/api/lastebiler -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoid3JpdGVfdXNlciJ9.REHJAaYJrSeNgTTcf8yNmgPJYcv4xXanT-0X4ztOOxI"   --data '{"kommersiell_aktor":true,"registreringsnummer":"ZZ82739","selskap":"SOUTH Cargo","passert_tollstasjon":"2023-10-02T15:37:03+00:00"}' localhost:3500/api/lastebiler
```

Http-body:
```json
{
  "kommersiell_aktor":true,"registreringsnummer":"ZZ82739","selskap":"SOUTH Cargo","passert_tollstasjon":"2023-10-02T15:37:03+00:00"
}
```

Test at raden finnes i databasen.

## Teknisk info  

### Hvordan kjøre applikasjonene lokalt  

```shell
cd
make run
```

### Autentisering  

Dette tokenet autentiserer med "write_user", som har skriverettigheter med databasen:  

```token
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoid3JpdGVfdXNlciJ9.REHJAaYJrSeNgTTcf8yNmgPJYcv4xXanT-0X4ztOOxI
```

Dette tokenet autentiserer med "read_user", som har lese-rettigheter i databasen:

```token
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoicmVhZF91c2VyIn0.vpbs889wR-AEPp9CjooGRThxRDfDzof0c6nW2iXxEwM
```


### Bonus  

Lag en CI-CD pipeline som bygger applikasjonen og kjører testene du har skrevet i pipelinen.  
