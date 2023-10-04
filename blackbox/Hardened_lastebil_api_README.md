# Lastebil api  

Består av enkel reverse-proxy, et autogenerert rest-api med simpel autentisering på skriving til databasen.
Swagger-dokumentasjon kjører i egen container på localhost:8080.

## Autentisering  

Dette tokenet autentiserer med "write_user", som har skriverettigheter med databasen:  

```token
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoid3JpdGVfdXNlciJ9.REHJAaYJrSeNgTTcf8yNmgPJYcv4xXanT-0X4ztOOxI
```

Dette tokenet autentiserer med "read_user", som har lese-rettigheter i databasen:

```token
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoicmVhZF91c2VyIn0.vpbs889wR-AEPp9CjooGRThxRDfDzof0c6nW2iXxEwM
```

## Nyttige kommandoer

POST /lastebiler

```shell
curl -v --header "Content-Type: application/json" --request POST localhost:3500/api/lastebiler -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoid3JpdGVfdXNlciJ9.REHJAaYJrSeNgTTcf8yNmgPJYcv4xXanT-0X4ztOOxI"   --data '{"kommersiell_aktor":true,"registreringsnummer":"ZZ82739","selskap":"SOUTH Cargo","tollstasjon_passert":2, "passert_tollstasjon":"2023-10-02T15:37:03+00:00"}' localhost:3500/api/lastebiler
```
