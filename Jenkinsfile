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
		mvn "--update-snapshots -Dmaven.test.failure.ignore=true clean deploy"
	}

	stage('Archive') {
		/*
		 * Fingerprint the output artifacts and archive the test results.
		 * (Archiving the artifacts here would waste space, as the build
		 * deploys them to the local Maven repository.)
		 */
		fingerprint '**/target/*.jar'
		junit testResults: '**/target/*-reports/TEST-*.xml', keepLongStdio: true
		archiveArtifacts artifacts: '**/target/*-reports/*.txt', allowEmptyArchive: true
	}

	stage('Quality Analysis') {
		/*
		 * The 'justdavis-sonarqube' SonarQube server will be sent the analysis
		 * results. See
		 * https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Jenkins
		 * for details.
		 */
		withSonarQubeEnv('justdavis-sonarqube') {
			mvn "org.sonarsource.scanner.maven:sonar-maven-plugin:3.4.0.905:sonar"
		}
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
