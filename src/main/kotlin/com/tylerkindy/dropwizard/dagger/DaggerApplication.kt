package com.tylerkindy.dropwizard.dagger

import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.setup.Environment
import javax.inject.Inject

abstract class DaggerApplication<C : Configuration> : Application<C>() {

    @Inject
    @JvmSuppressWildcards
    lateinit var resources: Set<Resource>

    @Inject
    @JvmSuppressWildcards
    lateinit var healthChecks: Set<NamedHealthCheck>

    override fun run(configuration: C, environment: Environment) {
        (applicationInjector(configuration) as DropwizardInjector<DaggerApplication<*>>).inject(this)

        resources.forEach { resource ->
            environment.jersey().register(resource)
        }

        healthChecks.forEach { healthCheck ->
            environment.healthChecks().register(healthCheck.name, healthCheck)
        }
    }

    protected abstract fun applicationInjector(configuration: C): DropwizardInjector<out DaggerApplication<*>>
}
