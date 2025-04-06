pipeline {
    agent any
    tools {
        maven "maven-3.9.8"
    }
    environment {
        docker_registry = 'iamroyalreddy/fusion-backend'
        DOCKERHUB_CREDENTIALS = credentials('docker-credentials')
        pomVersion = ''
        // SONAR_SCANNER_HOME = tool name: 'sonarqube'
    }
    stages {
        stage('Read POM') {
            steps {
                script {
                    def pom = readMavenPom(file: 'pom.xml')
                    pomVersion = pom.version
                    echo "Project version: ${pomVersion}"
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
                sh "docker push $docker_registry:v1"
            }       
        }

        stage('Deploy') {
            // when {
            //     expression{
            //         params.Deploy == 'true'
            //     }
            // }
            steps {
                script {
                        def params = [
                            string(name: 'VERSION', value: "$pomVersion"),
                            // string(name: 'environment', value: "dev")
                        ]
                        build job: "project-deploy", wait: true, parameters: params
                    }
            }
        }
        
    }
}