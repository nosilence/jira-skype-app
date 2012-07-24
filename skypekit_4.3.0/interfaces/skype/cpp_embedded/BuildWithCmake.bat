echo "preparing makefiles for Cpp build:"
rd /s /q cmake-build
md cmake-build
cd cmake-build
REM generating build scripts using default generator
REM other generators avilable using the -G option:
REM see `cmake --help` for details
cmake ..
REM assuming the default generator was Visual Studio, now build the INSTALL target
echo "building Cpp libraries and client:"
msbuild INSTALL.vcxproj
cd ..
echo "Cpp libraries and test client built in the build directory:"
dir build