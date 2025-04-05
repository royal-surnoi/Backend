pipeline {
    agent any
    tools {
        maven "maven-3.9.8"
    }
    environment {
        docker_registry = 'iamroyalreddy/fusion-backend'
        DOCKERHUB_CREDENTIALS = credentials('docker-credentials')
        // SONAR_SCANNER_HOME = tool name: 'sonarqube'
    }
    stages {
        stage('Build and Package'){
            steps{
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('containerization') {
            steps {
                script{
                    sh '''
                        docker build -t $docker_registry:v1 .
                    '''
                    // sh '''
                    //     EXISTING_IMAGE=$(docker images -q $docker_registry)
                    //     if [ ! -z "$EXISTING_IMAGE" ]; then
                    //         echo "previous build Image '$IMAGE_NAME' found. Removing..."
                    //         docker rmi -f $EXISTING_IMAGE
                    //         echo "previous build image is removed."
                    //     else
                    //         echo "No existing image found for '$IMAGE_NAME'."
                    //     fi
                    //     docker build -t $docker_registry:$GIT_COMMIT .
                    // '''
                }
            }
        }
        stage('Publish Docker Image') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh "docker push $docker_registry:v1"
            }       
        }

    }
}