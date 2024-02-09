package org.djawnstj.store.common.config

import org.djawnstj.store.common.provider.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ComponentConfig {

    @Bean
    fun Base64Provider(): Base64Provider = JavaBase64Provider()

    @Bean
    fun uuidProvider(): UUIDProvider = JavaUUIDProvider()

    @Bean
    fun timeProvider(): TimeProvider = ZonedDateTimeProvider()

}