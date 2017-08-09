#!/usr/bin/env groovy

pipeline {
    agent any
    stages {

        stage ('Compile') {
            steps {
                //-B non interactive batch mode
                //sh 'mvn -B -Dmaven.test.failure.ignore=true clean compile'
                sh 'mvn -B clean compile'
            }
        }

        stage ('Test') {
            steps {
                sh 'mvn -B test verify'
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml'
                    jacoco()
                }
            }
        }

        stage ('StaticAnalysis') {
            steps {
                    sh 'mvn -B pmd:pmd pmd:cpd jdepend:generate'
            }
            post {
                success {
                    pmd canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '', unHealthy: ''
                    dry canComputeNew: false, defaultEncoding: '', healthy: '', pattern: '', unHealthy: ''
                    //jdepend is not supported yet
                    //I found

                }

            }

        }

        stage ('JavaDoc') {
            steps {
                sh 'mvn -B javadoc:javadoc'
                }
               post {
                    success {
                        javadoc()
                    }
               }
        }

        //A step before deploying
        stage ('Package') {
            steps {
                sh 'mvn -B -Dmaven.test.skip=true package'
            }
            post {
                success {
                    archiveArtifacts 'target/**/*.war'
                    //archive includes: '*.jar, excludes: '*-sources.jar'
                }
            }

        }

        //TODO

        //integration-test

        //Deploy Stage

        //Run end to end tests

    }
}