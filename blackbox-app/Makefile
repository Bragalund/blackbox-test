run: 
	docker build -f postgres_lastebil_db_Dockerfile -t postgres_lastebil_db_local .;
	docker build -f postgREST_Dockerfile -t postgrest_local .;
	docker build -f nginx_apihardening_Dockerfile -t nginx_api_local .;
	docker build -f nginx_frontend_Dockerfile -t frontend_nginx_local .;
	docker build -f java_app_Dockerfile -t grensestasjoner_app_local .;
	docker build -f postgres_grensestasjoner_db_Dockerfile -t postgres_grensestasjoner_db_local .;
	docker-compose up --remove-orphans -d;

stop:
	docker-compose down;
	