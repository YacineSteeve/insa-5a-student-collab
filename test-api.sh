#!/bin/bash

# API Test Script for Student Collaboration Platform
# This script demonstrates all the main API endpoints

API_BASE="http://localhost:8080"
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}==================================="
echo "Student Collaboration Platform"
echo "API Test Script"
echo "===================================${NC}\n"

# Test 1: Register students
echo -e "${YELLOW}Test 1: Registering students...${NC}"
STUDENT1=$(curl -s -X POST "${API_BASE}/api/students/register" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Alice Martin",
    "email": "alice@insa.fr",
    "password": "password123",
    "etablissement": "INSA Lyon",
    "filiere": "Informatique",
    "competences": ["Java", "Spring Boot", "Python"],
    "disponibilites": ["Lundi 14h-16h", "Mercredi 10h-12h"]
  }')
echo "Student 1 registered: $STUDENT1"

STUDENT2=$(curl -s -X POST "${API_BASE}/api/students/register" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Bob Dupont",
    "email": "bob@insa.fr",
    "password": "password456",
    "etablissement": "INSA Toulouse",
    "filiere": "Génie Électrique",
    "competences": ["Électronique", "Python", "Arduino"],
    "disponibilites": ["Mardi 15h-17h"]
  }')
echo "Student 2 registered: $STUDENT2"

STUDENT3=$(curl -s -X POST "${API_BASE}/api/students/register" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Claire Rousseau",
    "email": "claire@insa.fr",
    "password": "password789",
    "etablissement": "INSA Lyon",
    "filiere": "Informatique",
    "competences": ["Java", "Spring Boot", "Docker", "Kubernetes"],
    "disponibilites": ["Jeudi 9h-11h", "Vendredi 14h-16h"]
  }')
echo "Student 3 registered: $STUDENT3"

sleep 2

# Test 2: Login
echo -e "\n${YELLOW}Test 2: Testing login...${NC}"
LOGIN_RESULT=$(curl -s -X POST "${API_BASE}/api/students/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@insa.fr",
    "password": "password123"
  }')
echo "Login result: $LOGIN_RESULT"

sleep 2

# Test 3: Get all students
echo -e "\n${YELLOW}Test 3: Getting all students...${NC}"
ALL_STUDENTS=$(curl -s -X GET "${API_BASE}/api/students")
echo "All students: $ALL_STUDENTS"

sleep 2

# Test 4: Create a help request
echo -e "\n${YELLOW}Test 4: Creating help request (student 2 asking for help)...${NC}"
HELP_REQUEST=$(curl -s -X POST "${API_BASE}/api/help-requests" \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 2,
    "titre": "Besoin d aide en Spring Boot",
    "description": "Je débute avec Spring Boot et j aurais besoin d aide pour comprendre les annotations et la configuration",
    "motsCles": ["Java", "Spring Boot", "Annotations"],
    "type": "DEMANDE_AIDE"
  }')
echo "Help request created: $HELP_REQUEST"

HELP_REQUEST_ID=$(echo $HELP_REQUEST | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
echo "Help Request ID: $HELP_REQUEST_ID"

sleep 2

# Test 5: Create an offer
echo -e "\n${YELLOW}Test 5: Creating help offer (student 1 offering help)...${NC}"
HELP_OFFER=$(curl -s -X POST "${API_BASE}/api/help-requests" \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "titre": "J offre mon aide en Python",
    "description": "Je peux aider pour des projets Python, machine learning, data science",
    "motsCles": ["Python", "Machine Learning", "Data Science"],
    "type": "OFFRE_AIDE"
  }')
echo "Help offer created: $HELP_OFFER"

sleep 2

# Test 6: Get all help requests
echo -e "\n${YELLOW}Test 6: Getting all help requests...${NC}"
ALL_REQUESTS=$(curl -s -X GET "${API_BASE}/api/help-requests")
echo "All help requests: $ALL_REQUESTS"

sleep 2

# Test 7: Get recommendations
echo -e "\n${YELLOW}Test 7: Getting recommendations for help request...${NC}"
if [ -n "$HELP_REQUEST_ID" ]; then
    RECOMMENDATIONS=$(curl -s -X GET "${API_BASE}/api/recommendations/help-request/${HELP_REQUEST_ID}")
    echo "Recommendations: $RECOMMENDATIONS"
else
    echo "No help request ID found, skipping recommendations test"
fi

sleep 2

# Test 8: Update help request status
echo -e "\n${YELLOW}Test 8: Updating help request status to EN_COURS...${NC}"
if [ -n "$HELP_REQUEST_ID" ]; then
    STATUS_UPDATE=$(curl -s -X PATCH "${API_BASE}/api/help-requests/${HELP_REQUEST_ID}/status?statut=EN_COURS")
    echo "Status updated: $STATUS_UPDATE"
fi

sleep 2

# Test 9: Search students by competence
echo -e "\n${YELLOW}Test 9: Searching students by competence 'Java'...${NC}"
JAVA_STUDENTS=$(curl -s -X GET "${API_BASE}/api/students/competence/Java")
echo "Students with Java competence: $JAVA_STUDENTS"

sleep 2

# Test 10: Get student by ID
echo -e "\n${YELLOW}Test 10: Getting student by ID (1)...${NC}"
STUDENT_BY_ID=$(curl -s -X GET "${API_BASE}/api/students/1")
echo "Student 1: $STUDENT_BY_ID"

echo -e "\n${GREEN}==================================="
echo "API Tests Completed!"
echo "===================================${NC}"
