# Wicket 8 Serverless

## Create AWS Lambda Deployment Package

    mvn -Paws-serverless -DskipTests package

## Lambda Function Configuration

Name: wicket8serverless

Triggers: None

Runtime: Java 8

Handler: com.hendyirawan.wicket8serverless.LambdaHandler

Role: Create new Role from Template

Role name: wicket8serverless

Policy template: Simple Microservices permissions

Memory: 512 MB (default)

Timeout: 30 sec (!!!)

Environment:

    SPRING_PROFILES_ACTIVE=production

## API Gateway Configuration

    API name: wicket8serverless
    
    Deployment stage: prod
    
    Security: AWS IAM
    
    Proxy resource: true
    
    Resource name: proxy
    
    Resource path: {proxy+}

## Map Resource to Lambda function

Select Integration Request

Integration Type: Lambda Function

Use Lambda Proxy Integration: true

Select region and Lambda function

Actions -> Deploy API    
