[ -d "mosaic" ] && rm -rf mosaic || mkdir mosaic

echo "Downloading mosaic ..."
wget -P mosaic https://www.dcaiti.tu-berlin.de/research/simulation/download/get/eclipse-mosaic-21.0.zip
unzip mosaic/eclipse-mosaic-21.0.zip -d mosaic
chmod +x mosaic/mosaic.sh
echo "Downloading mosaic ... done!"

echo "Prepare scenarios ..."
cp -r scenarios/barcelona mosaic/scenarios
cp app/nes-applications/target/nes-applications-0.0.1-jar-with-dependencies.jar mosaic/scenarios/barcelona/application

cp -r scenarios/berlin mosaic/scenarios
cp app/nes-applications/target/nes-applications-0.0.1-jar-with-dependencies.jar mosaic/scenarios/berlin/application

cp -r scenarios/berlin-moving-range mosaic/scenarios
cp app/nes-applications/target/nes-applications-0.0.1-jar-with-dependencies.jar mosaic/scenarios/berlin-moving-range/application

cp -r scenarios/berlin-range-query mosaic/scenarios
cp app/nes-applications/target/nes-applications-0.0.1-jar-with-dependencies.jar mosaic/scenarios/berlin-range-query/application
echo "Prepare scenarios ... done!"

echo "Prepare nes federate ..."
cp fed/nes-federate/target/nes-federate-0.0.1-jar-with-dependencies.jar mosaic/lib/mosaic
rm mosaic/etc/runtime.json
cp tests/integration-tests/src/test/resources/runtime.json mosaic/etc/runtime.json
echo "Prepare nes federate ... done!"
