# ---- Estágio 1: Build (Construção) ----
# Usa uma imagem do Maven com Java 17 para "buildar" o projeto
FROM maven:3.8-openjdk-17 AS build

# Define o diretório de trabalho dentro da "caixa"
WORKDIR /app

# Copia os arquivos de configuração do Maven e o pom.xml
COPY .mvn/ .mvn
COPY mvnw .
COPY pom.xml .

# Baixa as dependências (isso otimiza o cache)
RUN mvn dependency:go-offline

# Copia o resto do código-fonte (src)
COPY src ./src

# Roda o comando do Maven para criar o .jar
RUN mvn clean package -DskipTests


# ---- Estágio 2: Run (Execução) ----
# Usa uma imagem "slim" (leve) do Java 17 apenas para rodar
FROM openjdk:17-slim-bullseye

WORKDIR /app

# IMPORTANTE: Mude o nome do .jar aqui se o seu for diferente!
# Copia o .jar do estágio "build" para a imagem final
COPY --from=build /app/target/todolist-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta 8080 (que o Spring usa por padrão)
EXPOSE 8080

# O comando para iniciar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]