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
                stage('containerization') {
                    steps {
                        script {
                            def imageTag = "${docker_registry}:${pomVersion}"
                            echo "Building Docker image with tag: ${imageTag}"
                            sh "docker build -t ${imageTag} ."
                        }
                    }
                }
                // script{
                //     sh '''
                //         echo "Building Docker image with tag: ${docker_registry}:${pomVersion}"
                //         docker build -t ${docker_registry}:${pomVersion} .
                //     '''
                //     // sh '''
                //     //     EXISTING_IMAGE=$(docker images -q $docker_registry)
                //     //     if [ ! -z "$EXISTING_IMAGE" ]; then
                //     //         echo "previous build Image '$IMAGE_NAME' found. Removing..."
                //     //         docker rmi -f $EXISTING_IMAGE
                //     //         echo "previous build image is removed."
                //     //     else
                //     //         echo "No existing image found for '$IMAGE_NAME'."
                //     //     fi
                //     //     docker build -t $docker_registry:$GIT_COMMIT .
                //     // '''
                // }
            }
        }
        // stage('Publish Docker Image') {
        //     steps {
        //         sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
        //         sh "docker push $docker_registry:v1"
        //     }       
        // }

    }
}