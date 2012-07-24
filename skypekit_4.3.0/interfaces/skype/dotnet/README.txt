
SkypeKit .NET Wrapper 1.0 (alpha)

1. Convert your pem file into a pfx file. More info on this in the Reference Manual.

2. To build the wrapper library, use project files in the build (VS2010 or VS2008) directory.

3. To build the tutorials, use project file in examples\tutorials\build\VS2010 (or VS2008).

4. To run tutorials, you will need to modify some values in examples\tutorials\common\tutorials_common.cs
    path            - path to both runtime and the keyfile (the .pfx file you converted from pem)
    keyfilename     - name of the pfx file.
    keypassword     - keyfile password, if you made it password-protected during conversion

5. This version of the wrapper works with SDK 4.X.X public release runtimes (not tested with others).

6. For quick access to the reference manual, use reference_manual.html

NB! This is alpha-release. In all likeliehood, there are still plenty of bugs lurking around.
