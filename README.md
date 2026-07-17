# Gateway de Pagamento PayFlow


<!-- Tecnologias / Logos -->

[![Java](https://img.shields.io/badge/Java-21-orange?logo=java&logoColor=white)](https://www.java.com/)
[![Spring](https://img.shields.io/badge/Spring-Boot-brightgreen?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8-blue?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3-orange?logo=rabbitmq&logoColor=white)](https://www.rabbitmq.com/)


## Resumo das regras de negócio

PayFlow é um gateway de pagamento simplificado que expõe APIs REST para processamento assíncrono de transações, gerenciamento de lojas e configuração de taxas. Fluxos principais:

- Autenticação: lojas se autentican via `/api/payflow/auth/login` gerando token que é usado em endpoints protegidos.
- Tokenização: valida cartão e retorna um token/resultado via `/v1/transaction/tokenrization`. Há um mecanismo de `VelocityCheck` para limitar requisições por cartão/token.
- Criação de transação: o endpoint de criação (`POST /api/payflow/transaction`) recebe uma transação (DTO) e a envia para processamento assíncrono via RabbitMQ. A resposta imediata é 202 (processando).
- Atualização de status e webhook: o status de uma transação pode ser atualizado via `/api/payflow/transaction/status/{id}`, e após alteração o sistema envia notificação ao webhook da loja (via `ChangeStatusService`).
- Captura: existe um endpoint específico para capturas que envia uma mensagem para uma fila de captura (`/api/payflow/{token}/capture`).
- Bloqueio/Liberação: permite criar bloqueios de transações (endpoint de blockade) para operações de segurança/fraude.
- Gestão de lojas e configuração de taxas: CRUD de `Store` e configuração de `FeesConfig` por loja.

Observações arquiteturais:
- Processamento de transações é assíncrono: as requisições de criação/capture colocam mensagens em exchanges RabbitMQ para workers processarem.
- Controllers mantêm pouca lógica; a maior parte das regras está em services, mappers e usecases (por exemplo, `VelocityCheck`).


## Endpoints

Abaixo um inventário dos endpoints expostos agrupados por controller (método, path, entrada e saída resumidas):

- AuthController
  - POST `/api/payflow/auth/login`
    - Request: AuthDto (email, password)
    - Response: Store (contém token no campo `token`)
    - Propósito: autenticar loja e devolver um token de uso nos headers `Authorization`.

- TokenrizationController
  - POST `/v1/payflow/tokenrization`
    - Request: TokenrizationDto
    - Headers: `Authorization: Bearer <token>` (opcional, usado para limites por loja)
    - Response: TokenrizationResponseDto
    - Propósito: validação/tokenização de cartão; dispara `VelocityCheck` caso necessário e pode lançar erro se ultrapassar limite de requisições.

- TransactionController
  - POST `/api/payflow/transaction` (consumes application/json)
    - Request: TransactionDto (informações da transação)
    - Headers: `Authorization: Bearer <token>` (opcional — quando presente, token é anexado ao DTO)
    - Behavior: envia o DTO para RabbitMQ (exchange/route definidos em `AutorizationMQConfig`) e retorna 202
    - Response: { status: processing, message: ... }
  - GET `/api/payflow/transaction/{id}`
    - Response: TransactionResponse
  - GET `/api/payflow/transaction` (paginated)
    - Query: parâmetros de `Pageable`
    - Response: Page<TransactionResponse>
  - PUT `/api/payflow/transaction/status/{id}`
    - Request: TransactionStatusDto (ex.: status)
    - Headers: `Authorization: Bearer <token>` (opcional — usado para localizar store e webhook)
    - Behavior: atualiza status da transação, recupera webhook da loja e envia notificação via `ChangeStatusService`. Retorna 202.

- CaptureController
  - POST `/api/payflow/{token}/capture`
    - Path: token (identificador de autorização/token da transação)
    - Request: CaptureDto
    - Behavior: constrói `CaptureApiDto` e envia para RabbitMQ (exchange/route em `CaptureMQConfig`). Retorna 202.

- BlockadeController
  - POST `/api/payflow/blockade`
    - Headers: `Authorization: Bearer <token>`
    - Request: BlockadeDto
    - Response: Blockade (201)
    - Propósito: criar um bloqueio relacionado à loja/conta (ex.: medidas antifraude ou reversão temporária).

- StoreController
  - POST `/api/payflow/store` — criar loja (StoreDto)
  - PUT `/api/payflow/store/{id}` — atualizar loja (StoreDto)
  - GET `/api/payflow/store/{id}` — obter loja por id
  - GET `/api/payflow/store` — lista todas as lojas
  - DELETE `/api/payflow/store/{id}` — remove loja

- FeesConfigController
  - POST `/api/payflow/store/config` — criar configuração de fees (FeesConfigDto) (201)
  - PUT `/api/payflow/store/config/{id}` — atualizar configuration (201)
  - GET `/api/payflow/store/config` — obter configuração baseada no token enviado no header `Authorization`


## AutorizationMQConfig / CaptureMQConfig (para que servem)

- Classes como `AutorizationMQConfig` e `CaptureMQConfig` definem constantes (exchange, routing key) e possivelmente beans relacionados ao RabbitMQ. Elas existem para centralizar as configurações de rota de mensagens entre produtor (API) e consumidores (workers).
- Em `TransactionController` e `CaptureController` o código usa essas constantes para `rabbitTemplate.convertAndSend(exchange, routingKey, payload)`.


## Bugs conhecidos e recomendações de correção

1) Erro: "SimpleMessageConverter only supports String, byte[] and Serializable payloads, received: com.flow.payflow.dto.TransactionDto"
   - Causa: `RabbitTemplate` por padrão usa `SimpleMessageConverter`, que não converte objetos Java para JSON automaticamente.
   - Solução recomendada: registrar um bean `Jackson2JsonMessageConverter` e configurá-lo para o `RabbitTemplate`. Exemplo (adicionar em uma classe de configuração Spring):

```java
// Exemplo de bean a ser adicionado em uma configuração RabbitMQ
@Bean
public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
}

@Bean
public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                     Jackson2JsonMessageConverter converter) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(converter);
    return template;
}
```

   - Alternativa: serializar o DTO manualmente para JSON antes de enviar (não recomendado). Usar `Jackson2JsonMessageConverter` é a prática padrão.

2) Erro de injeção: "Parameter 0 of constructor in com.flow.payflow.service.impl.TokenrizationServiceImpl required a bean of type 'org.springframework.web.reactive.function.client.WebClient' that could not be found."
   - Causa: `TokenrizationServiceImpl` espera um `WebClient` injetado (para chamar APIs externas), mas não existe bean definido.
   - Solução: declarar um bean `WebClient` (por exemplo, em uma classe `@Configuration`):

```java
@Bean
public WebClient webClient(WebClient.Builder builder) {
    return builder.build();
}
```

   - Se a chamada externa tem configuração por ambiente (baseUrl, timeouts), use `@ConfigurationProperties` para tipar essas propriedades por profile.

3) Assunto assíncrono — "na linha 44 na chamada ao rabbit, como vou ter o valor se der erro na transacao?"
   - Contexto: o endpoint posta a transação na fila e responde imediatamente (202). Se o processamento do worker falhar, a resposta HTTP original já foi enviada.
   - Recomendações:
     - Use correlationId / messageId para rastrear mensagens e retornar links de consulta (p.ex. fornecer um transactionId no corpo da resposta) para que o cliente consulte o status posteriormente (`GET /api/payflow/transaction/{id}`).
     - Configure Dead Letter Exchanges (DLX) e políticas de retry nos workers para lidar com falhas.
     - Considere um fluxo síncrono opcional ou um endpoint de callback/webhook se o cliente precisar da confirmação imediata.

4) Observações de segurança e validação
   - Valide e trate `Optional` com segurança (evitar `Optional.get()` sem `isPresent()` como no `AuthController` que pode lançar `NoSuchElementException`). Prefira `orElseThrow(() -> new UsernameNotFoundException(...))`.
   - Padronize respostas de erro (por exemplo, através de `@ControllerAdvice`) para que clientes recebam mensagens e códigos consistentes.


## Como executar ( rápida )

- Build e testes (maven wrapper):

```bash
./mvnw clean package
./mvnw test
```

- Executar a aplicação localmente:

```bash
./mvnw spring-boot:run
```
