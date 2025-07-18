# 🗂️ Java Board Manager

Este projeto é uma aplicação de gerenciamento de tarefas, desenvolvida em **Java puro** com foco em **boas práticas**, arquitetura limpa (**SOLID**) e persistência de dados via **MySQL**. A aplicação permite a criação de boards com colunas personalizadas e cards organizados por status.

## 💡 Funcionalidades

### 📋 Menu Inicial
1. Criar um novo **Board**
2. Selecionar um **Board** existente
3. Excluir um **Board**

### 📁 Menu do Board Selecionado
1. Criar um novo **Card**
2. Mover um **Card** existente entre colunas
3. **Bloquear** um Card
4. **Desbloquear** um Card
5. **Cancelar** um Card
6. Visualizar o **Board** completo
7. Visualizar uma **coluna** com seus Cards
8. Ver detalhes de um **Card**
9. Voltar ao menu anterior

---

## 🛠️ Tecnologias Utilizadas

- **Java** – linguagem principal do projeto
- **MySQL** – banco de dados relacional
- **Docker** – containerização do banco de dados
- **DBeaver** – gerenciamento visual do banco
- **Flyway** – versionamento de scripts SQL
- **Lombok** – simplificação de código com anotações
- **SOLID** – princípios de design orientado a objetos
