
create schema api;

create table api.lastebiler (
  id serial primary key,
  kommersiell_aktor boolean not null default false,
  registreringsnummer text not null,
  selskap text not null,
  tollstasjon_passert integer not null,
  passert_tollstasjon timestamptz
);

insert into api.lastebiler (kommersiell_aktor, registreringsnummer, selskap, tollstasjon_passert, passert_tollstasjon) 
values (true, 'DP26272', 'NOR Cargo', 1, '2023-10-02 15:30:00+00:00');

insert into api.lastebiler (kommersiell_aktor, registreringsnummer, selskap, tollstasjon_passert, passert_tollstasjon) 
values (true, 'FD91232', 'SØR Cargo', 2, '2023-10-02 16:32:00+00:00');

insert into api.lastebiler (kommersiell_aktor, registreringsnummer, selskap, tollstasjon_passert, passert_tollstasjon) 
values (true, 'JL72739', 'WEST Cargo', 1, '2023-10-02 15:36:02+00:00');

insert into api.lastebiler (kommersiell_aktor, registreringsnummer, selskap, tollstasjon_passert, passert_tollstasjon) 
values (true, 'JK72759', 'WEST Cargo', 1, '2023-10-02 11:12:01+00:00');

create role read_user nologin;

grant usage on schema api to read_user;
grant select on api.lastebiler to read_user;

create role authenticator noinherit login password 'mysecretpassword';
grant read_user to authenticator;

create role write_user nologin;
grant write_user to authenticator;

grant usage on schema api to write_user;
grant all on api.lastebiler to write_user;
grant usage, select on sequence api.lastebiler_id_seq to write_user;
