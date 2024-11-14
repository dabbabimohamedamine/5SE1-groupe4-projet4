pipeline {  
    agent any

    stages {
         stage('lancement de services') {
            steps {
                script {
                    echo 'Starting SonarQube, Nexus, Grafana, and Prometheus containers...'
                    sh '''docker start f047b5df5d91 5b5f0a13965c 2c693693ad09 909675f08276'''
                    echo 'Waiting for all services to become available...'
                    def isServicesReady = false
                    def retryCount = 0
                    while (!isServicesReady && retryCount < 20) {
                        try {
                            sh 'curl -s http://192.168.50.131:9000'
                            sh 'curl -s http://192.168.50.131:8081'
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
        stage('git') {
            steps {
                cleanWs()
                echo 'Pulling source code from GitHub...'
                checkout([$class: 'GitSCM', branches: [[name: 'refs/heads/nour']],
                    doGenerateSubmoduleConfigurations: false, extensions: [],
                    userRemoteConfigs: [[
                        credentialsId: '4a5b8f09-8ed4-4a9c-841c-ad2e7ca6609e',
                        url: 'https://github.com/dabbabimohamedamine/5SE1-groupe4-projet4.git'
                    ]]
                ])
            }
        }

        stage('test unitaire') {
            steps {
                echo 'Running Unit Tests...'
                sh 'mvn test'
            }
        }

        stage('Analyse SonarQube') {
            steps {
                echo 'Running SonarQube analysis...'
                withCredentials([string(credentialsId: 'sonartoken', variable: 'SONAR_TOKEN')]) {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.host.url=http://192.168.50.131:9000 \
                        -Dsonar.projectKey=tn.esprit.amin.devops_project \
                        -Dsonar.projectName='My Project' \
                        -Dsonar.projectVersion=1.0 \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.language=java \
                        -Dsonar.login=\$SONAR_TOKEN
                    """
                }
            }
        }

        stage('maven build') {
            steps {
                echo 'Running Maven clean and package...'
                sh 'mvn clean package'
            }
        }

        stage('Nexus') {
            steps {
                echo 'Deploying artifact to Nexus...'
                withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh """
                        mvn deploy -DskipTests=true \
                        -DrepositoryId=nexus \
                        -Durl=http://192.168.178.129:8081/repository/maven-releases/ \
                        -Dusername=\$NEXUS_USERNAME \
                        -Dpassword=\$NEXUS_PASSWORD
                    """
                }
            }
        }

        stage('Docker Image') {
            steps {
                script {
                    def imageTag = "nour200/nour-5se1-g4-projet4:${env.BRANCH_NAME}"
                    echo "Building Docker image: ${imageTag}"
                    sh "docker build -t ${imageTag} ."
                }
            }
        }

        stage('push DockerHub') {
    steps {
        script {
            def imageTag = "nour200/nour-5se1-g4-projet4:${env.BRANCH_NAME}"
            echo "Logging in to DockerHub and pushing Docker image: ${imageTag}"

            // Login et push direct avec les credentials en dur (pas sécurisé)
            def DOCKER_HUB_USERNAME = 'nour200'
            def DOCKER_HUB_PASSWORD = 'c3F_gtEL.XWTqf3'

            sh "echo ${DOCKER_HUB_PASSWORD} | docker login -u ${DOCKER_HUB_USERNAME} --password-stdin"
            sh "docker push ${imageTag}"
        }
    }
}

        stage('Docker Compose') {
            steps {
                sh 'docker compose up -d'
                sh 'docker compose logs'
            }
        }
    }

    post {
        always {
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
            echo 'Build, tests, SonarQube analysis, and Docker image creation completed successfully!'
            archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
            junit 'target/surefire-reports/*.xml'
        }
        failure {
            echo 'Build, tests, or Docker image creation failed.'
        }
    }
}
