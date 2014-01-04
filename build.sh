echo "        STATUS: Preparing build file"
sed 's/BUILDNO/'+$BUILD_NUMBER+'/' <build.template >build.gradle

echo "        STATUS: Building"
./gradlew build

echo "        STATUS: Moving release file"
mv -uf build/libs/* .

echo "        STATUS: Cleanup"
rm -fr build
