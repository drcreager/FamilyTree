echo off
rem
rem Script:  process.bat
rem Desc:    process loader for Family Tree Microservices
rem Author:  Daniel R Creager
rem Written: Sept 2015
rem
set BASE="C:\Users\Z8364A\Documents\My Projects\GeneologyWS\FamilyTree2"
set CLASSPATH=.\build\libs\*;.\lib\*

pushd %BASE%
START /B java -classpath %CLASSPATH% com.ko.na.microservices.%1% >> %1%.log 
rem java -classpath %CLASSPATH% com.ko.na.microservices.%1%
popd