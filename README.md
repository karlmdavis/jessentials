jessentials
===========

A set of basic "why doesn't this already exist?" utility/library code for Java projects.

# Performing a Release

The [maven-release-plugin](https://maven.apache.org/maven-release/maven-release-plugin/) is used to perform releases of this project's modules, deploying the build results to [Maven Central](http://central.sonatype.org/) (by way of [OSSRH](http://central.sonatype.org/pages/ossrh-guide.html)).

The modules in this project are intended to be release separately/independently: a release of `jessentials-parent` shouldn't automatically release `jessentials-misc`, for example. To ensure this, only run releases from the module subdirectories, like this:

$ cd jessentials.git
$ cd jessentials-parent
$ mvn release:prepare release:perform
