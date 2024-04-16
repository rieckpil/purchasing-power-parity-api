FROM maven:3.9.6-sapmachine-21 as build

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
WORKDIR /opt/demo
COPY . .
RUN mvn package -Dmaven.test.skip=true -Dspotless.check.skip --no-transfer-progress

FROM sapmachine:21.0.2-jre-ubuntu
WORKDIR /opt/demo
COPY --from=build /opt/demo/target/app.jar /opt/demo
ENTRYPOINT ["java","-jar","/opt/demo/app.jar"]
