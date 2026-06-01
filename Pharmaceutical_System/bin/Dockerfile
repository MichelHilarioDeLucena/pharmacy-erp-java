FROM tomcat:9.0-jdk17-temurin

# Remove aplicações padrão
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia WAR do Maven
COPY target/erp.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
