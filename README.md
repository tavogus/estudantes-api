# API de Monitoramento de Frequência Escolar

## Objetivo
Esta API foi desenvolvida para monitorar a frequência escolar de alunos bolsistas ou de baixa renda, permitindo um acompanhamento mais eficiente de sua presença nas aulas. O sistema permite:

- Cadastro de escolas
- Cadastro de alunos (bolsistas ou de baixa renda)
- Registro de frequência diária
- Geração de relatórios de frequência por período
- Acompanhamento individual de cada aluno
- Verificação de alerta por excesso de faltas

## Requisitos
- Java 21
- Maven
- Docker e Docker Compose
- PostgreSQL

## Configuração do Ambiente

1. Clone o repositório:
```bash
git clone [url-do-repositorio]
cd estudantes-api
```

2. Inicie o banco de dados PostgreSQL usando Docker Compose:
```bash
docker-compose up -d
```

3. Compile e execute a aplicação:
```bash
mvn clean install
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

## Endpoints da API

### Escolas

#### Criar Escola
- **POST** `/api/escolas`
- **Body**:
```json
{
    "nome": "Escola Municipal São Paulo",
    "endereco": "Rua das Flores, 123",
    "telefone": "(11) 1234-5678"
}
```

#### Buscar Escola por ID
- **GET** `/api/escolas/{id}`

#### Listar Todas as Escolas
- **GET** `/api/escolas`
- **Query Parameters**:
  - `nome`: (opcional) Filtra escolas por nome (case-insensitive)
- **Exemplo**: `/api/escolas?nome=São Paulo`

#### Atualizar Escola
- **PUT** `/api/escolas/{id}`
- **Body**:
```json
{
    "nome": "Escola Municipal São Paulo Atualizada",
    "endereco": "Rua das Flores, 123",
    "telefone": "(11) 1234-5678"
}
```

#### Deletar Escola
- **DELETE** `/api/escolas/{id}`

### Alunos

#### Criar Aluno
- **POST** `/api/alunos`
- **Body**:
```json
{
    "nome": "João Silva",
    "cpf": "12345678901",
    "dataNascimento": "2005-01-01",
    "endereco": "Rua das Árvores, 456",
    "telefone": "(11) 98765-4321",
    "tipoBeneficio": "BAIXA_RENDA",
    "escolaId": 1
}
```

#### Buscar Aluno por ID
- **GET** `/api/alunos/{id}`

#### Listar Todos os Alunos
- **GET** `/api/alunos`
- **Response**: Lista de todos os alunos cadastrados no sistema

#### Listar Alunos por Escola
- **GET** `/api/alunos/escola/{escolaId}`
- **Query Parameters**:
  - `nome`: (opcional) Filtra alunos por nome (case-insensitive)
  - `cpf`: (opcional) Filtra alunos por CPF
  - `tipoBeneficio`: (opcional) Filtra alunos por tipo de benefício (BAIXA_RENDA ou BOLSISTA)
- **Exemplo**: `/api/alunos/escola/1?nome=João&tipoBeneficio=BAIXA_RENDA`

#### Atualizar Aluno
- **PUT** `/api/alunos/{id}`
- **Body**: Mesmo formato do POST

#### Deletar Aluno
- **DELETE** `/api/alunos/{id}`

### Frequências

#### Registrar Frequência
- **POST** `/api/frequencias`
- **Body**:
```json
{
    "alunoId": 1,
    "data": "2024-03-20",
    "presente": true,
    "observacao": "Aluno presente em todas as aulas"
}
```

#### Buscar Frequências por Aluno
- **GET** `/api/frequencias/aluno/{alunoId}`

#### Buscar Frequências por Período
- **GET** `/api/frequencias/aluno/periodo?dataInicio=2024-03-01&dataFim=2024-03-31`
- **Query Parameters**:
  - `alunoId`: (opcional) ID do aluno para filtrar frequências
  - `dataInicio`: Data inicial do período (formato: YYYY-MM-DD)
  - `dataFim`: Data final do período (formato: YYYY-MM-DD)
- **Exemplo**: 
  - Buscar todas as frequências: `/api/frequencias/aluno/periodo?dataInicio=2024-03-01&dataFim=2024-03-31`
  - Buscar frequências de um aluno: `/api/frequencias/aluno/periodo?alunoId=1&dataInicio=2024-03-01&dataFim=2024-03-31`

#### Verificar Alerta de Faltas
- **GET** `/api/frequencias/aluno/{alunoId}/verificar-faltas?data=2024-03-20`
- **Response**: Retorna `true` se o aluno tiver mais de 15 faltas no mês, `false` caso contrário
- **Observação**: Este endpoint também atualiza automaticamente o campo `alerta` do aluno

#### Atualizar Frequência
- **PUT** `/api/frequencias/{id}`
- **Body**:
```json
{
    "presente": false,
    "observacao": "Aluno faltou por motivo de saúde"
}
```

#### Deletar Frequência
- **DELETE** `/api/frequencias/{id}`

## Acessando o Banco de Dados

Para acessar o banco de dados PostgreSQL, use o comando:
```bash
docker exec -it estudantes_db psql -U postgres -d estudantes_db
```

Senha: `postgres`

## Testando a API

Você pode importar a collection do Postman localizada em `postman/estudantes-api.postman_collection.json` para testar todos os endpoints da API.

## Fluxo de Uso

1. Cadastre uma escola
2. Cadastre os alunos (indicando se são bolsistas ou de baixa renda)
3. Registre a frequência diária dos alunos
4. Consulte os relatórios de frequência por aluno ou por período
5. Verifique o status de alerta de faltas dos alunos

## Validações

- CPF deve ser único por aluno
- Data de nascimento deve ser no passado
- Tipo de benefício deve ser "BAIXA_RENDA" ou "BOLSISTA"
- Campos obrigatórios são validados
- Relacionamentos entre entidades são mantidos (aluno pertence a uma escola, frequência pertence a um aluno)
- Alerta é atualizado automaticamente quando um aluno tem mais de 15 faltas no mês

## Buscas e Filtros

A API implementa buscas flexíveis com as seguintes características:

- **Escolas**:
  - Busca por nome (case-insensitive)
  - Suporte a busca parcial (ex: "São" encontra "São Paulo")
  - Parâmetro opcional (retorna todas as escolas se não especificado)

- **Alunos**:
  - Busca por nome (case-insensitive)
  - Busca por CPF
  - Filtro por tipo de benefício
  - Todos os parâmetros são opcionais
  - Combinação de filtros suportada
  - Busca por escola obrigatória

As buscas são otimizadas para PostgreSQL e utilizam queries parametrizadas para segurança e performance. 