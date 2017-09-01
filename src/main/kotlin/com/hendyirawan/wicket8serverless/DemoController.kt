package com.hendyirawan.wicket8serverless

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

//@RestController
class DemoController {

    @GetMapping("/")
    fun getHome(): String {
        return "Hello"
    }
}