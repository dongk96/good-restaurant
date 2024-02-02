package com.sparta.restaurant_search.geolocation

import com.maxmind.db.CHMCache
import com.maxmind.geoip2.DatabaseReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.io.InputStream

@Configuration
class GeoLocationConfig {
    @Bean("databaseReader")
    fun databaseReader(): DatabaseReader {
        val inputStream: InputStream = ClassPathResource("geolocation/GeoLite2-City.mmdb").inputStream

        return DatabaseReader
            .Builder(inputStream)
            .withCache(CHMCache())
            .build()
    }
}