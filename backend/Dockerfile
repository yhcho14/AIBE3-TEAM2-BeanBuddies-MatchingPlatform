# =========================================
# 1. 빌드(Build) 스테이지
# =========================================
FROM eclipse-temurin:21-jdk AS builder

# 작업 디렉토리 설정
WORKDIR /workspace

# ✅ 'backend/' 접두사 제거
COPY gradlew ./
COPY gradle/ ./gradle/
COPY build.gradle.kts ./
COPY settings.gradle.kts ./

# Gradle 종속성 다운로드
RUN ./gradlew dependencies

# ✅ 'backend/' 접두사 제거
COPY src ./src

# 애플리케이션 빌드
RUN ./gradlew build -x test


# =========================================
# 2. 최종 실행(Final) 스테이지
# =========================================
FROM eclipse-temurin:21-jre

WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# ✅ '/workspace/backend'에서 '/workspace'로 경로 수정
COPY --from=builder /workspace/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]