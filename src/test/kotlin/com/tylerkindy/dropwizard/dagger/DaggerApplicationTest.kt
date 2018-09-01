package com.tylerkindy.dropwizard.dagger

import com.nhaarman.mockitokotlin2.mock
import dagger.Component
import io.dropwizard.Configuration
import io.dropwizard.setup.Environment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verifyZeroInteractions
import javax.inject.Singleton

class DaggerApplicationTest {

    lateinit var environment: Environment

    @Before
    fun setUp() {
        environment = mock()
    }

    @Test
    fun itBindsNoResources() {
        val app = TestApplication().apply { run(TestConfiguration(), environment) }

        verifyZeroInteractions(environment)
        assertThat(app.resources).isEmpty()
    }
}

@Singleton
@Component(
        modules = [
            DropwizardInjectionModule::class
        ]
)
private interface TestComponent : DropwizardInjector<TestApplication>

private class TestApplication : DaggerApplication<TestConfiguration>() {
    override fun applicationInjector(
        configuration: TestConfiguration,
        environment: Environment
    ): DropwizardInjector<out DaggerApplication<*>> {
        return DaggerTestComponent.create()
    }
}

private class TestConfiguration : Configuration()
