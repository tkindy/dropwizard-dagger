# dropwizard-dagger

[![CircleCI](https://img.shields.io/circleci/project/github/tkindy/dropwizard-dagger/master.svg?label=circleci)](https://circleci.com/gh/tkindy/dropwizard-dagger/tree/master)
[![Bintray](https://img.shields.io/bintray/v/tkindy/maven/dropwizard-dagger.svg)](https://bintray.com/tkindy/maven/dropwizard-dagger)

[Dagger](https://github.com/google/dagger) helpers for [Dropwizard](https://github.com/dropwizard/dropwizard) applications in the spirits of [dagger.android](https://google.github.io/dagger/api/latest/dagger/android/package-summary.html) and [dropwizard-guicier](https://github.com/HubSpot/dropwizard-guicier).

This library leverages Dagger to automatically construct and register Dropwizard resources and health checks on application launch.

## Usage

Add the following to your `build.gradle`:

```groovy
repositories {
  jcenter()
}

dependencies {
  implementation "com.tylerkindy:dropwizard-dagger:0.1.0-alpha03"
}
```

Have each of your Dropwizard resources implement the `Resource` marker interface:

```kotlin
@Path("/hello")
class HelloResource @Inject constructor() : Resource
```

> `Resource` doesn't require your resource implement anything, it's just to allow for a Dagger multibinding.

Do the same for your health checks with `NamedHealthCheck`:

```kotlin
class HelloHealthCheck @Inject constructor(): NamedHealthCheck("hello")
```

Add Dagger bindings for your resources and health checks:

```kotlin
@Module
interface DropwizardModule {
  @Binds
  @IntoSet
  fun bindHelloResource(resource: HelloResource): Resource

  @Binds
  @IntoSet
  fun bindHelloHealthCheck(healthCheck: HelloHealthCheck): NamedHealthCheck
}
```

> This is just an example of how to break out your modules. As long as all of your resources are bound in the dependency graph, the library will be able to inject them.

Then, add your module(s) as well as the `DropwizardInjectionModule` to your `@Component` and have it implement `DropwizardInjector`:

```kotlin
@Singleton
@Component(
  modules = [
    DropwizardModule::class,
    DropwizardInjectionModule::class
  ]
)
interface ApplicationComponent : DropwizardInjector<HelloWorldApplication>
```

Rebuild the project and extend `DaggerApplication` instead of Dropwizard's `Application`:

```kotlin
class HelloWorldApplication : DaggerApplication<HelloWorldConfiguration>() {
  override fun applicationInjector(
    configuration: HelloWorldConfiguration,
    environment: Environment
  ): DropwizardInjector<HelloWorldApplication> {
    return DaggerApplicationComponent.create()
  }
}
```

Now, you can run your application and Dagger will automatically construct and register your resources and health checks.

## Publishing

Run the build and tests:

```
> ./gradlew clean check
```

Then, update the version number in `build.gradle.kts` and in the example above and commit the changes:

```
> git add build.gradle.kts README.md
> git commit -m "Prepare x.y.z release"
```

Finally, rebuild and publish to Bintray:

```
> ./gradlew bintrayUpload
```

## Work in Progress

It's pretty cumbersome to use this library in its experimental form. I'm developing it actively to make it easier to implement in a new or existing Dropwizard project.
