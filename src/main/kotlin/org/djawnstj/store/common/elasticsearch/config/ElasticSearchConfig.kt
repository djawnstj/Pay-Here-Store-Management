package org.djawnstj.store.common.elasticsearch.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

@Configuration
@EnableElasticsearchRepositories
@EnableConfigurationProperties(ElasticSearchProperties::class)
class ElasticSearchConfig(
    private val elasticSearchProperties: ElasticSearchProperties
): ElasticsearchConfiguration() {
    override fun clientConfiguration(): ClientConfiguration {
        return ClientConfiguration.builder()
            .connectedTo(elasticSearchProperties.host)
            .build()
    }
}