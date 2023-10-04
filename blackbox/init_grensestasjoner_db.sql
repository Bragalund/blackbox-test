create schema api;

create table api.grensestasjoner (
  id serial primary key,
  navn text not null,
  kommune text not null
);

insert into api.grensestasjoner (id, navn, kommune) 
values (1, 'Svinesund', 'Halden');

insert into api.grensestasjoner (id, navn, kommune) 
values (2, 'Storskog', 'Sør-Varanger');

create role everything_user nologin;

grant usage on schema api to everything_user;
grant all on api.grensestasjoner to everything_user;