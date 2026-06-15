Swagger OpenApi docs
http://localhost:8081/swagger-ui/index.html

Steps to run this journal application in local.

1. Make sure your MongoDB database is accessible and your current ip address is whitelisted.
2. Run the below commands to start the kafka service in your local.

Go to /Softwares/kafka/bin/win and run below cmds in separate windows

Start zookeeper -
zookeeper-server-start.bat ..\..\config\zookeeper.properties

Start below three kafka servers -
kafka-server-start.bat ..\..\config\server.properties

kafka-server-start.bat ..\..\config\server-1.properties

kafka-server-start.bat ..\..\config\server-2.properties
