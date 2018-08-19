#!/usr/bin/env groovy
import com.travelstart.plugins.jenkins.sonar.Coverage

def call(final Map args) {

    println(env.SONAR_HOST_URL)
    println(env.SONAR_AUTH_TOKEN)

    final String gitRepo = env.GIT_REPO ?: sh(returnStdout: true, script: 'git config remote.origin.url').trim().replace("https://github.com","").replace(".git", "")
    final String gitToken = env.GIT_TOKEN
    final String gitPrId = env.CHANGE_ID ?: sh(returnStdout: true, script: 'git rev-parse HEAD').trim()

    final def coverage = new Coverage(env.SONAR_HOST_URL, env.SONAR_AUTH_TOKEN, gitRepo, gitToken)
    final def projects = []
    final def newMetric = args.new_coverage ? (args.new_coverage) as Boolean : false

    projects.add(args.originalId)
    projects.add(args.newId)
    //args.originalId ?: projects.add(args.originalId)
    //args.newId ?: projects.add(args.newId)

    println(args.originalId)
    println(args.newId)

    coverage.compare(gitPrId, projects, newMetric)

}