#!/bin/bash

echo "Stopping all services..."

if [ -f /tmp/service-pids.txt ]; then
    while read pid; do
        if ps -p $pid > /dev/null 2>&1; then
            echo "Stopping process $pid..."
            kill $pid
        fi
    done < /tmp/service-pids.txt
    rm /tmp/service-pids.txt
    echo "All services stopped."
else
    echo "No PID file found. Services may not be running."
fi

# Clean up log files
rm -f /tmp/eureka.log /tmp/student.log /tmp/helprequest.log /tmp/recommendation.log /tmp/gateway.log
echo "Log files cleaned."
