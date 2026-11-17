# FitApp 💪

Aplicativo Android para gestão de treinos em academia, conectando professores e alunos.

> **Projeto Integrador** · Engenharia de Software · UniEVANGÉLICA

---

## 📖 Sobre

O **FitApp** digitaliza o acompanhamento de treinos de uma academia. O professor
cadastra alunos, monta fichas de treino personalizadas e acompanha a evolução;
o aluno visualiza seus treinos e registra o que realizou. A ligação entre os dois
é feita por um **código de vinculação exclusivo** gerado para cada professor.

## ✨ Funcionalidades

**Professor:** criar conta (recebe um código de vinculação), ver seus alunos e criar
treinos personalizados (com exercícios, séries e repetições).

**Aluno:** criar conta com o código do professor, visualizar os treinos prescritos,
marcar treinos como realizados e acompanhar a frequência.

## 🛠️ Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| Interface | Jetpack Compose + Material 3 |
| Arquitetura | MVVM |
| Estado | ViewModel + StateFlow |
| Banco de dados | Room / SQLite (offline-first) |
| Assíncrono | Kotlin Coroutines |
| Testes | JUnit |

**Planejado:** Firebase Authentication e sincronização com servidor remoto.

## 🏗️ Arquitetura (MVVM)

```
UI (Jetpack Compose)
      ↕
ViewModel (StateFlow)
      ↕
Repositórios (regras de negócio)
      ↕
DAOs (Room) → SQLite (banco local)
```

## 📁 Estrutura

```
app/src/main/java/com/fitapp/
  data/local/entity/   # Entidades Room (Professor, Aluno, Treino, Exercicio, Desempenho)
  data/local/dao/      # DAOs (acesso aos dados)
  data/local/          # AppDatabase
  repository/          # Regras de negócio (Usuario, Treino, Desempenho)
  ui/login/            # Tela e ViewModel de Login
  ui/cadastro/         # Tela e ViewModel de Cadastro
  ui/professor/        # Área do professor (home + criar treino)
  ui/aluno/            # Área do aluno (home + treinos)
  ui/theme/            # Tema visual
  utils/               # Resultado, Validador, GeradorCodigo, SegurancaSenha
  MainActivity.kt      # Navegação e injeção de dependências
app/src/test/          # Testes unitários das regras de negócio
```

## 🗃️ Modelo de dados

```
Professor 1 ─── N Aluno        (um professor tem vários alunos)
Aluno     1 ─── N Treino       (um aluno tem vários treinos)
Treino    1 ─── N Exercicio    (um treino tem vários exercícios)
Aluno     1 ─── N Desempenho   (registros de treinos realizados)
```

As senhas são armazenadas apenas como hash (SHA-256). E-mail e código de vinculação
possuem restrição de unicidade no banco.

## ▶️ Como executar

**Pré-requisitos:** Android Studio (recente), JDK 17, um emulador ou celular Android.

1. Abra a pasta do projeto no Android Studio e aguarde o Gradle sincronizar.
2. Selecione um dispositivo (emulador ou celular) e clique em **Run ▶**.
3. O app abre na tela de login.

**Fluxo de teste sugerido:** cadastre um professor (anote o código mostrado) →
cadastre um aluno usando esse código → faça login como professor e crie um treino →
faça login como aluno e veja/realize o treino.

Para rodar os testes: aba Gradle → app → verification → test.

## 👥 Equipe

| Integrante | Papel |
|---|---|
| Matheus Vinícios Nunes Jacinto | Product Owner |
| Luiz Eduardo Veloso Toquio | Arquiteto de Software |
| Muryllo Alvarenga Chaves | Designer (UX/UI) |
| Eduardo Soares de Araújo Filho | Analista de Requisitos |
