echo "preparing build file."
sed 's/BUILDNO/'+$BUILD_NUMBER+'/' <build.template >build.gradle

echo "Building"
./gradlew build

echo "cleanup"
rm -fr build

