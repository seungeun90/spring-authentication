# 기본 이미지 설정
FROM openjdk:17-alpine

# 패키지 설치
RUN apk update && \
    apk add openssl busybox-extras curl tzdata mysql-client py-pip && \
    apk add --no-cache fontconfig ttf-dejavu && \
    pip install --no-cache-dir awscli && \
    rm -rf /var/cache/apk/*

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 작업 디렉토리 설정 및 파일 복사
WORKDIR /app
COPY . .

# 기본 실행 명령어 설정
CMD ["java", "-jar", "client-boot/build/libs/client-boot-1.0.0-SNAPSHOT.jar"]