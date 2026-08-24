# Payment Microservice

Microserviço de processamento de transações com Pub/Sub, Storage e Segurança.

## Funcionalidades

- ✅ API de criação de pagamento com idempotência
- ✅ Publicação de eventos em Google Cloud Pub/Sub
- ✅ Consumo de eventos para processamento
- ✅ Armazenamento de comprovantes em Google Cloud Storage
- ✅ APIs seguras com JWT/OAuth2
- ✅ Idempotência, observabilidade e tratamento robusto de erros
- ✅ Trilha de auditoria via AOP

## API de Criação de Pagamento

**Entrada:**
- `idempotencyKey`: String (obrigatório)
- `pagadorId`: UUID (obrigatório)
- `valor`: BigDecimal > 0 (obrigatório)
- `moeda`: ISO-4217 (obrigatório)
- `metodo`: CARTAO|CONTA (obrigatório)
- `descricao`: String (opcional)
- `anexos`: Base64 (opcional)

**Saída:**
- `paymentId`: UUID
- `status`: CRIADO|PROCESSANDO|CONFIRMADO|RECUSADO
- `createdAt`: LocalDateTime

## Endpoints
POST /payments           # Cria pagamento (idempotent)
GET /payments/{id}       # Consulta por ID
GET /payments            # Busca paginada com filtros
POST /auth/token         # Obtém token JWT
## Segurança

### Roles
- `ROLE_ADMIN`: Acesso total
- `ROLE_ANALYST`: Leitura/consulta
- `ROLE_SERVICE`: Criação de pagamentos

### Políticas
- `POST /payments` → ROLE_SERVICE ou ROLE_ADMIN
- `GET /payments/**` → ROLE_ANALYST ou ROLE_ADMIN

## Configuração

### Emuladores (para desenvolvimento)

```bash
# Pub/Sub Emulator
gcloud beta emulators pubsub start --host-port=localhost:8085

# Storage Emulator  
gcloud beta emulators storage start --host-port=localhost:8080