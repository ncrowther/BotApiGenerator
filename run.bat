@echo on

REM run java with params %1 %2


IF %1.==. GOTO No1
IF %2.==. GOTO No2

set PATH=%PATH%;C:\Program Files\Java\jdk-15.0.1\bin
set DATA_HOME=..\data

pushd D:\RPA\github\bpmnBotAPIGenerator\bin
C:\Program Files\Java\jdk-15.0.1\bin\java -cp ..\lib\jsoup-1.14.3.jar;. converter.bwl.BwlBpmnToWdgCompiler ..\data\%1 JA

GOTO End

:No1
  ECHO No param 1
GOTO End

:No2
  ECHO No param 2

:End

echo %result%
popd