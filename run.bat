@echo off

REM run java with params %1

IF %1.==. GOTO No1

set PATH=%PATH%;C:\Program Files\Java\jdk-15.0.1\bin

pushd D:\RPA\github\BotApiGenerator\bin
java -cp ..\lib\jsoup-1.14.3.jar;..\lib\javax.xml.bind.jar;..\lib\json-20210307.jar;. converter.bwl.BwlToApi %1

GOTO End

:No1
  ECHO No param 1

:End

echo %result%
popd