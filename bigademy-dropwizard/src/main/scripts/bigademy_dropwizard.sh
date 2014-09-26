#!/bin/sh
SERVICE_NAME=BigademyDropwizard
PATH_TO_JAR=../lib/bigademy-dropwizard.jar
PATH_TO_YAML_FILE=../config/bigademy-config.yaml
PID_PATH_NAME=/tmp/BigademyDropwizard-pid
CLASSPATH=../lib
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            nohup java -jar $PATH_TO_JAR server $PATH_TO_YAML_FILE > startup-log.out &
                        echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stoping ..."
            kill $PID;
            echo "$SERVICE_NAME stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    migrate)
        echo "Migrating database ..."
        nohup java -jar $PATH_TO_JAR db migrate $PATH_TO_YAML_FILE > database-log.out &
        echo "Database migrated."
    ;;
esac
