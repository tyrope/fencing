echo "        STATUS: Building"
./gradlew build

echo "        STATUS: Moving release file"
mv -uf build/libs/* fencing-nightly.jar

echo "        STATUS: Cleanup"
rm -fr build
