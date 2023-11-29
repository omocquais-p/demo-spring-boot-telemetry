# Spring Boot Application with observability

- Build the container image from source code with cloud native buildpacks
```
make build-image
```

- Start the application with docker compose
```
make start-app
```

- Call the application to create customers in Redis
```
make customers-local
```

## Metrics in Prometheus / Grafana
- Check Metrics ( _creating_time_milliseconds_count_ or _creating_time_milliseconds_sum_ in Prometheus: http://localhost:9090/ (Metrics Explorer) and in Grafana (http://localhost:3000/)