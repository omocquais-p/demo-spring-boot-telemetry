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

## Local - Start Redis + postgres with Docker compose
#start-backends:
#	{ \
#	set -e ;\
#	docker compose up ;\
#	}
#
## Local - Run the application
#start-app-maven:
#	{ \
#	set -e ;\
#	./mvnw spring-boot:run ;\
#	}
#
## Local - Run the application with docker compose support
#start-app-maven-compose:
#	{ \
#	set -e ;\
#	./mvnw spring-boot:run -P docker-compose ;\
#	}
#
#################################################################################################################################
## TAP - Kubernetes
#################################################################################################################################
#
## TAP - Install Testing supply chain + Gitops SSH secret + Create Claims (Postgres + Redis) + Deploy Workload
#install: patch
#	{ \
#	set -e ;\
#	./tap/tap-01-install-supply-chain.sh ;\
#	./tap/tap-02-install-deploy-workload.sh ;\
#	}
#
#fresh-install: kubeconfig install-supply-chain workload-create-claims
#	{ \
#	set -e ;\
#	}
#
## TAP - Install Testing supply chain + Gitops SSH secret
#install-supply-chain: kubeconfig patch
#	{ \
#	set -e ;\
#	./tap/tap-01-install-supply-chain.sh ;\
#	}
#
## TAP - Create Claims
#workload-create-claims: kubeconfig
#	{ \
#	set -e ;\
#	./tap/01-claims.sh ;\
#	}
#
## TAP - Deploy workload without observability
#workload-deploy: kubeconfig
#	{ \
#	set -e ;\
#	yq '(.spec.env[] | select(.name == "ENABLE_LOKI") | .value) = "false" | (.spec.env[] | select(.name == "MANAGEMENT_TRACING_ENABLED") | .value) = "false"' config/workload.yaml  | tanzu apps workload apply -f- --yes ;\
#	}
#
## TAP - Deploy workload with observability
#workload-deploy-observability: kubeconfig
#	{ \
#	set -e ;\
#	yq '(.spec.env[] | select(.name == "ENABLE_LOKI") | .value) = "true" | (.spec.env[] | select(.name == "MANAGEMENT_TRACING_ENABLED") | .value) = "true"' config/workload.yaml  | tanzu apps workload apply -f- --yes ;\
#	}
#
## TAP - Deploy workload with observability and a native image
#workload-deploy-observability-native: kubeconfig
#	{ \
#	set -e ;\
#	yq '(.spec.build.env[] | select(.name == "BP_NATIVE_IMAGE") | .value) = "true" | (.spec.build.env[] | select(.name == "BP_MAVEN_BUILD_ARGUMENTS") | .value) = "-Pnative -Dmaven.test.skip=true --no-transfer-progress package" | (.spec.env[] | select (.name == "ENABLE_LOKI").value=true | select (.name == "MANAGEMENT_TRACING_ENABLED")).value=true' config/workload.yaml  | tanzu apps workload apply -f- --yes ;\
#	}
#
## TAP - Deploy workload without observability and a native image
#workload-deploy-native: kubeconfig
#	{ \
#	set -e ;\
#	yq '(.spec.build.env[] | select(.name == "BP_NATIVE_IMAGE") | .value) = "true" | (.spec.build.env[] | select(.name == "BP_MAVEN_ACTIVE_PROFILES") | .value) = "native" | (.spec.env[] | select (.name == "ENABLE_LOKI").value=false | select (.name == "MANAGEMENT_TRACING_ENABLED")).value=false' config/workload.yaml  | tanzu apps workload apply -f- --yes ;\
#	}
#
#
## TAP - Undeploy demo-spring-boot workload
#workload-undeploy: kubeconfig
#	{ \
#	set -e ;\
#	tanzu apps workload delete demo-spring-boot --yes ;\
#	}
#
## TAP - Call the actuator endpoint to check the status of the application
#actuator: kubeconfig
#	{ \
#	set -e ;\
#	./tap/helpers/01-check-actuator-endpoint.sh ;\
#	}
#
## TAP - Call the API to create customers
#customers: kubeconfig
#	{ \
#	set -e ;\
#	./tap/helpers/03-populate-customers.sh ;\
#	}
#
## TAP - Patch the CPU and the memory to change the quotas
#patch: kubeconfig
#	{ \
#  	./tap/tap-sandbox/patch-sandbox.sh ;\
#	}
#
## TAP - deploy the demo-springboot workload on TAP
#deploy: kubeconfig
#	{ \
#	set -e ;\
#	./tap/tap-02-install-deploy-workload.sh ;\
#	}
#
## TAP - cleanup claims
#claims-delete: kubeconfig
#	{ \
#	set -e ;\
#	tanzu service class-claim delete postgres-1 --yes ;\
#	tanzu service class-claim delete redis-1 --yes ;\
#	}
#
## Kubernetes - Install the Observability Helm Charts (Grafana + Prometheus + Loki + Tempo)
#observability-install: kubeconfig
#	{ \
#	set -e ;\
#	./observability/install.sh ;\
#	}
#
## Kubernetes - Uninstall the Observability Helm Charts (Grafana + Prometheus + Loki + Tempo)
#observability-uninstall: kubeconfig
#	{ \
#	set -e ;\
#	./observability/uninstall.sh ;\
#	}
#
## Port-forward the demo-spring-boot Application
#forward-app:
#	{ \
#	set -e ;\
#	./observability/application/port-forward.sh ;\
#	}
#
## Port-forward Grafana
#grafana-forward: kubeconfig
#	{ \
#	set -e ;\
#	./observability/grafana/port-forward.sh  ;\
#	}
#
#workload-logs-build: kubeconfig
#	{ \
#	set -e ;\
#	tanzu apps workload tail demo-spring-boot --component build  ;\
#	}
#
#workload-logs-run: kubeconfig
#	{ \
#	set -e ;\
#	./tap/helpers/workload-logs.sh --since 3m ;\
#	}
#
#workload-logs-test: kubeconfig
#	{ \
#	set -e ;\
#	tanzu apps workload tail demo-spring-boot --component test  ;\
#	}
#
#print-tap-gui-url: kubeconfig
#	{ \
#	set -e ;\
#	./tap/helpers/print-tap-gui-url.sh  ;\
#	}