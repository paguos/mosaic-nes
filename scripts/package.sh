[ -d "mosaic" ] && rm -rf mosaic || mkdir mosaic

echo "Downloading mosaic ..."
wget -P mosaic https://www.dcaiti.tu-berlin.de/research/simulation/download/get/eclipse-mosaic-21.0.zip
unzip mosaic/eclipse-mosaic-21.0.zip -d mosaic
chmod +x mosaic/mosaic.sh
echo "Downloading mosaic ... done!"

echo "Prepare scenario ..."
cp -r scenarios/barcelona mosaic/scenarios
cp -r scenarios/ernst-reuter mosaic/scenarios
cp app/rsu-source/target/rsu-source-0.0.1-jar-with-dependencies.jar mosaic/scenarios/ernst-reuter/application
cp app/vehicle-sensor/target/vehicle-sensor-0.0.1-jar-with-dependencies.jar mosaic/scenarios/ernst-reuter/application
cp app/vehicle-sink/target/vehicle-sink-0.0.1-jar-with-dependencies.jar mosaic/scenarios/ernst-reuter/application
echo "Prepare scenario ... done!"

echo "Prepare nes federate ..."
cp fed/nes-federate/target/nes-federate-0.0.1-jar-with-dependencies.jar mosaic/lib/mosaic
rm mosaic/etc/runtime.json
cp tests/integration-tests/src/test/resources/runtime.json mosaic/etc/runtime.json
echo "Prepare nes federate ... done!"
