package com.tylerkindy.dropwizard.dagger

interface DropwizardInjector<T> {
    fun inject(application: T)
}
