# Grensestasjoner API  

## API doc  


***Get /grensestasjoner***  
Returnerer alle grensestasjoner  
```shell
curl localhost:2345/grensestasjoner

# Returnerer
[{"id":1,"navn":"Svinesund","kommune":"Halden"},{"id":2,"navn":"Storskog","kommune":"Sør-Varanger"}]
```



***GET /grensesatsjoner/{grensestasjonId}***  
Returnerer en grensestasjon med alle grensepasseringer  
```shell
curl localhost:2345/grensestasjoner/2

# Returnerer
{"grensestasjon":{"id":2,"navn":"Storskog","kommune":"Sør-Varanger"},"lastebilPasseringer":[{"id":2,"kommersiell_aktor":true,"registreringsnummer":"FD91232","selskap":"SØR Cargo","passert_tollstasjon":"2023-10-02T16:32:00+00:00","tollstasjon_passert":2}]}
```

