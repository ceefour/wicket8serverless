package com.hendyirawan.wicket8serverless

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

//@RunWith(SpringRunner::class)
//@SpringBootTest
class Wicket8serverlessApplicationTests {

	companion object {
	    val log = LoggerFactory.getLogger(Wicket8serverlessApplicationTests::class.java)
        var handler: LambdaHandler? = null

        @BeforeClass @JvmStatic
		fun setUp() {
			handler = LambdaHandler()
		}
	}

	@Test
	fun homeBuilder() {
		var context = MockLambdaContext()
		val req = AwsProxyRequestBuilder().method("GET").path("/").build()
		val resp = handler!!.handleRequest(req, context)
		log.info("Resp.status: {}", resp.statusCode)
        log.info("Resp.body: {}", resp.body)
		Assert.assertThat(resp.statusCode, Matchers.equalTo(200))
        Assert.assertThat(resp.body, Matchers.not(Matchers.isEmptyOrNullString()))
	}

    @Test
    fun privacyBuilder() {
        var context = MockLambdaContext()
        val req = AwsProxyRequestBuilder().method("GET").path("/privacy").build()
        val resp = handler!!.handleRequest(req, context)
        log.info("Resp.status: {}", resp.statusCode)
        log.info("Resp.body: {}", resp.body)
        Assert.assertThat(resp.statusCode, Matchers.equalTo(500))
        Assert.assertThat(resp.body, Matchers.not(Matchers.isEmptyOrNullString()))
    }

    @Test
    fun faviconBuilder() {
        var context = MockLambdaContext()
        val req = AwsProxyRequestBuilder().method("GET").path("/favicon.ico").build()
        val resp = handler!!.handleRequest(req, context)
        log.info("Resp.status: {}", resp.statusCode)
        log.info("Resp.body: {}", resp.body)
        Assert.assertThat(resp.statusCode, Matchers.equalTo(200))
        Assert.assertThat(resp.body, Matchers.not(Matchers.isEmptyOrNullString()))
    }

    @Test
	fun homeJson() {
		var context = MockLambdaContext()
		val om = ObjectMapper()
		val req = om.readValue("""{"path":"/","httpMethod":"GET","headers":null,"queryStringParameters":null,"pathParameters":null,"requestContext":{"path":"/","stage":"test-invoke-stage","requestId":"test-invoke-request","identity":{"sourceIp":"127.0.0.1"}}}""", AwsProxyRequest::class.java)
		val resp = handler!!.handleRequest(req, context)
		log.info("Resp.status: {}", resp.statusCode)
        log.info("Resp.body: {}", resp.body)
		Assert.assertThat(resp.statusCode, Matchers.equalTo(200))
        Assert.assertThat(resp.body, Matchers.not(Matchers.isEmptyOrNullString()))
	}

	@Test
	fun wrongUriJson() {
		var context = MockLambdaContext()
		val om = ObjectMapper()
		val req = om.readValue("""{"path":"/wicket8serverless","httpMethod":"GET","headers":null,"queryStringParameters":null,"pathParameters":null,"requestContext":{"path":"/wicket8serverless","stage":"test-invoke-stage","requestId":"test-invoke-request","identity":{"sourceIp":"127.0.0.1"}}}""",
				AwsProxyRequest::class.java)
		val resp = handler!!.handleRequest(req, context)
		log.info("Resp.status: {}", resp.statusCode)
        log.info("Resp.body: {}", resp.body)
		Assert.assertThat(resp.statusCode, Matchers.equalTo(404))
        Assert.assertThat(resp.body, Matchers.not(Matchers.isEmptyOrNullString()))
	}

    @Test
    fun wrongUri() {
        var context = MockLambdaContext()
        val req = AwsProxyRequestBuilder().method("GET").path("/wicket8serverless").build()
        val resp = handler!!.handleRequest(req, context)
        log.info("Resp.status: {}", resp.statusCode)
        log.info("Resp.body: {}", resp.body)
        Assert.assertThat(resp.statusCode, Matchers.equalTo(404))
        Assert.assertThat(resp.body, Matchers.not(Matchers.isEmptyOrNullString()))
    }

}
