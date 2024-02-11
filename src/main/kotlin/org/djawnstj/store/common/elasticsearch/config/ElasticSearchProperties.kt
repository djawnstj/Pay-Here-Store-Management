package org.djawnstj.store.common.elasticsearch.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "elastic")
data class ElasticSearchProperties(
    val host: String,
)
