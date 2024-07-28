FROM eclipse-temurin:21 as builder

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew --no-daemon installDist

FROM eclipse-temurin:21

WORKDIR /user/app

COPY --from=builder build/install/airlanding ./

ENTRYPOINT ["/user/app/bin/airlanding"]