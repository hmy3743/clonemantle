FROM amazoncorretto:21.0.7-alpine3.21 AS builder

WORKDIR /app

# 시스템 업데이트 및 필요한 패키지 설치
RUN apk update && apk upgrade && \
    apk add --no-cache curl

# 의존성 레이어 분리 (이 파일들은 자주 변경되지 않음)
COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .

# 의존성 다운로드 (소스 코드 변경과 무관하게 캐시 활용)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사 및 빌드
COPY src src
RUN ./gradlew build -x test --no-daemon

FROM amazoncorretto:21.0.7-alpine3.21 AS runner

EXPOSE 8080

WORKDIR /app

# 보안 업데이트 적용
RUN apk update && apk upgrade && \
    # 비루트 사용자 생성
    addgroup -S appgroup && adduser -S appuser -G appgroup && \
    mkdir -p /app && \
    chown -R appuser:appgroup /app

# 애플리케이션 JAR 파일만 복사
COPY --from=builder /app/build/libs/clonemantle-0.0.1-SNAPSHOT.jar /app/application.jar

# 비루트 사용자로 전환
USER appuser

# JVM 최적화 옵션 추가
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+TieredCompilation", \
    "-XX:TieredStopAtLevel=1", \
    "-jar", "/app/application.jar"]

# 메타데이터 추가
LABEL maintainer="hmy3743@gmail.com" \
      version="0.0.1" \
      description="CloneMantle Application"