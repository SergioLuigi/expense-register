package br.com.sergioluigi.expenseregister.infra.audit

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

/**
 * This class was created because, during the initialization of test classes annotated with @WebFluxTest,
 * spring throws BeanCreationException.
 * Other possible solution would create other environments and use @ActiveProfiles like is described in this
 * article : http://www.javafixing.com/2022/05/fixed-how-to-disable-configuration.html?m=1
 */
@Configuration
@EnableJpaAuditing
class JpaAuditingConfig {
}