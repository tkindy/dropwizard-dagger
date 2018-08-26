package com.tylerkindy.dropwizard.dagger

import com.codahale.metrics.health.HealthCheck

abstract class NamedHealthCheck(val name: String) : HealthCheck()
