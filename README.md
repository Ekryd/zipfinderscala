# ZipFinderScala #
This is a standalone tool that searches zip- or jarfiles for specific classes. The tool accepts a directory which it scans recursively for jarfiles and a string that it tries to match with the classes in the jarfiles.

The main use for this tool is to answer questions like: **"Which jarfile in the Spring framework contains the class URLContextResolver?"**

## Implementation ##
This project is a translation of the project ZipFinder, from Java to Scala.
The first revisions are direct translations without Scala functionality, and later
revisions will use all the extra features Scala has.

For the rest of the documentation see original project [ZipFinder](https://github.com/GreenThingSalad/zipfinder)
