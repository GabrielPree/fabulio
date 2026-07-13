<p align="center">
  <img src="img/logo.png" alt="Logo Fabulio" width="220">
</p>

<p align="center">

Backend desenvolvido em <strong>Java Spring Boot</strong> responsável por toda a lógica de negócio da plataforma de streaming <strong>Fabulio</strong>.

A API gerencia filmes, séries, temporadas e episódios, além de integrar diversas APIs externas para construir automaticamente uma base de dados completa.

</p>

<p align="center">

<img src="https://img.shields.io/badge/Java-25-red?style=for-the-badge&logo=openjdk">

<img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">

<img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white">

<img src="https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white">

<img src="https://img.shields.io/badge/JPA-Hibernate-orange?style=for-the-badge">

</p>

<p align="center">
  <a href="https://gabrielpree.github.io/fabulioFrontEnd">
    <img src="https://img.shields.io/badge/%20Acessar%20o%20Site-f5c16c?style=for-the-badge" alt="Site">
  </a>

  <a href="https://github.com/GabrielPree/fabulio-frontend">
    <img src="https://img.shields.io/badge/%20Frontend-5a2ea6?style=for-the-badge" alt="Frontend">
  </a>
</p>

---

# Sobre

O **Fabulio API** é o backend da plataforma de streaming **Fabulio**.

Seu principal objetivo é fornecer todas as informações consumidas pelo frontend através de uma API REST desenvolvida com **Spring Boot**.

Além das funcionalidades da API, o projeto possui uma ferramenta em modo console capaz de popular automaticamente todo o banco de dados utilizando APIs externas.

Foi desenvolvido durante a carreira de Java da **Alura**.

---

# Principais Recursos

- Cadastro de Filmes

- Cadastro de Séries

- Cadastro de Temporadas

- Cadastro de Episódios

- Busca de informações em APIs externas

- Tradução automática de sinopses

- Integração com PostgreSQL

- API REST

- Tratamento global de exceções

- Arquitetura em camadas

---

# Tecnologias

## Backend

- Java 25
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven

## Banco

- PostgreSQL

## APIs Externas

### OMDb API

Responsável por:

- Informações gerais
- Poster dos filmes
- Dados das séries

---

### FanArt.tv API

Responsável por:

- Logos
- Backgrounds
- Artes em alta resolução

---

### TMDB API

Utilizada para converter:

```
IMDb ID
      ↓
 TMDB TV ID
```

Esse identificador é necessário para buscar imagens de séries na FanArt API.

---

### Groq API

Modelo utilizado:

```
llama-3.1-8b-instant
```

Responsável pela tradução automática das sinopses para português.

---

# Arquitetura

```
                 APIs Externas

        OMDb
          │
FanArt ───┼────► Spring Boot
          │
TMDB ─────┤
          │
Groq ─────┘

              │

              ▼

         Camada Service

              │

              ▼

      Spring Data JPA

              │

              ▼

          PostgreSQL

              │

              ▼

      Controllers REST

              │

              ▼

     Frontend Fabulio
```

---

# Estrutura do Projeto

```
config
│
controller
│
dto
│
exception
│
model
│
repository
│
service
│
resources
```

Cada camada possui uma responsabilidade específica, seguindo a arquitetura em camadas utilizada em aplicações Spring Boot.

---

# Interface de Administração

O projeto possui uma aplicação em modo console chamada:

```
FabulioApplicationSemWeb.java
```

Ela permite administrar o banco de dados através de um menu interativo.

```
1  - Adicionar Filme
2  - Adicionar Série
3  - Adicionar Episódios
4  - Listar Filmes
5  - Listar Séries
6  - Adicionar Lista de Filmes
7  - Adicionar Lista de Séries
8  - Buscar imagens FanArt (Filmes)
9  - Buscar imagens FanArt (Séries)
10 - Remover Filme
11 - Remover Série
0  - Sair
```

---

# Endpoints

A API disponibiliza endpoints REST para consumo pelo frontend.

Exemplos:

```
GET /filmes

GET /series

GET /series/{id}

GET /series/{id}/temporadas

GET /series/{id}/temporadas/{numero}

GET /filmes/{id}
```

---

# Como executar

## Clone

```bash
git clone https://github.com/GabrielPree/fabulio.git
```

---

## Configure as variáveis de ambiente

```
DB_HOST

DB_NAME

DB_USER

DB_PASSWORD

OMDB_API_KEY

FANART_API_KEY

TMDB_API_KEY

GROQ_API_KEY
```

---

## Execute

```
FabulioApplication
```

ou

```
FabulioApplicationSemWeb
```

para utilizar a interface de administração.

---

# Segurança

As chaves das APIs externas não fazem parte do projeto.

Todas são carregadas através de variáveis de ambiente.

---

# Objetivos do Projeto

Durante o desenvolvimento foram aplicados conhecimentos em:

- Spring Boot
- REST API
- Java
- PostgreSQL
- JPA
- Hibernate
- DTO
- Tratamento de exceções
- Consumo de APIs
- Integração entre sistemas
- Arquitetura em camadas

---

# Autor

**Gabriel Preé**

Estudante de Análise e Desenvolvimento de Sistemas

Desenvolvedor Full Stack em formação

GitHub:

https://github.com/GabrielPree

---

# Gostou do projeto?

Se este projeto foi útil ou interessante para você, considere deixar uma ⭐ no repositório.

Isso ajuda bastante na divulgação do projeto.

---

# Licença

Este projeto está distribuído sob a licença **MIT**.
