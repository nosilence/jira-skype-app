#!/bin/bash 
echo "preparing makefiles for Cpp build:"
mkdir cmake-build
cd cmake-build
# generating unix makefiles by default
# other generators avilable using the -G option:
# see `cmake --help` for details
echo -e "\n******************* CMAKE stderr *******************" > warnings.txt
cmake .. 2> >( tee -a warnings.txt )
echo "building Cpp libraries and client:"
echo "******************* GMAKE stderr *******************" >> warnings.txt
make install 2> >( tee -a warnings.txt ) 
cd ..
echo "Cpp libraries and test client built in the build directory:"
ls -al build

cat cmake-build/warnings.txt
