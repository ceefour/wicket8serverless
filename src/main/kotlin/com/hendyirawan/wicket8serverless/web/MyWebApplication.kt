package com.hendyirawan.wicket8serverless.web

import de.agilecoders.wicket.core.Bootstrap
import de.agilecoders.wicket.core.settings.BootstrapSettings
import org.apache.commons.lang3.StringUtils
import org.apache.wicket.Page
import org.apache.wicket.RuntimeConfigurationType
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.Url
import org.apache.wicket.request.resource.UrlResourceReference
import org.apache.wicket.resource.JQueryResourceReference
import org.apache.wicket.spring.injection.annot.SpringComponentInjector
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import javax.inject.Inject

/**
 * Created by ceefour on 27/12/14.
 */
@Component("webApp")
class MyWebApplication : WebApplication() {

    //    @Inject
    //    private AdminNotifier adminNotifier;
    @Inject
    private val env: Environment? = null

    override fun getHomePage(): Class<out Page> {
        return HomePage::class.java
    }

    private fun mountPages() {
        mountPage("privacy", PrivacyPage::class.java)
    }

    override fun init() {
        super.init()

        //        // Wicket data store:
        //        // http://apache-wicket.1842946.n4.nabble.com/Is-there-a-way-to-substitute-the-application-scoped-cache-td4676073.html
        //        // https://stackoverflow.com/a/36693035/122441
        //        if (RuntimeConfigurationType.DEPLOYMENT == getConfigurationType()) {
        //            log.info("Switching DataStore to HttpSessionDataStore to make Wicket data clusterable using session");
        //            setPageManagerProvider(new DefaultPageManagerProvider(this) {
        //                @Override
        //                protected IDataStore newDataStore() {
        //                    // 360 KB per session should be enough, if not then sometimes is wrong with the page's models
        //                    // Amazon DynamoDB limits to 400 KB
        //                    // http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Limits.html#limits-items
        //                    // Now that we're using PostgreSQL, we can have more in session, yay!
        //                    // 2 MB/session = max 2 GB for 1000 users
        //                    return new HttpSessionDataStore(getPageManagerContext(),
        //                            new MemorySizeEvictionStrategy(Bytes.kilobytes(2000)));
        //                }
        //            });
        //        } else {
        //            DebugDiskDataStore.register(this);
        //        }
        //        // disable application cache, to make clustering-ready
        //        getStoreSettings().setInmemoryCacheSize(0);
        //
        //        // Exception settings
        ////        getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_EXCEPTION_PAGE);
        ////        getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
        ////        getApplicationSettings().setInternalErrorPage(WicketErrorPage.class);
        //        getRequestCycleListeners().add(new AbstractRequestCycleListener() {
        //            @Override
        //            public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler) {
        //                if (handler instanceof IPageClassRequestHandler) {
        //                    MDC.put("wicket.page", ((IPageClassRequestHandler) handler).getPageClass().getSimpleName());
        //                }
        //            }
        //
        //            @Override
        //            public void onEndRequest(RequestCycle cycle) {
        //                MDC.remove("wicket.page");
        //            }
        //
        //        });

        componentInstantiationListeners.add(SpringComponentInjector(this))
        //        getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));
        //        getComponentPostOnBeforeRenderListeners().add(new StatelessChecker());
        //
        val bootstrapSettings = BootstrapSettings()
                .useCdnResources(getConfigurationType() == RuntimeConfigurationType.DEPLOYMENT)
        //                .setThemeProvider(new SingleThemeProvider(BootswatchTheme.Paper));
        Bootstrap.install(this, bootstrapSettings);
        // FIXME: https://issues.apache.org/jira/browse/WICKET-5610?focusedCommentId=16094486&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-16094486
        // Use CDN jQuery if we're in production
        if (bootstrapSettings.useCdnResources()) {
            val version = StringUtils.removeEnd(StringUtils.removeStart(JQueryResourceReference.VERSION_1, "jquery/jquery-"),
                ".js")
            getJavaScriptLibrarySettings().setJQueryReference(UrlResourceReference(
                    Url.parse("//ajax.googleapis.com/ajax/libs/jquery/" + version + "/jquery.min.js")))
        }

        mountPages()
    }

    companion object {

        private val log = LoggerFactory.getLogger(MyWebApplication::class.java)
    }

}
