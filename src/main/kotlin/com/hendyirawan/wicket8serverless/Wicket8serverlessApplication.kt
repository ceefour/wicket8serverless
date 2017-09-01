package com.hendyirawan.wicket8serverless

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.support.SpringBootServletInitializer

@SpringBootApplication
/**
 * FIXME: Requires explicit PropertySource here
 * FIXME: PropertySource("classpath:/application.yml") doesn't work
 */
//@PropertySource("classpath:/application.properties")
class Wicket8serverlessApplication : SpringBootServletInitializer() {

//    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
//        return builder.listeners(LambdaFlushResponseListener())
//    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Wicket8serverlessApplication::class.java, *args)
}
