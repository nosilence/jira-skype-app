create a cmakebuild folder or whatever you name it, go to that folder and run the following command
"cmake .."

cmake currently takes three parameters:
APPS_TO_BUILD
OUTPUT_DIR
ARCH


For APPS_TO_BUILD:
By default, it will generate makefiles for all available application that can be invoked by cmakescript,
or you can specify with the particular application you want to build
e.g. if you want to build cpp and java wrappers, you can type in the command
"cmake -DAPPS_TO_BUILD=wrappers .."

if you only want javawrapper or cppwrapper, go with 
"cmake -DAPPS_TO_BUILD=javawrapper .." or "cmake -DAPPS_TO_BUILD=cppwrapper .."

if you only want uikit, you can type:
"cmake -DAPPS_TO_BUILD=uikit .."


For OUTPUT_DIR and ARCH
By default, ARCH is empty, OUTPUT_DIR is defined as CMAKE_CURRENT_SOURCE_DIR, it will install all the binaries into CMAKE_CURRENT_SOURCE_DIR/bin, and install all libraries into CMAKE_CURRENT_SOURCE_DIR/lib

if OUTPUT_DIR (needs to be absolute path if specified) and ARCH are specified by user, it will install the platform specified binaries into OUTPUT_DIR/bin/ARCH, and non platform specified binaries into OUTPUT_DIR/bin 
and it will install all the libraries into OUTPUT_DIR/lib
An example command line can be:
"cmake -DAPPS_TO_BUILD=wrappers -DARCH=windows -DOUTPUT_DIR=Z:\Cygwin\home\user\src\cbuild\output .."