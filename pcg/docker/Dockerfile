FROM bellsoft/liberica-openjdk-alpine-musl:21

LABEL maintainer=peter.paul@perfana.nl

COPY app.jar /app.jar

COPY files /files

RUN apk add --no-cache bash

RUN apk add --no-cache curl && apk add --no-cache zip
RUN curl -s "https://get.sdkman.io" | bash
RUN bash -c "source \"$HOME/.sdkman/bin/sdkman-init.sh\" \
  && sdk install kotlin \
  && sdk install kscript"
RUN chmod u+x /files/generate-config.sh 
  
CMD ["java", "-jar", "app.jar"]
