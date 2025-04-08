pipeline {
    agent any
    tools {
        maven "maven-3.9.8"
    }
    options{
        disableConcurrentBuilds()
        disableResume()
        timeout(time: 1, unit: "HOURS")
    }
    environment {
        docker_registry = 'iamroyalreddy/fusion-backend'
        DOCKERHUB_CREDENTIALS = credentials('docker-credentials')
        pomVersion = ''
    }
    stages {
        stage('Read POM') {
            steps {
                script {
                    def pom = readMavenPom(file: 'pom.xml')
                    pomVersion = pom.version
                    echo "Project version: ${pomVersion}"
                    // Save version to a file
                    writeFile file: 'image-version.txt', text: pomVersion
                }
            }
        }
        stage('Build and Package'){
            steps{
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('containerization') {
            steps {
                script {
                    def imageTag = "${docker_registry}:${pomVersion}"
                    echo "Building Docker image with tag: ${imageTag}"
                    sh "docker build -t ${imageTag} ."
                }
            }
        }
        stage('Publish Docker Image') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh "docker push $docker_registry:${pomVersion}"
            }       
        }
        stage('Archive Image Version') {
            steps {
                archiveArtifacts artifacts: 'image-version.txt', onlyIfSuccessful: true
            }
        }
        
    }
}