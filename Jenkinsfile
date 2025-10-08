// Jenkinsfile

pipeline {
    agent any // 파이프라인을 실행할 Jenkins 에이전트 지정

    // 환경 변수 정의
    environment {
        DOCKERHUB_USERNAME = 'yhcho14' // 본인의 Docker Hub 사용자 이름으로 변경
        APP_NAME = 'beanbuddies-matching-platform'

        DEPLOY_HOST = '192.168.50.35'
        DEPLOY_USER = 'yhcho'
        DB_HOST = '192.168.50.35' // ✅ 여기에 실제 DB 호스트 IP를 입력합니다.
        DB_NAME = 'db_dev'          // ✅ 여기에 실제 DB 스키마 이름을 입력합니다.

        // --- Jenkins Credentials를 통해 민감 정보 안전하게 불러오기 ---
        DOCKERHUB_CREDENTIALS_ID = 'yhcho-dockerhub'
        SSH_CREDENTIALS_ID = 'yhcho-ssh'
        DB_USERNAME = credentials('db-username')        // Jenkins에 생성한 Credential ID
        DB_PASSWORD = credentials('db-password')        // Jenkins에 생성한 Credential ID
        JWT_ACCESS_KEY = credentials('jwt-secret-key')  // Jenkins에 생성한 Credential ID
        JWT_REFRESH_KEY = credentials('jwt-refresh-key')// Jenkins에 생성한 Credential ID
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
                        -e SPRING_DATASOURCE_URL=jdbc:mysql://${DB_HOST}:3306/${DB_NAME}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true \
                        -e SPRING_DATASOURCE_USERNAME=${DB_USERNAME} \
                        -e SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD} \
                        -e SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MySQLDialect \
                        -e CUSTOM_JWT_ACCESSTOKEN_SECRETKEY=${JWT_ACCESS_KEY} \
                        -e CUSTOM_JWT_ACCESSTOKEN_EXPIRESECONDS=3600 \
                        -e CUSTOM_JWT_REFRESH_TOKEN_SECRETKEY=${JWT_REFRESH_KEY} \
                        -e CUSTOM_JWT_REFRESH_TOKEN_EXPIRESECONDS=604800 \
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