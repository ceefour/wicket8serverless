package com.hendyirawan.wicket8serverless.web

import org.apache.wicket.markup.head.CssReferenceHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.resource.CssResourceReference

abstract class PubLayout(parameters: PageParameters) : WebPage(parameters) {

    override fun renderHead(response: IHeaderResponse?) {
        super.renderHead(response)
        response!!.render(CssReferenceHeaderItem.forReference(CssResourceReference(javaClass, "PubLayout.css")))
        //        response.render(FontAwesome.asHeaderItem());
    }
}
