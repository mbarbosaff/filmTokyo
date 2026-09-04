🎬 FilmoTokio

Aplicação web Full Stack para gestão e descoberta de filmes — Projeto Final do curso de Desenvolvimento Full Stack Java da Tokio School.

Permite a administradores gerir um catálogo de filmes e pessoas associadas (realizadores, atores, argumentistas, músicos, fotógrafos), e a utilizadores registados pesquisar filmes, consultar fichas de detalhe e deixar reviews e pontuações — tanto através de uma aplicação web tradicional como de uma API REST protegida por JWT.

Java Spring Boot MySQL Maven License

Índice
Funcionalidades
Arquitetura e stack
Modelo de dados
Pré-requisitos
Configuração e arranque
Documentação da API
Endpoints principais
Processo batch
Segurança
Estrutura do projeto
Testes
Roadmap
Autor
Licença
Funcionalidades

Aplicação Web

Registo e autenticação de utilizadores (Spring Security, passwords com hash BCrypt)
Gestão administrativa de filmes e pessoas (ROLE_ADMIN)
Listagem, pesquisa por título e ficha de detalhe de cada filme
Reviews e pontuações (1–5) por utilizadores autenticados, com média calculada
Indicador de sessão em tempo real na interface

API REST

Autenticação stateless via JWT (/api/auth/login)
Endpoints públicos de consulta (filmes, pessoas) e protegidos de escrita (reviews, scores)
DTOs dedicados — as entidades JPA nunca são expostas diretamente
Documentação interativa via Swagger / OpenAPI

Processo Batch

Exportação diária automática (agendada) dos filmes por migrar para CSV
Disparo manual via endpoint administrativo, para testes e execuções fora de agenda
Execução idempotente — filmes já exportados não são reprocessados
Arquitetura e stack
Componente	Tecnologia
Linguagem / JDK	Java 17 (Amazon Corretto)
Framework	Spring Boot 2.6.1
Segurança	Spring Security 5.6.0 — form login (web) + JWT (API)
Persistência	Spring Data JPA / Hibernate 5.6.1.Final
Base de dados	MySQL 8
Templates	Thymeleaf + thymeleaf-extras-springsecurity5
Processamento em lote	Spring Batch
Documentação da API	springdoc-openapi-ui 1.6.4 (Swagger)
Tokens	JJWT 0.9.1
Build	Maven

A aplicação segue uma arquitetura em camadas: Controller (web e REST) → Service / Security → Repository (Spring Data JPA) → MySQL. A camada de API expõe DTOs mapeados manualmente a partir das entidades, evitando ciclos de serialização nas relações bidirecionais.

Modelo de dados
User ──< Role (N:N)
User ──< Review, Score (1:N)

Film ──> Person (director, photographer)      [N:1]
Film ──< Person (actors, musicians,
                 screenwriters)                [N:N]
Film ──< Review, Score                         [1:N]

Person.type: DIRECTOR | ACTOR | GUIONISTA | MUSICO | FOTOGRAFO

Film inclui ainda os campos migrate e dateMigrate, usados pelo processo batch para controlar quais os registos já exportados.

Pré-requisitos
JDK 17
Maven 3.6+
MySQL 8 em execução localmente (ou acessível pela datasource.url)
Configuração e arranque
1. Base de dados

Cria a base de dados vazia (o schema é gerado automaticamente pelo Hibernate):

sql
CREATE DATABASE filmotokio;
2. Variável de ambiente da password

A password da base de dados nunca é gravada em texto simples no repositório. É lida a partir da variável de ambiente DB_PASSWORD:

bash
# macOS / Linux
export DB_PASSWORD="a-tua-password"

# ou, para persistir entre sessões no macOS:
launchctl setenv DB_PASSWORD "a-tua-password"
powershell
# Windows (PowerShell)
$env:DB_PASSWORD = "a-tua-password"
3. Clonar e compilar
bash
git clone https://github.com/mbarbosaff/filmTokyo.git
cd filmTokyo/filmotokio
mvn clean install
4. Arrancar a aplicação
bash
mvn spring-boot:run

A aplicação fica disponível em http://localhost:8080.

5. Utilizador administrador inicial

O primeiro utilizador ROLE_ADMIN e os roles base (ROLE_ADMIN, ROLE_USER) devem ser inseridos manualmente na primeira execução (ver src/main/resources/script.sql como referência). Depois do arranque inicial, o registo de novos utilizadores fica disponível em /registo (atribuídos automaticamente a ROLE_USER).

Documentação da API

Com a aplicação em execução, a documentação interativa está disponível em:

http://localhost:8080/swagger-ui/index.html

Inclui suporte a autenticação Bearer JWT diretamente na interface (botão Authorize).

Endpoints principais
Método	Endpoint	Acesso	Descrição
POST	/api/auth/login	Público	Autentica e devolve o token JWT
GET	/api/films	Público	Lista todos os filmes
GET	/api/films/{id}	Público	Detalhe do filme (reviews + média)
GET	/api/films/search?title=	Público	Pesquisa por título
POST	/api/films/{id}/review	Autenticado	Cria uma review
POST	/api/films/{id}/score	Autenticado	Cria uma pontuação (1–5)
GET	/api/persons	Público	Lista todas as pessoas
GET	/api/persons/{id}	Público	Detalhe de uma pessoa
POST	/api/admin/export-films	ROLE_ADMIN	Dispara manualmente o processo batch

Exemplo — autenticar e chamar um endpoint protegido:

bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"tokioschool","password":"..."}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")

curl -X POST http://localhost:8080/api/films/1/review \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Excelente","textReview":"Recomendo vivamente."}'
Processo batch

Exporta os filmes com migrate=false para exports/films_export_<data>.csv, marcando-os de seguida como migrados (migrate=true, dateMigrate=<data>).

Agendamento automático: todos os dias às 03:00 (@Scheduled(cron = "0 0 3 * * *"))
Disparo manual: POST /api/admin/export-films (requer token JWT com ROLE_ADMIN)
Idempotência: execuções repetidas não reexportam filmes já migrados
Segurança
Password da base de dados externalizada via variável de ambiente — nunca em texto simples no repositório
Passwords de utilizador com hash BCrypt
Tokens JWT assinados (HS512), com validade de 24 horas
Duas cadeias de segurança independentes: API REST (stateless, JWT) e aplicação Web (sessão + form login), coexistindo sem interferência
Autorização por papel (ROLE_ADMIN / ROLE_USER) aplicada tanto na Web como na API
Estrutura do projeto
src/main/java/es/tokioschool/filmotokio/
├── batch/          # ItemProcessor e agendamento do Spring Batch
├── config/         # SecurityConfig, BatchConfig, OpenApiConfig, ModelMapperConfig
├── controller/      # Controllers web (MVC) e REST
├── dto/             # DTOs e mappers da API
├── model/           # Entidades JPA
├── repository/       # Repositórios Spring Data JPA
├── security/         # JwtUtil, JwtRequestFilter
└── service/          # UserDetailsServiceImpl

src/main/resources/
├── templates/        # Views Thymeleaf
└── application.properties
Testes

Validação manual end-to-end cobrindo os três fluxos principais:

Registo → login → logout, com verificação do indicador de sessão
CRUD administrativo de Person e Film, pesquisa e ficha de detalhe
Autenticação JWT, acesso aos endpoints protegidos e disparo do processo batch (com confirmação de idempotência)
Roadmap
 Testes automatizados (unitários e de integração)
 Paginação nos endpoints de listagem
 Upload de imagens (posters) em vez de campo de texto
 Interface de administração para consultar histórico de exportações batch
Autor

José Miguel Figueiredo Projeto Final — Desenvolvimento Full Stack Java, Tokio School

Licença

Projeto académico desenvolvido para fins de avaliação e certificação. Uso livre para fins de estudo e referência.
