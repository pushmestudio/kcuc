# 基本的にTomcat with alpineのイメージのDockerfileを改変して使用しているので
# 動かなくなった場合(e.g. Tomcatのリンク切れ)には次を参照する
# https://github.com/docker-library/tomcat/blob/master/8.5/jre8-alpine/Dockerfile
# この設定ファイルはビルドが不要で且つTomcatが必要な環境に用いる
# {@code docker build -t registry.gitlab.com/pushmestudio/kcuc .}
# ビルド時間短縮のため、事前にWarを加える直前まで作成し、後からWarを加えてリリース用のコンテナに仕上げる
FROM registry.gitlab.com/pushmestudio/kcuc:base
MAINTAINER tkhm PushMe Studio

# Remove
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
