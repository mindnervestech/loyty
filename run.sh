#!/bin/bash
git pull
java -Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -jar ../../opt/play-2.1.3/framework/sbt/sbt-launch.jar -Dsbt.log.noformat=true dist
cd dist
unzip loyaltymgmt-1.0-SNAPSHOT.zip
rsync -avzh loyaltymgmt-1.0-SNAPSHOT/ ../../loyaltymgmt-1.0-SNAPSHOT/
cd ../../loyaltymgmt-1.0-SNAPSHOT/
chmod -c 777 start
rm RUNNING_PID
