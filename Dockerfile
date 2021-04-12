FROM centos

COPY . /workspace

WORKDIR /workspace

RUN yum install -y maven

WORKDIR /workspace/build/libs

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "AF-LocationService-0.0.1-SNAPSHOT.jar" ]