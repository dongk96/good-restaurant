package com.sparta.restaurant_search.geolocation

import com.maxmind.db.CHMCache
import com.maxmind.db.Reader
import com.maxmind.geoip2.DatabaseReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import java.io.File
import java.io.InputStream

@Configuration
class GeoLocationConfig {
    @Bean("databaseReader")
    fun databaseReader(): DatabaseReader {
        val database = FileSystemResource("/geolocation/geoLite2-city.mmdb")

//        val database = ClassPathResource("geolocation/GeoLite2-City.mmdb")
        return DatabaseReader
            .Builder(database.file)
            .fileMode(Reader.FileMode.MEMORY_MAPPED)
            .withCache(CHMCache())
            .build()
    }
}