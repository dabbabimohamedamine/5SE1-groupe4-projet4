pipeline {  
    agent any

    stages {
        stage('stop services') {
            steps {
                script {
                    echo 'Stopping Docker containers...'
                    sh '''
                         docker stop 2afa7431e894 9aa6fdaa5fa8 2ab58c4e9f7e 52a60192d063 2175f6502428 bffcba365d78
                    '''
                    echo 'Docker containers have been stopped.'
                }
            }
        }

        stage('start services :sq,prom,graf,nex') {
            steps {
                script {
                    echo 'Starting SonarQube, Nexus, Grafana, and Prometheus containers...'
                    sh '''docker start 2175f6502428 52a60192d063 2ab58c4e9f7e 9aa6fdaa5fa8'''
                    echo 'Waiting for all services to become available...'
                    def isServicesReady = false
                    def retryCount = 0
                    while (!isServicesReady && retryCount < 20) {
                        try {
                            sh 'curl -s http://192.168.204.128:9000'
                            sh 'curl -s http://192.168.204.128:8081'
                            isServicesReady = true
                        } catch (Exception e) {
                            echo "Service not ready yet, retrying in 15 seconds..."
                            sleep 15
                            retryCount++
                        }
                    }
                    if (!isServicesReady) {
                        error 'Timed out: Services did not start within the expected time.'
                    } else {
                        echo 'All services are now up and ready. Moving to the next stage.'
                    }
                }
            }
        }

        stage('get code from git') {
            steps {
                script {
                    cleanWs()
                    def gitRepoUrl = 'https://github.com/dabbabimohamedamine/5SE1-groupe4-projet4.git'
                    def gitBranch = 'refs/heads/anis'
                    def gitCredentials = 'c024349e-0754-4415-b7a9-433bc2d533de'
                    echo "Attempting to clone repository ${gitRepoUrl} from branch ${gitBranch}..."
                    checkout scm: [
                        $class: 'GitSCM',
                        branches: [[name: gitBranch]],
                        userRemoteConfigs: [
                            [url: gitRepoUrl, credentialsId: gitCredentials]
                        ]
                    ]
                    echo "Successfully checked out code from ${gitRepoUrl} on branch ${gitBranch}."
                }
            }
        }

        stage('unit tests') {
            steps {
                echo 'Executing unit tests with Maven...'
                sh 'mvn test'
            }
        }

stage('sonarqube') {
    steps {
        script {
            def sonarUrl = 'http://192.168.204.128:9000'
            def sonarProjectKey = 'tn.esprit.amin.devops_project'
            def sonarProjectName = 'My Project'
            def sonarProjectVersion = '1.0'
            def sonarSourceDirectory = 'src/main/java'

            echo "Initiating SonarQube analysis for project ${sonarProjectName}..."

            withCredentials([string(credentialsId: 'sonartoken2', variable: 'SONAR_TOKEN')]) {
                sh """
                    mvn sonar:sonar \
                    -Dsonar.host.url=${sonarUrl} \
                    -Dsonar.projectKey=${sonarProjectKey} \
                    -Dsonar.projectName="${sonarProjectName}" \
                    -Dsonar.projectVersion=${sonarProjectVersion} \
                    -Dsonar.sources=${sonarSourceDirectory} \
                    -Dsonar.language=java \
                    -Dsonar.login=${SONAR_TOKEN}
                """
            }
            echo "SonarQube analysis for ${sonarProjectName} completed successfully."
        }
    }
}

        stage('build maven') {
            steps {
                echo 'Building and packaging the application with Maven...'
                sh 'mvn clean package'
            }
        }

        stage('artefact to nexus') {
            steps {
                script {
                    def nexusUrl = 'http://192.168.204.128:8081/repository/maven-releases'
                    def repositoryId = 'nexus'
                    echo "Initiating artifact deployment to Nexus repository at ${nexusUrl}..."
                    withCredentials([usernamePassword(credentialsId: 'nexustoken2', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh """
                            mvn deploy \
                            -DskipTests=true \
                            -DrepositoryId=${repositoryId} \
                            -Durl=${nexusUrl} \
                            -Dusername=\$NEXUS_USERNAME \
                            -Dpassword=\$NEXUS_PASSWORD
                        """
                    }
                    echo "Artifact successfully deployed to Nexus repository."
                }
            }
        }

        stage('docker image') {
            steps {
                script {
                    def dockerTag = "aniskh643/anis-5se1-g4-projet4:${env.BRANCH_NAME}"
                    echo "Building Docker image with tag: ${dockerTag}"
                    sh "docker build -t ${dockerTag} ."
                }
            }
        }

        stage('push docker image') {
            steps {
                script {
                    def dockerHubUser = 'aniskh643'
                    def dockerRepo = 'anis-5se1-g4-projet4'
                    def branchName = env.BRANCH_NAME
                    def dockerTag = "${dockerHubUser}/${dockerRepo}:${branchName}"
                    echo "Preparing to push Docker image ${dockerTag} to Docker Hub..."
                    echo 'Logging in to Docker Hub...'
                    sh "echo Heel-2020 | docker login -u ${dockerHubUser} --password-stdin"
                    echo "Pushing Docker image to Docker Hub..."
                    sh "docker push ${dockerTag}"
                    echo "Successfully pushed Docker image ${dockerTag} to Docker Hub."
                }
            }
        }

        stage('start mysql and spring with docker compose') {
            steps {
                echo 'Deploying the application using Docker Compose...'
                sh 'docker compose up -d'
                sh 'docker compose logs'
            }
        }

    }

    post {
        always {
            echo 'Generating code coverage report...'
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: false,
                keepAll: true,
                reportDir: './target/site/jacoco',
                reportFiles: 'index.html',
                reportName: 'Jacoco Code Coverage Report'
            ])
        }

        success {
            echo 'Pipeline completed successfully!'
            archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
            junit 'target/surefire-reports/*.xml'
        }

        failure {
            echo 'Pipeline failed. Please check the logs for more details.'
        }
    }
}