ROOT_DIR := $(shell pwd)

build-cmake:
	mkdir -p build
	cd build && cmake ..
	cd build && make -j4

tests: build-cmake
	cd build/tests && make test_debug

mosaic:
	mkdir -p integration-tests

	[ ! -f ./integration-tests/eclipse-mosaic-21.0.zip ] && wget -P integration-tests https://www.dcaiti.tu-berlin.de/research/simulation/download/get/eclipse-mosaic-21.0.zip || true
	[ ! -f ./integration-tests/scenario-convert-21.0.zip ] && wget -P integration-tests https://www.dcaiti.tu-berlin.de/research/simulation/download/get/scenario-convert-21.0.zip || true

	[ ! -d ./integration-tests/mosaic ] && unzip integration-tests/eclipse-mosaic-21.0.zip -d integration-tests/mosaic && chmod +x integration-tests/mosaic/mosaic.sh || true
	[ ! -f ./integration-tests/scenario-convert-21.0.jar ] && unzip integration-tests/scenario-convert-21.0.zip -d integration-tests || true

integration-test: build-cmake mosaic
	./build/scenarioGenerator --config=$(ROOT_DIR)/examples/configs/ernst-reuter-ci.json \
							  --osm=$(ROOT_DIR)/examples/scenarios/ernst-reuter.osm \
							  --scenario-convert=$(ROOT_DIR)/integration-tests/scenario-convert-21.0.jar

	cd integration-tests/mosaic && ./mosaic.sh -c $(ROOT_DIR)/examples/scenarios/ernst-reuter/scenario_config.json
