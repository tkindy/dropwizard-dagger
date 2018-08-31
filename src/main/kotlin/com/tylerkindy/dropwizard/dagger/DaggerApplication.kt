package com.tylerkindy.dropwizard.dagger

import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.setup.Environment
import javax.annotation.OverridingMethodsMustInvokeSuper
import javax.inject.Inject

abstract class DaggerApplication<C : Configuration> : Application<C>() {

    @Inject
    @JvmSuppressWildcards
    internal lateinit var resources: Set<Resource>

    @Inject
    @JvmSuppressWildcards
    internal lateinit var healthChecks: Set<NamedHealthCheck>

    @OverridingMethodsMustInvokeSuper
    override fun run(configuration: C, environment: Environment) {
        @Suppress("UNCHECKED_CAST")
        (applicationInjector(configuration, environment) as DropwizardInjector<DaggerApplication<*>>)
            .inject(this)

        resources.forEach { resource ->
            environment.jersey().register(resource)
        }

        healthChecks.forEach { healthCheck ->
            environment.healthChecks().register(healthCheck.name, healthCheck)
        }
    }

    protected abstract fun applicationInjector(
        configuration: C,
        environment: Environment
    ): DropwizardInjector<out DaggerApplication<*>>
}
