package com.project.dia.ApplicationContext

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class SpringApplicationContext(context: ApplicationContext?=null
) :ApplicationContextAware {
    override fun setApplicationContext(context: ApplicationContext) {
        CONTEXT = context
    }

    companion object {
        private var CONTEXT: ApplicationContext? = null
        fun getBean(beanName: String?): Any {
            return CONTEXT!!.getBean(beanName!!)
        }
    }
}