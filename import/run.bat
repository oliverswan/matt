@echo off
echo Start....

setlocal enabledelayedexpansion

for %%i in (.\lib\*.jar) do  set CLASSPATH=!CLASSPATH!;%%i

java -classpath %CLASSPATH% -Dlogback.configurationFile=./config/logback.xml -Xmx1024M -Xms1024M -XX:+HeapDumpOnOutOfMemoryError  -XX:HeapDumpPath=./dump.txt -Xloggc:./gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:GCLogFileSize=1M  net.oliver.Importer
echo Finished....
@pause
