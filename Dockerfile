FROM eclipse-temurin:21 AS builder

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew --no-daemon installDist

FROM eclipse-temurin:21

WORKDIR /user/app
EXPOSE 8080

COPY --from=builder build/install/airlanding ./

ENTRYPOINT ["/user/app/bin/airlanding"]