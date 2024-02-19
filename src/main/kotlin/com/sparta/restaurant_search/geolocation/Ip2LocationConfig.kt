package com.sparta.restaurant_search.geolocation

import com.ip2location.IPGeolocation
import com.ip2location.spring.IPGeolocationSpring
import com.ip2location.spring.strategies.attribute.AttributeStrategy
import com.ip2location.spring.strategies.attribute.RequestAttributeStrategy
import com.ip2location.spring.strategies.attribute.SessionAttributeStrategy
import com.ip2location.spring.strategies.interceptor.BotInterceptorStrategy
import com.ip2location.spring.strategies.ip.SimpleIPStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class Ip2LocationConfig: WebMvcConfigurer {
    @Bean(name = ["attributeStrategy"])
    fun createBean(): AttributeStrategy {
        val bean: AttributeStrategy = RequestAttributeStrategy()
        return bean
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        val config = com.ip2location.Configuration()
        val apiKey = "FDCBD1614AF19B34687E78EDA41B52E8"
        config.apiKey = apiKey
        val ipGeo = IPGeolocation(config)
        val ipGeolocationSpring = IPGeolocationSpring.Builder()
            .setIPGeolocation(ipGeo)
            .interceptorStrategy(BotInterceptorStrategy())
            .ipStrategy(SimpleIPStrategy())
            .attributeStrategy(SessionAttributeStrategy())
            .build()
        registry.addInterceptor(ipGeolocationSpring)
    }
}