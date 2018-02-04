@ECHO OFF

java -cp C:\Users\cdimaio\Dropbox\MyProjects\DataStuff\VersionControl\classes; VersionControl C:\Users\cdimaio\Dropbox\MyProjects\DataStuff\FileSorter java

javac -d classes\  src\*.java

java -cp classes\; FileSorter %1 %2 %3 %4 %5 %6 %7 %8 %9

ECHO ON