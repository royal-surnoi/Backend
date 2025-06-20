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
        imageTag = ''
        AWS_ACCESS_KEY_ID = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
        AWS_DEFAULT_REGION = "us-east-1"
    }
    parameters {
        booleanParam(name: 'CodeAnalysisDependencyCheck', defaultValue: false, description: 'is it required Code Analysis and Dependency Check')
        // choice(name: 'DeployToStage', choices: ['yes', 'no'], description: 'is it required Deploy to stage')
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

        // stage('Code Analysis and Testing'){
        //      when {
        //         expression{
        //             params.CodeAnalysisDependencyCheck == true
        //         }
        //     }
        //     parallel{             
        //         stage('OWASP Dependency Check') {
        //             steps {
        //                     withCredentials([string(credentialsId: 'NVD-access', variable: 'NVD_API_KEY')]) {
        //                         dependencyCheck additionalArguments: """
        //                             --scan ./ 
        //                             --out ./  
        //                             --format ALL 
        //                             --disableYarnAudit
        //                             --prettyPrint
        //                             --noupdate                                    
        //                         """, odcInstallation: 'OWASP-DepCheck-10'
        //                        dependencyCheckPublisher failedTotalCritical: 1, pattern: 'dependency-check-report.xml', stopBuild: false
        //                 }
        //             }
        //         }
  

        //         stage('Unit Testing'){
        //             steps{
        //                 sh 'sleep 5s'
        //             }
        //         }

        //         stage ("SAST - SonarQube") {
        //             // currently skip test cases
        //             steps {
        //                     sh 'sleep 5s'
        //                 // script {
        //                 //     withSonarQubeEnv('sonarqube') {
        //                 //         withCredentials([string(credentialsId: 'sonar-credentials', variable: 'SONAR_TOKEN')]){
        //                 //             withEnv(["PATH+SONAR=$SONAR_SCANNER_HOME/bin"]) {
        //                 //                 sh '''
        //                 //                     mvn clean verify sonar:sonar -DskipTests \
        //                 //                         -Dsonar.projectKey=fusion-be \
        //                 //                         -Dsonar.projectName='fusion-be' \
        //                 //                         -Dsonar.host.url=$SONAR_HOST_URL \
        //                 //                         -Dsonar.token=$SONAR_TOKEN
        //                 //                 '''
        //                 //             }
        //                 //         }
        //                 //     }
        //                 // }
        //             }
        //         }
        //     }
        // }


        stage('containerization') {
            steps {
                script {
                    def imageTag = "${docker_registry}:${pomVersion}"
                    echo "Building Docker image with tag: ${imageTag}"
                    sh "docker build -t ${imageTag} ."
                }
            }
        }

        // stage('Trivy Vulnerability Scanner') {
        //     steps {
        //         sh  """
        //             trivy image $imageTag \
        //                 --severity LOW,MEDIUM,HIGH \
        //                 --exit-code 0 \
        //                 --quiet \
        //                 --format json -o trivy-image-MEDIUM-results.json

        //             trivy image $imageTag \
        //                 --severity CRITICAL \
        //                 --exit-code 1 \
        //                 --quiet \
        //                 --format json -o trivy-image-CRITICAL-results.json || true
        //         """
        //     }
        //      post {
        //         always {
        //             sh '''
        //                 trivy convert \
        //                     --format template --template "@/usr/local/share/trivy/templates/html.tpl" \
        //                     --output trivy-image-MEDIUM-results.html trivy-image-MEDIUM-results.json 

        //                 trivy convert \
        //                     --format template --template "@/usr/local/share/trivy/templates/html.tpl" \
        //                     --output trivy-image-CRITICAL-results.html trivy-image-CRITICAL-results.json

        //                 trivy convert \
        //                     --format template --template "@/usr/local/share/trivy/templates/junit.tpl" \
        //                     --output trivy-image-MEDIUM-results.xml  trivy-image-MEDIUM-results.json 

        //                 trivy convert \
        //                     --format template --template "@/usr/local/share/trivy/templates/junit.tpl" \
        //                     --output trivy-image-CRITICAL-results.xml trivy-image-CRITICAL-results.json          
        //             '''
        //         }
        //     }
        // }

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

        stage('Integration-test') {
            steps {
                script {
                    sh '''
                        set -e

                        aws eks update-kubeconfig --region us-east-1 --name fusioniq-dev
                        kubectl get nodes
                        kubectl apply -f namespace.yaml
                        kubectl apply -f backend-integrate-test.yaml
                        
                    '''
                }
            }
        }
// kubectl wait --namespace=fusioniq --for=condition=available deployment/backend --timeout=120s

                        // echo "Running curl test inside a test pod..."
                        // kubectl run curl-tester --rm -i --restart=Never --image=curlimages/curl:latest -n fusioniq \
                        // -- curl -s http://backend:8080/user/find/all
        stage('DAST - Full Scan (Backend)') {
            steps {
                script {
                    echo 'Starting OWASP ZAP full scan on backend via port-forward...'

                    // Validate service existence
                    sh 'kubectl get svc/backend -n fusioniq || { echo "Service backend not found"; exit 1; }'

                    // Start port-forward with debugging
                    sh '''
                        # Kill any existing port-forward processes
                        pkill -f "kubectl port-forward svc/backend" || true
                        kubectl port-forward svc/backend 8081:8080 -n fusioniq &
                        echo $! > pf_pid.txt
                        sleep 5
                        if ! ps -p $(cat pf_pid.txt) > /dev/null; then
                            echo "Port-forward failed to start"
                            kubectl describe svc/backend -n fusioniq
                            kubectl get pods -n fusioniq -l app=backend
                            exit 1
                        fi
                        # Verify port-forward connectivity
                        nc -zv localhost 8081 || { echo "Port-forward not working"; exit 1; }
                    '''

                    // Verify backend accessibility
                    sh '''
                        curl -f http://localhost:8081/health || { echo "Backend not accessible"; exit 1; }
                    '''

                    // Run ZAP full scan
                    sh '''
                        docker run --rm -v $(pwd):/zap/wrk ghcr.io/zaproxy/zaproxy:stable \
                            zap-full-scan.py \
                            -t http://localhost:8081/health \
                            -r zap-backend-fullscan.html \
                            -w /zap/wrk/zap-backend-fullscan.html 2>&1 | tee zap-scan.log
                    '''

                    // Stop port-forward and cleanup
                    sh '''
                        kill $(cat pf_pid.txt) || echo "Port-forward already stopped"
                        rm pf_pid.txt
                    '''

                    // Check for report
                    sh '''
                        if [ -f zap-backend-fullscan.html ]; then
                            echo "ZAP report generated successfully"
                        else
                            echo "Warning: ZAP report not found"
                        fi
                    '''

                    // Archive artifacts
                    archiveArtifacts artifacts: 'zap-backend-fullscan.html, zap-scan.log', allowEmptyArchive: true
                }
            }
        }

    // stage('DAST - Full Scan (Backend)') {
    //     steps {
    //         script {
    //             echo 'Starting OWASP ZAP full scan on backend via port-forward...'

    //             // Start port-forward in background
    //             sh 'kubectl port-forward svc/backend 8080:8080 -n fusioniq & echo $! > pf_pid.txt'
    //             sleep 5

    //             // Pull and run ZAP full scan using GitHub Container Registry image
    //             sh '''
    //                 docker run --rm -v $(pwd):/zap/wrk owasp/zap2docker-stable \
    //                     -t http://host.docker.internal:8080 \
    //                     -r zap-backend-fullscan.html \
    //                     -w /zap/wrk/zap-backend-fullscan.html || true

    //                 echo "Listing files in workspace:"
    //                 ls -lh /zap/wrk || true
    //             '''

    //             // Stop port-forward
    //             sh '''
    //                 kill $(cat pf_pid.txt) || true
    //                 rm pf_pid.txt
    //             '''

    //             // Archive the report
    //             archiveArtifacts artifacts: 'zap-backend-fullscan.html', onlyIfSuccessful: false
    //         }
    //     }
    // }




        
    }

    // post { 
    //     always { 
    //         junit allowEmptyResults: true, stdioRetention: '', testResults: 'dependency-check-junit.xml' 
    //         junit allowEmptyResults: true, stdioRetention: '', testResults: 'trivy-image-CRITICAL-results.xml'
    //         junit allowEmptyResults: true, stdioRetention: '', testResults: 'trivy-image-MEDIUM-results.xml'            
    //         publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: './', reportFiles: 'dependency-check-jenkins.html', reportName: 'Dependency Check HTML Report', reportTitles: '', useWrapperFileDirectly: true])
    //         publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: './', reportFiles: 'trivy-image-CRITICAL-results.html', reportName: 'Trivy Image Critical Vul Report', reportTitles: '', useWrapperFileDirectly: true])
    //         publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: './', reportFiles: 'trivy-image-MEDIUM-results.html', reportName: 'Trivy Image Medium Vul Report', reportTitles: '', useWrapperFileDirectly: true])
    //         echo "\033[34mJob completed. Cleaning up workspace...\033[0m"
    //         deleteDir()
    //     }
    //     success {
    //         echo "\033[33mPipeline completed successfully. Performing success actions...\033[0m"
    //         // Add additional actions here if needed, like sending success notifications
    //     }
    //     failure { 
    //         echo "\033[35mPipeline failed. Triggering failure response...\033[0m"
    //         // send notification
    //     }
    //     unstable {
    //         echo "\033[34mPipeline marked as unstable. Reviewing issues...\033[0m"
    //         // Send notification or take action for unstable builds, if needed
    //     }
    //     aborted {
    //         echo "\033[33mPipeline was aborted. Clearing any partial artifacts...\033[0m"
    //         // Any specific actions for aborted jobs
    //     }
    // }
}