package com.tylerkindy.dropwizard.dagger

import dagger.Module
import dagger.multibindings.Multibinds

@Module
interface DropwizardInjectionModule {
    @Multibinds
    fun resources(): Set<Resource>

    @Multibinds
    fun healthChecks(): Set<NamedHealthCheck>
}
