#!/bin/bash

echo "==================================="
echo "Student Collaboration Platform"
echo "Starting all microservices..."
echo "==================================="

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "\n${YELLOW}Step 1: Building all services...${NC}"
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Build successful${NC}"
else
    echo "Build failed. Exiting..."
    exit 1
fi

echo -e "\n${YELLOW}Step 2: Starting services in order...${NC}"
echo -e "\n${GREEN}Starting Eureka Server (Port 8761)...${NC}"
cd eureka-server
mvn spring-boot:run > /tmp/eureka.log 2>&1 &
EUREKA_PID=$!
echo "Eureka PID: $EUREKA_PID"
cd ..

echo "Waiting 30 seconds for Eureka to start..."
sleep 30

echo -e "\n${GREEN}Starting Student Service (Port 8081)...${NC}"
cd student-service
mvn spring-boot:run > /tmp/student.log 2>&1 &
STUDENT_PID=$!
echo "Student Service PID: $STUDENT_PID"
cd ..

echo -e "\n${GREEN}Starting Help Request Service (Port 8082)...${NC}"
cd help-request-service
mvn spring-boot:run > /tmp/helprequest.log 2>&1 &
HELPREQUEST_PID=$!
echo "Help Request Service PID: $HELPREQUEST_PID"
cd ..

echo -e "\n${GREEN}Starting Recommendation Service (Port 8083)...${NC}"
cd recommendation-service
mvn spring-boot:run > /tmp/recommendation.log 2>&1 &
RECOMMENDATION_PID=$!
echo "Recommendation Service PID: $RECOMMENDATION_PID"
cd ..

echo "Waiting 20 seconds for services to register with Eureka..."
sleep 20

echo -e "\n${GREEN}Starting API Gateway (Port 8080)...${NC}"
cd api-gateway
mvn spring-boot:run > /tmp/gateway.log 2>&1 &
GATEWAY_PID=$!
echo "API Gateway PID: $GATEWAY_PID"
cd ..

echo -e "\n${YELLOW}Waiting 15 seconds for API Gateway to start...${NC}"
sleep 15

echo -e "\n${GREEN}==================================="
echo "All services started successfully!"
echo "===================================${NC}"
echo ""
echo "Service URLs:"
echo "  - Eureka Dashboard: http://localhost:8761"
echo "  - API Gateway: http://localhost:8080"
echo "  - Student Service: http://localhost:8081 (direct)"
echo "  - Help Request Service: http://localhost:8082 (direct)"
echo "  - Recommendation Service: http://localhost:8083 (direct)"
echo ""
echo "Log files:"
echo "  - Eureka: /tmp/eureka.log"
echo "  - Student Service: /tmp/student.log"
echo "  - Help Request Service: /tmp/helprequest.log"
echo "  - Recommendation Service: /tmp/recommendation.log"
echo "  - API Gateway: /tmp/gateway.log"
echo ""
echo "PIDs saved to /tmp/service-pids.txt"
echo "$EUREKA_PID" > /tmp/service-pids.txt
echo "$STUDENT_PID" >> /tmp/service-pids.txt
echo "$HELPREQUEST_PID" >> /tmp/service-pids.txt
echo "$RECOMMENDATION_PID" >> /tmp/service-pids.txt
echo "$GATEWAY_PID" >> /tmp/service-pids.txt
echo ""
echo "To stop all services, run: ./stop-services.sh"
