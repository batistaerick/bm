COMPOSE ?= docker compose

.PHONY: help infra-up app-up up down infra-down app-down logs app-logs infra-logs

help:
	@printf '%s\n' 'Available commands:'
	@printf '  %-14s %s\n' 'make infra-up' 'Start database services'
	@printf '  %-14s %s\n' 'make app-up' 'Build and start the app service'
	@printf '  %-14s %s\n' 'make up' 'Build and start app plus infrastructure'
	@printf '  %-14s %s\n' 'make down' 'Stop and remove all compose services'
	@printf '  %-14s %s\n' 'make infra-down' 'Stop database services'
	@printf '  %-14s %s\n' 'make app-down' 'Stop the app service'
	@printf '  %-14s %s\n' 'make logs' 'Follow all service logs'
	@printf '  %-14s %s\n' 'make app-logs' 'Follow app logs'
	@printf '  %-14s %s\n' 'make infra-logs' 'Follow database logs'

infra-up:
	$(COMPOSE) up -d postgres

app-up:
	$(COMPOSE) up -d --build --no-deps app

up:
	$(COMPOSE) up -d --build

down:
	$(COMPOSE) down

infra-down:
	$(COMPOSE) stop postgres

app-down:
	$(COMPOSE) stop app

logs:
	$(COMPOSE) logs -f

app-logs:
	$(COMPOSE) logs -f app

infra-logs:
	$(COMPOSE) logs -f postgres
