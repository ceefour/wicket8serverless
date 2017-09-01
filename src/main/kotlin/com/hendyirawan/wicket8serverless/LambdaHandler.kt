package com.hendyirawan.wicket8serverless

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import org.slf4j.LoggerFactory
import org.springframework.boot.web.support.SpringBootServletInitializer


class LambdaHandler : RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    companion object {
        val log = LoggerFactory.getLogger(LambdaHandler::class.java)
    }

    private var handler: SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse>? = null

    override fun handleRequest(awsProxyRequest: AwsProxyRequest, context: Context): AwsProxyResponse {
//        val app = SpringApplication(Wicket8serverlessApplication::class.java)
//        ///app.webApplicationType = WebApplicationType.NONE
//        app.isWebEnvironment = false
//        app.setBannerMode(Banner.Mode.OFF)
//
//        // create a new empty context and set the spring boot application as a parent of it
//        log.info("create a new empty context and set the spring boot application as a parent of it")
//        val wrappingContext = AnnotationConfigWebApplicationContext()
//        app.addInitializers()
//        log.info("app.run")
//        wrappingContext.parent = app.run()
//
//        // now we can initialize the framework with the wrapping context
//        log.info("now we can initialize the framework with the wrapping context")
//        val handler = SpringLambdaContainerHandler.getAwsProxyHandler(wrappingContext)
//        return handler.proxy(awsProxyRequest, context)
//        val handler = SpringLambdaContainerHandler.getAwsProxyHandler(Wicket8serverlessApplication::class.java)
//        return handler.proxy(awsProxyRequest, context)

        if (null == handler) {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(Wicket8serverlessApplication::class.java)
        }
        return handler!!.proxy(awsProxyRequest, context)

    }
}