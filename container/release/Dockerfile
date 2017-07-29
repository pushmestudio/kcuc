# Based on Tocmat with alpine image
# If something goes wrong(e.g. tomcat version is expired), you should check the following link:
# https://github.com/docker-library/tomcat/blob/master/8.5/jre8-alpine/Dockerfile
# {@code docker build -t registry.gitlab.com/pushmestudio/kcuc:latest .}
# For making faster build, prepare the tomcat base and add a war file for release
FROM registry.gitlab.com/pushmestudio/kcuc:base
MAINTAINER tkhm PushMe Studio

# Remove all files under webapps
RUN rm -rf webapps/*

# Add original kcuc app
ADD build/libs/kcuc.war webapps/ROOT.war

# Set the cloudant env variables
ARG CLOUDANT_ACCOUNT
ARG CLOUDANT_USER
ARG CLOUDANT_PW
ENV CLOUDANT_ACCOUNT "${CLOUDANT_ACCOUNT}"
ENV CLOUDANT_USER "${CLOUDANT_USER}"
ENV CLOUDANT_PW "${CLOUDANT_PW}"

EXPOSE 8080
CMD ["catalina.sh", "run"]
