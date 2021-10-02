#!/bin/bash

# shellcheck disable=SC2015
[ -d "mosaic" ] && rm -rf mosaic || mkdir mosaic

echo "Downloading mosaic ..."
wget -P mosaic https://www.dcaiti.tu-berlin.de/research/simulation/download/get/eclipse-mosaic-21.0.zip
unzip mosaic/eclipse-mosaic-21.0.zip -d mosaic
chmod +x mosaic/mosaic.sh
echo "Downloading mosaic ... done!"

scenarios=( barcelona barcelona-moving-range barcelona-range-query berlin berlin-moving-range berlin-range-query leipzig leipzig-moving-range )
for scenario in "${scenarios[@]}"
do
	echo "Preparing $scenario scenario ..."
	cp -r scenarios/"$scenario" mosaic/scenarios
  cp app/nes-applications/target/nes-applications-0.0.1-jar-with-dependencies.jar mosaic/scenarios/"$scenario"/application

done

echo "Scenarios prepared!"

echo "Prepare nes federate ..."
cp fed/nes-federate/target/nes-federate-0.0.1-jar-with-dependencies.jar mosaic/lib/mosaic
rm mosaic/etc/runtime.json
cp tests/integration-tests/src/test/resources/runtime.json mosaic/etc/runtime.json
echo "Prepare nes federate ... done!"
