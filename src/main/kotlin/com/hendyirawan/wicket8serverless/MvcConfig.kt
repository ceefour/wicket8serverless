package com.hendyirawan.wicket8serverless

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import javax.annotation.PostConstruct

//@EnableWebMvc
//@Configuration
class MvcConfig {
    companion object {
        private val log = LoggerFactory.getLogger(MvcConfig::class.java)
    }

    @PostConstruct
    fun init() {
        log.info("MvcConfig initialized")
    }

}