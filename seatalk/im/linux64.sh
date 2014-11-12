mvn clean
mvn install
cd ./im-client
mvn assembly:assembly
cd ../../im-console
mvn assembly:assembly
cd ../../im-server
mvn assembly:assembly
cd ../../
ant -file ./build_linux64.xml run
chmod 777 * -R
