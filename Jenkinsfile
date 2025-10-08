// Jenkinsfile

pipeline {
    agent any // 파이프라인을 실행할 Jenkins 에이전트 지정

    // 환경 변수 정의
    environment {
        DOCKERHUB_USERNAME = 'yhcho14' // 본인의 Docker Hub 사용자 이름으로 변경
        APP_NAME = 'beanbuddies-matching-platform'

    }

    stages {
        stage('Checkout') {
            steps {
                // GitHub 저장소에서 코드를 가져옵니다.
                git branch: 'main', url: 'https://github.com/yhcho14/AIBE3-TEAM2-BeanBuddies-MatchingPlatform.git'
            }
        }

        stage('Build') {
            steps {
                // Gradle 실행 권한을 부여하고, clean build를 통해 빌드 및 테스트를 수행합니다.
                dir('backend') {
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build -x test'
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {
                    // Dockerfile을 사용하여 이미지를 빌드합니다. 태그는 빌드 번호를 사용합니다.
                    def appImage = docker.build("${DOCKERHUB_USERNAME}/${APP_NAME}:${env.BUILD_NUMBER}", './backend')

                    // Jenkins Credentials에 등록한 Docker Hub 인증 정보를 사용하여 로그인합니다.
                    docker.withRegistry('https://registry.hub.docker.com', 'yhcho-dockerhub') {
                        // 빌드한 이미지를 Docker Hub에 Push 합니다.
                        appImage.push()
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                // Jenkins Credentials에 등록한 SSH 키를 사용하여 배포 서버에 접속합니다.
                sshagent(['yhcho-ssh']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no yhcho@192.168.50.35 \
                        "docker stop ${APP_NAME} || true && docker rm ${APP_NAME} || true"
                    """
                    sh """
                        ssh -o StrictHostKeyChecking=no yhcho@192.168.50.35 \
                        "docker pull ${DOCKERHUB_USERNAME}/${APP_NAME}:${env.BUILD_NUMBER}"
                    """
                    sh """
                        ssh -o StrictHostKeyChecking=no yhcho@192.168.50.35 \
                        "docker run -d --name ${APP_NAME} -p 8080:8080 \
                        -e SPRING_PROFILES_ACTIVE=prod \
                        -e SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/db_dev \
                        -e SPRING_DATASOURCE_USERNAME=db-username \
                        -e SPRING_DATASOURCE_PASSWORD=db-password \
                        -e CUSTOM_JWT_ACCESSTOKEN_SECRETKEY=jwt-secret-key \
                        -e CUSTOM_JWT_ACCESSTOKEN_EXPIRESECONDS=3600\
                        -e CUSTOM_JWT_REFRESHTOKEN_SECRETKEY=jwt-refresh-key \
                        ${DOCKERHUB_USERNAME}/${APP_NAME}:${env.BUILD_NUMBER}"
                    """
                }
            }
        }
    }

    post {
        always {
            // 빌드 중간 산물들을 정리합니다.
            cleanWs()
        }
    }
}