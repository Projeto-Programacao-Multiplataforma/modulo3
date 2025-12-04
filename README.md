# Módulo 3 — Lambda Consumer Kafka com Docker e GitHub Actions

Este projeto tem o objetivo de criar uma **função simulando uma AWS Lambda em Java** utilizando o Spring Boot, que **escuta um tópico Kafka** e **exibe no console** a mensagem recebida.

Após o desenvolvimento da função, o projeto gera uma **imagem Docker** e a **publica automaticamente no DockerHub** através de uma **GitHub Action** configurada no repositório.

---

## Funcionalidades

* Escuta mensagens em um tópico Kafka.
* Deserializa o JSON recebido para `MessageEntity`.
* Exibe no console os dados processados.
* Possui tolerância a falhas com tentativas automáticas.
* Cria imagem Docker do projeto.
* Pipeline automatizado publica a imagem no DockerHub.

---

##  Estrutura do Projeto

* **Código da aplicação**

  * `configuration`: configurações relacionadas ao Kafka.
  * `consumer`: componente responsável por receber e processar as mensagens do tópico.
  * `entity`: representação dos dados recebidos na mensagem.

* **Gerenciamento e build**

  * `pom.xml`: define dependências, plugins e configurações do Maven.

* **Empacotamento**

  * `Dockerfile`: criação da imagem Docker da aplicação.

* **Infraestrutura**

  * `docker-compose`: inicia o Kafka e o Zookeeper para testes locais.

---

## Como funciona a "Lambda" Consumer

A classe `LambdaConsumer` ouve o tópico definido no `application.properties`:

```properties
spring.kafka.topic.modulo3=modulo3-events
spring.kafka.consumer.group-id=modulo3-group
```

Quando a mensagem chega, o projeto imprime:

```
CHAVE: <key>
MENSAGEM: <conteúdo>
```

---

## Como rodar com Docker

**1 - Inicialize Kafka via Docker Compose**

```bash
docker-compose up -d
```


**2 - Gere o executável**

```bash
mvn clean package
```

**3 - Construa a imagem Docker**

```bash
docker build -t usuario/modulo3-lambda:latest .
```

**4 - Rode a aplicação**

```bash
docker run --network="host" usuario/modulo3-lambda:latest
```

> Obs.: A network host é necessária para permitir conexão com o Kafka local (`localhost:9092`).

---

## Publicando no DockerHub com GitHub Actions

Quando um **push** é feito no branch principal, a pipeline:

1. Constrói o jar.
2. Gera a imagem Docker.
3. Faz login no DockerHub (usando secrets configurados no repositório).
4. Faz push da imagem.

---

## Testando o envio da mensagem

Com o Kafka em execução, envie uma mensagem com:

```bash
docker exec -it kafka kafka-console-producer --broker-list localhost:9092 --topic modulo3-events
```

Digite qualquer coisa como:

```
{"messageContent":"Hello Lambda!"}
```

Após issom o terminal exibe a mensagem processada:

```
CHAVE: null
MENSAGEM: MessageEntity[messageContent=Hello Lambda!]
```
