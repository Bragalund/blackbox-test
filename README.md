# Blackbox-test  

## Oppgave  

Vi skal lage større automatiserte tester for noen eksisterende applikasjoner for et annet lag. 
Prosjektet heter "blackbox" og er beskrevet mer i detalj i [blackbox/README.md](blackbox/README.md)

Noen rammebetingelser:  

1. Vi har flere applikasjoner i en microservice-arkitektur  
2. Vi benytter docker og kubernetes/openshift som kjøremiljø  
3. Vi benytter frontends i react og backends i java/kotlin


Krav:

1. Vi ønsker at hver og en av testene skal kjøre selvstendig, hver for seg.  
2. At det skal være mulig å trigge testene fra en ci-cd pipeline på en enkel måte.  
3. Oppsett og nedrivning av kjøremiljø for blackbox og testene skal være helautomatisert


Ønskelig:

1. Tenker på hvordan du ville automatisert testing av sikkerhet  
2. Tenker på hvordan du kan endre state/data i systemet for hver testkjøring, for å kunne trigge annen funksjonalitet


Vi ønsker at du:  

Tenker gjennom problemstilingen og kan forklare hvordan du vil løse oppgaven.
Bestemmer hvilke tester du ville skrevet, hvor du ville skrevet de og hvorfor. 
Vi forventer at du skriver    - ___*NOE*___ -    kode for å løse en del av oppgaven.
Du velger språk og verktøy selv, så vi kan se litt på den tekniske tilnærmingen.


## Forslag til tester å skrive   

Dette er et forslag til tester du kan lage. 
Hvis du ønsker å lage/tenke ut andre tester, så er du fri til å gjøre det.  

### grensestasjoner_api:

#### Sjekke helse  

GET /health  
Skal returnere Http Statuskode "200"

### hardened_lastebil_api:

#### Liste med grensepasseringer  

GET /lastebiler

```shell
curl localhost:3500/api/lastebiler
```

Skal returnere: Den samme listen med grensepasseringer som finnes i databasen.


#### Manipuler grensepasseringsdata, sjekk at på nettsiden

POST /lastebiler
Nøyere beskrevet i [Nyttige kommandoer -> blackbox/Hardened_lastebil_api_README.md](blackbox/Hardened_lastebil_api_README.md)  

Assert: Test at dataene som ble lagt inn via API'et dukker opp på nettsiden når knappen på nettsiden trykkes på.  


### Sammensatte tester  

#### Manipuler grensepasseringsdata, sjekk at blir returnert fra grensestasjons_api  

Hardened_lastebiler_api: POST /lastebiler  
Grensestasjoner_api: GET /grensestasjoner/2  

Assert: At den opprettede grensepasseringen eksisterer når man spør om detaljert informasjon på grensestasjon nummer 2.  


### Bonus  

Lag en CI-CD pipeline som bygger applikasjonene og kjører testene du har skrevet i pipelinen.  
