sed 's/BUILDNO/'+$BUILD_NUMBER+'/' <build.template >build.gradle
./gradlew build

