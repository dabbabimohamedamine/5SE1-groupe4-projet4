pipeline {
    agent any

    environment {
            DOCKER_HUB_REPO = 'mohamedaminedabbabi/5se-g4'
            DOCKER_HUB_CREDENTIALS = 'dokerhub-5se-g4'
        }

    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling...'
                checkout([$class: 'GitSCM', branches: [[name: '*/master']],
                    doGenerateSubmoduleConfigurations: false, extensions: [],
                    userRemoteConfigs: [[
                        credentialsId: '1dd4e651-22db-44e2-8285-a907f0267d5b',
                        url: 'https://github.com/dabbabimohamedamine/devops-back.git'
                    ]]
                ])
            }
        }
        stage('Run Unit Tests') {
                    steps {
                        echo 'Running Unit Tests...'
                        sh 'mvn test'
                    }
                }
        stage('Build and Package') {
            steps {
                echo 'Running Maven clean and package...'
                sh 'mvn clean package'
            }
        }
        stage('Build Docker Image') {
                    steps {
                        script {
                            docker.build("${env.DOCKER_HUB_REPO}:latest")
                        }
                    }
                }
                stage('Push to DockerHub') {
                    steps {
                        script {
                            docker.withRegistry('https://index.docker.io/v1/', "${env.DOCKER_HUB_CREDENTIALS}") {
                                sh "docker push ${env.DOCKER_HUB_REPO}:latest"
                            }
                        }
                    }
                }
    post {
        success {
            echo 'Build and tests completed successfully!'
            archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
            junit 'target/surefire-reports/*.xml'
        }
        failure {
            echo 'Build or tests failed.'
        }
    }
    }
}
