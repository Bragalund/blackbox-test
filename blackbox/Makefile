DOCKER_COMPOSE := $(shell if command -v docker-compose >/dev/null 2>&1; then echo docker-compose; else echo docker compose; fi)
DOCKER_PLATFORM ?= linux/amd64

.PHONY: build-images start stop test

build-images:
	docker build --platform $(DOCKER_PLATFORM) -f postgres_lastebil_db_Dockerfile -t postgres_lastebil_db_local .
	docker build --platform $(DOCKER_PLATFORM) -f postgREST_Dockerfile -t postgrest_local .
	docker build --platform $(DOCKER_PLATFORM) -f nginx_apihardening_Dockerfile -t nginx_api_local .
	docker build --platform $(DOCKER_PLATFORM) -f nginx_frontend_Dockerfile -t frontend_nginx_local .
	docker build --platform $(DOCKER_PLATFORM) -f java_app_Dockerfile -t grensestasjoner_app_local .
	docker build --platform $(DOCKER_PLATFORM) -f postgres_grensestasjoner_db_Dockerfile -t postgres_grensestasjoner_db_local .
	docker build --platform $(DOCKER_PLATFORM) -f blackbox_tester_Dockerfile -t blackbox_tester_local .

start: build-images
	$(DOCKER_COMPOSE) up --remove-orphans -d

stop:
	$(DOCKER_COMPOSE) down

test: build-images
	@status=0; \
	$(DOCKER_COMPOSE) -f docker-compose.test.yml up --remove-orphans --abort-on-container-exit --exit-code-from blackbox_tester || status=$$?; \
	$(DOCKER_COMPOSE) -f docker-compose.test.yml down; \
	exit $$status
