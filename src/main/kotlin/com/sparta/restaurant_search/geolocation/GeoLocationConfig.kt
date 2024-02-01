package com.sparta.restaurant_search.geolocation

import com.maxmind.db.CHMCache
import com.maxmind.geoip2.DatabaseReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.File

@Configuration
class GeoLocationConfig {
    @Bean("databaseReader")
    fun databaseReader(): DatabaseReader {
        val resource = ClassPathResource("geolocation/GeoLite2-City.mmdb")
        val file = resource.file

        return DatabaseReader
            .Builder(file)
            .withCache(CHMCache())
            .build()
    }
}