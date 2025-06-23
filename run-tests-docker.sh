#!/bin/bash

echo "🚀 Starting Insider Test Automation Framework with Docker"
echo "=========================================================="

# Clean up previous containers
echo "🧹 Cleaning up previous containers..."
docker-compose down -v

# Build and start services
echo "🏗️  Building and starting services..."
docker-compose up --build -d selenium-hub chrome-node firefox-node edge-node allure-report

# Wait for services to be ready
echo "⏳ Waiting for Selenium Grid to be ready..."
sleep 30

# Check if hub is ready
echo "🔍 Checking Selenium Hub status..."
curl -sSL http://localhost:4444/wd/hub/status

# Run tests
echo "🧪 Running tests..."
docker-compose up --build automation-tests

# Generate Allure report
echo "📊 Allure Report available at: http://localhost:5050"
echo "📈 Allure Dashboard available at: http://localhost:5252"

echo "✅ Test execution completed!"
echo "Check the results at: http://localhost:5050" 