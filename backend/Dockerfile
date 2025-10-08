# =========================================
# 1. 빌드(Build) 스테이지
# =========================================
# Java 21과 Gradle을 포함한 이미지에서 빌드를 시작합니다.
FROM eclipse-temurin:21-jdk AS builder

# 작업 디렉토리 설정
WORKDIR /workspace/backend

# Gradle 래퍼 파일들을 먼저 복사하여 종속성 캐싱을 활용합니다.
COPY backend/gradlew ./
COPY backend/gradle/ ./gradle/
COPY backend/build.gradle.kts ./
COPY backend/settings.gradle.kts ./

# Gradle 종속성을 다운로드합니다. 소스 코드가 변경되어도 이 레이어는 캐시될 수 있습니다.
RUN ./gradlew dependencies

# 나머지 소스 코드를 복사합니다.
COPY backend/src ./src

# Gradle을 사용하여 애플리케이션을 빌드합니다. 테스트는 건너뜁니다.
RUN ./gradlew build -x test


# =========================================
# 2. 최종 실행(Final) 스테이지
# =========================================
# 더 가벼운 Java 21 JRE(Java Runtime Environment) 이미지를 기반으로 최종 이미지를 만듭니다.
FROM eclipse-temurin:21-jre

# 작업 디렉토리 설정
WORKDIR /app

# 보안을 위해 별도의 사용자를 생성하고 전환합니다.
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# 빌드 스테이지에서 생성된 실행 가능한 JAR 파일만 복사합니다.
# 경로의 'your-project-name-*.jar' 부분은 실제 생성되는 JAR 파일 이름에 맞게 수정될 수 있습니다.
COPY --from=builder /workspace/backend/build/libs/*.jar app.jar

# 애플리케이션이 사용할 포트를 외부에 알립니다.
EXPOSE 8080

# 컨테이너가 시작될 때 JAR 파일을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]