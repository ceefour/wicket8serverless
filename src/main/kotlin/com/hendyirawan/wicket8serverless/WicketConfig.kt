package com.hendyirawan.wicket8serverless

import org.apache.wicket.protocol.http.WicketFilter
import org.apache.wicket.protocol.http.servlet.AbstractRequestWrapperFactory
import org.apache.wicket.protocol.http.servlet.SecuredRemoteAddressRequestWrapperFactory
import org.apache.wicket.protocol.http.servlet.XForwardedRequestWrapperFactory
import org.apache.wicket.spring.SpringWebApplicationFactory
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.io.IOException
import java.util.regex.Pattern
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by ceefour on 27/12/14.
 */
@Configuration
class WicketConfig {

    @Autowired
    protected var env: Environment? = null

//    @Bean
//    fun securityFilterChain(@Autowired(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME) securityFilter: Filter): FilterRegistrationBean {
//        val registration = FilterRegistrationBean(securityFilter)
//        registration.order = 1
//        registration.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
//        return registration
//    }

    @Bean
    fun wicketFilter(): FilterRegistrationBean {
        val reg = FilterRegistrationBean(WicketFilter())
        reg.addInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*")
        reg.addInitParameter(WicketFilter.APP_FACT_PARAM, SpringWebApplicationFactory::class.java.getName())
        reg.addInitParameter("applicationBean", "webApp")
        reg.addInitParameter("configuration", env!!.getRequiredProperty("wicket.configuration"))
        //        reg.addInitParameter("applicationClassName", MyWebApplication.class.name);
        reg.order = Integer.MAX_VALUE // make sure it is after Spring Security filter
        return reg
    }

    private val securedRemoteAddresses = arrayOf(Pattern.compile("10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"), Pattern.compile("192\\.168\\.\\d{1,3}\\.\\d{1,3}"), Pattern.compile("172\\.(?:1[6-9]|2\\d|3[0-1]).\\d{1,3}.\\d{1,3}"), Pattern.compile("169\\.254\\.\\d{1,3}\\.\\d{1,3}"), Pattern.compile("127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))

    /**
     * See:
     * https://issues.apache.org/jira/browse/WICKET-3015
     * https://issues.apache.org/jira/browse/WICKET-3009
     * @return
     */
    @Bean
    fun xForwardedFilter(): FilterRegistrationBean {
        // SecuredRemoteAddressRequestWrapper won't be needed if nginx or Load Balancer can terminate SSL
        // FIXME: https://issues.apache.org/jira/browse/WICKET-6440
        val addressRequestWrapperFactory = object : SecuredRemoteAddressRequestWrapperFactory() {
            override fun getWrapper(request: HttpServletRequest): HttpServletRequest {
                var xRequest = request
                if (isEnabled && needsWrapper(request)) {
                    xRequest = newRequestWrapper(request)
                }

                val log = LoggerFactory.getLogger(SecuredRemoteAddressRequestWrapperFactory::class.java)
                if (log.isDebugEnabled) {
                    log.debug("Incoming request uri=" + request.requestURI + " with originalSecure='" +
                            request.isSecure + "', remoteAddr='" + request.remoteAddr +
                            "' will be seen with newSecure='" + xRequest.isSecure + "'")
                }

                return xRequest
            }

            override fun needsWrapper(request: HttpServletRequest): Boolean {
                return !request.isSecure && AbstractRequestWrapperFactory.matchesOne(request.remoteAddr, *securedRemoteAddresses)
            }
        }
        val requestWrapperFactory = XForwardedRequestWrapperFactory()
        requestWrapperFactory.config.setProtocolHeader("X-Forwarded-Proto")
        val filterRegistrationBean = FilterRegistrationBean(object : Filter {
            @Throws(ServletException::class)
            override fun init(filterConfig: FilterConfig) {
                requestWrapperFactory.init(filterConfig)
                addressRequestWrapperFactory.init(filterConfig)
            }

            @Throws(IOException::class, ServletException::class)
            override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
                val xRequest: ServletRequest
                if (request is HttpServletRequest) {
                    xRequest = requestWrapperFactory.getWrapper(
                            addressRequestWrapperFactory.getWrapper(request))
                } else {
                    xRequest = request
                }
                chain.doFilter(xRequest, response)
            }

            override fun destroy() {}
        })
        filterRegistrationBean.setName("xForwardedFilter")
        // MUST be before Spring Security!
        filterRegistrationBean.order = 0
        return filterRegistrationBean
    }

//    @Bean
//    fun mdcInsertingServletFilter(): FilterRegistrationBean {
//        val reg = FilterRegistrationBean(MDCInsertingServletFilter())
//        reg.order = 1
//        return reg
//    }

    //    @Bean
    //    public FilterRegistrationBean moreMdcServletFilter() {
    //        final FilterRegistrationBean reg = new FilterRegistrationBean(new MoreMdcServletFilter());
    //        reg.setOrder(1);
    //        return reg;
    //    }
    //
    //    @Bean
    //    public FilterRegistrationBean springSecurityMdcServletFilter() {
    //        final FilterRegistrationBean reg = new FilterRegistrationBean(new SpringSecurityMdcServletFilter());
    //        reg.setOrder(2);
    //        return reg;
    //    }

}
