/**
 * <p>
 * This is the script that will be run by Jenkins to build and test this
 * project. This drives the project's continuous integration and delivery.
 * </p>
 * <p>
 * This script is run by Jenkins' Pipeline feature. A good intro tutorial for
 * working with Jenkins Pipelines can be found here:
 * <a href="https://jenkins.io/doc/book/pipeline/">Jenkins > Pipeline</a>.
 * </p>
 * <p>
 * The canonical Jenkins server job for this project is located here:
 * <a href="https://justdavis.com/jenkins/job/jessentials/">jessentials</a>.
 * </p>
 */

node {
	stage('Checkout') {
		// Grab the commit that triggered the build.
		checkout scm
	}
	
	stage('Build') {
		mvn "--update-snapshots clean deploy"
	}
}

/**
 * Runs Maven with the specified arguments.
 *
 * @param args the arguments to pass to <code>mvn</code>
 */
def mvn(args) {
	// This tool must be setup and named correctly in the Jenkins config.
	def mvnHome = tool 'maven-3'

	// Run the build, using Maven, with the appropriate config.
	configFileProvider(
			[
				configFile(fileId: 'justdavis:settings.xml', variable: 'MAVEN_SETTINGS'),
				configFile(fileId: 'justdavis:toolchains.xml', variable: 'MAVEN_TOOLCHAINS')
			]
	) {
		sh "${mvnHome}/bin/mvn --settings $MAVEN_SETTINGS --toolchains $MAVEN_TOOLCHAINS ${args}"
	}
}
