SHELL=/bin/bash

################################################################################################################################
# Local
################################################################################################################################

# Local - build the container image (with Maven Spring plugins and cloud native buildpacks)
build-image:
	{ \
	set -e ;\
	source /Users/omocquais/.sdkman/bin/sdkman-init.sh ;\
	sdk use java 17.0.8-tem  ;\
	./mvnw clean spring-boot:build-image ;\
	}

# Local - call the actuator endpoint
actuator-local:
	{ \
	set -e ;\
	./scripts/check-actuator-endpoint.sh http://localhost:8080 ;\
	}

# Local - call the API to create customers
customers-local:
	{ \
	set -e ;\
	./scripts/populate-customers.sh  http://localhost:8080 ;\
	}

# Local - Start Redis + postgres  + Observability stack with Docker compose
start-app:
	{ \
	set -e ;\
	docker compose --profile observability up ;\
	}
