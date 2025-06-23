#!/usr/bin/env pwsh

Write-Host "🚀 Starting Insider Test Automation Framework with Docker" -ForegroundColor Green
Write-Host "==========================================================" -ForegroundColor Green

# Clean up previous containers
Write-Host "🧹 Cleaning up previous containers..." -ForegroundColor Yellow
docker-compose down -v

# Build and start services
Write-Host "🏗️  Building and starting services..." -ForegroundColor Yellow
docker-compose up --build -d selenium-hub chrome-node firefox-node edge-node allure-report

# Wait for services to be ready
Write-Host "⏳ Waiting for Selenium Grid to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Check if hub is ready
Write-Host "🔍 Checking Selenium Hub status..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:4444/wd/hub/status" -UseBasicParsing
    Write-Host "✅ Selenium Hub is ready!" -ForegroundColor Green
} catch {
    Write-Host "❌ Selenium Hub not ready yet..." -ForegroundColor Red
}

# Run tests
Write-Host "🧪 Running tests..." -ForegroundColor Yellow
docker-compose up --build automation-tests

# Generate Allure report
Write-Host "📊 Allure Report available at: http://localhost:5050" -ForegroundColor Cyan
Write-Host "📈 Allure Dashboard available at: http://localhost:5252" -ForegroundColor Cyan

Write-Host "✅ Test execution completed!" -ForegroundColor Green
Write-Host "Check the results at: http://localhost:5050" -ForegroundColor Cyan 