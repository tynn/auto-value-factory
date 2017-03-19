# AutoValue: Factory Extension

Mix the usage of the `@AutoValue` and `@AutoFactory` [Auto] libraries.


## Usage

The property type should be annotated with an `@AutoValueFactory` annotation
holding the actual `@AutoFactory` definition. All `@Provided` properties need
to be annotated with `@FactoryProvided` and any `@Qualifier` which should be
applied to it.

    @AutoValue
    @AutoValueFactory(@AutoFactory)
    public abstract class AutoValueWithFactoryType {

        @FactoryProvided
        @AQualifier
        public abstract String provided();

        @Nullable
        @FactoryProvided
        public abstract String nullableProvided();

        @Deprecated
        @TestQualifier
        @TestAnnotation
        public abstract String notProvided();
    }


## Installation

Use [JitPack] with `autoValueFactoryVersion` set to the id of *HEAD*.
Then add the following dependencies to the *build.gradle*.

    compile "com.github.tynn.auto-value-factory:runtime:$autoValueFactoryVersion"
    compileOnly "com.github.tynn.auto-value-factory:annotations:$autoValueFactoryVersion"
    annotationProcessor "com.github.tynn.auto-value-factory:extension:$autoValueFactoryVersion"

  [Auto]: https://github.com/google/auto
  [JitPack]: https://jitpack.io/#com.github.tynn/auto-value-factory
