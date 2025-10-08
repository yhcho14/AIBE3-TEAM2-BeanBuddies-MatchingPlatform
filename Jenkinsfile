// Jenkinsfile (전체 수정본)

pipeline {
    agent any // 파이프라인을 실행할 Jenkins 에이전트 지정

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
                    // Dockerfile을 사용하여 이미지를 빌드합니다. (Dockerfile은 backend 폴더 내에 위치해야 함)
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
                // withCredentials 블록으로 Jenkins에 등록된 Secret text를 환경 변수로 안전하게 주입합니다.
                withCredentials([
                    string(credentialsId: 'your-db-host', variable: 'DB_URL_SECRET'),
                    string(credentialsId: 'db-username', variable: 'DB_USERNAME_SECRET'),
                    string(credentialsId: 'db-password', variable: 'DB_PASSWORD_SECRET'),
                    string(credentialsId: 'jwt-secret-key', variable: 'JWT_ACCESS_KEY_SECRET'),
                    string(credentialsId: 'jwt-refresh-key', variable: 'JWT_REFRESH_KEY_SECRET')
                ]) {
                    // sshagent 블록으로 SSH 키를 사용하여 배포 서버에 접속합니다.
                    sshagent(['yhcho-ssh']) {
                        // 하나의 SSH 세션에서 모든 배포 명령을 실행하여 효율성을 높입니다.
                        sh """
                            ssh -o StrictHostKeyChecking=no yhcho@192.168.50.35 <<'EOF'
                                # 기존 컨테이너가 있다면 중지하고 삭제합니다.
                                docker stop ${APP_NAME} || true && docker rm ${APP_NAME} || true

                                # 새로 빌드된 Docker 이미지를 가져옵니다.
                                docker pull ${DOCKERHUB_USERNAME}/${APP_NAME}:${env.BUILD_NUMBER}

                                # 환경 변수를 주입하여 새 컨테이너를 실행합니다.
                                docker run -d --name ${APP_NAME} -p 8080:8080 \\
                                -e SPRING_PROFILES_ACTIVE=prod \\
                                -e SPRING_DATASOURCE_URL=${DB_URL_SECRET} \\
                                -e SPRING_DATASOURCE_USERNAME=${DB_USERNAME_SECRET} \\
                                -e SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD_SECRET} \\
                                -e SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.MySQLDialect \\
                                -e CUSTOM_JWT_ACCESSTOKEN_SECRETKEY=${JWT_ACCESS_KEY_SECRET} \\
                                -e CUSTOM_JWT_ACCESSTOKEN_EXPIRESECONDS=3600 \\
                                -e CUSTOM_JWT_REFRESHTOKEN_SECRETKEY=${JWT_REFRESH_KEY_SECRET} \\
                                -e CUSTOM_JWT_REFRESHTOKEN_EXPIRESECONDS=604800 \\
                                ${DOCKERHUB_USERNAME}/${APP_NAME}:${env.BUILD_NUMBER}
                            'EOF'
                        """
                    }
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