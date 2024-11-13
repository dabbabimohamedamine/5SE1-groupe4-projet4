pipeline {  
    agent any

    environment {
        DOCKER_HUB_REPO = 'mohamedaminedbbabi/5se1-g4'
        DOCKER_HUB_CREDENTIALS = 'dockerhub-5se-g4'
        SONARQUBE_URL = 'http://192.168.17.128:9000'
        SONARQUBE_TOKEN = credentials('new-sonar-token')
        SONARQUBE_PROJECT_KEY = 'tn.esprit.amin.devops_project'
    }

    stages {
        stage('Checkout GIT') {
            steps {
                cleanWs()
                echo 'Pulling source code from GitHub...'
                checkout([$class: 'GitSCM', branches: [[name: 'amine-dabbabi']],
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

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar ' +
                        '-Dsonar.projectKey=${SONARQUBE_PROJECT_KEY} ' +
                        '-Dsonar.host.url=${SONARQUBE_URL} ' +
                        '-Dsonar.login=${SONARQUBE_TOKEN}'
                }
            }
        }

        stage('Build and Package') {
            steps {
                echo 'Running Maven clean and package...'
                sh 'mvn clean package'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                echo 'Deploying artifact to Nexus...'
                sh 'mvn deploy -DskipTests=true -s /usr/share/maven/conf/settings.xml'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageTag = "${DOCKER_HUB_REPO}:${BRANCH_NAME}"
                    def latestTag = "${DOCKER_HUB_REPO}:latest"
                    echo "Building Docker image: ${imageTag}"
                   
                    // Build and tag both with branch name and "latest"
                    sh "docker build -t ${imageTag} ."
                    sh "docker tag ${imageTag} ${latestTag}"
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    def imageTag = "${DOCKER_HUB_REPO}:${BRANCH_NAME}"
                    def latestTag = "${DOCKER_HUB_REPO}:latest"
                    echo "Pushing Docker images to DockerHub: ${imageTag} and ${latestTag}"
                   
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_HUB_CREDENTIALS) {
                        // Push both the branch-specific tag and the latest tag
                        sh "docker push ${imageTag}"
                        sh "docker push ${latestTag}"
                    }
                }
            }
        }

        stage('Cleanup Old Images') {
            steps {
                script {
                    // Remove all untagged (dangling) images to save space
                    sh 'docker image prune -f'
                }
            }
        }
    }

    post {
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


