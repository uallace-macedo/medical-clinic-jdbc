<div align="center">
  <h1>Medical Clinic (JDBC) 🩺</h1>
  <p>Gestão de clínica médica com persistência em banco de dados relacional e arquitetura em camadas.</p>
</div>

## 🛠️ Implementação Técnica

### 🏗️ Arquitetura e Padrões
- `DAO Pattern`: Encapsulamento da lógica de acesso a dados, isolando o SQL das regras de negócio.
- `Connection Factory`: Centralização da gestão de conexões com o banco de dados (PostgreSQL/MySQL).
- `Separação em Camadas`: Divisão clara entre Interface (View), Lógica (Service) e Persistência (Repository).

### 🗄️ Persistência de Dados (JDBC)
- `PreparedStatement`: Uso obrigatório para execução de queries, garantindo proteção contra SQL Injection.
- `Gestão de Recursos`: Implementação de try-with-resources para fechamento automático de conexões e statements.
- `Relacionamentos SQL`: Modelagem e manipulação de relações entre entidades (Médicos, Pacientes e Consultas).

### 🛡️ Experiência do Usuário (CLI)
- `Entradas Seguras`: Tratamento de buffer e validação de tipos para impedir falhas de leitura e saltos de menu.
- `Tratamento de Exceções`: Captura de SQLException e conversão para exceções de negócio personalizadas.
- `Feedback Visual`: Interface via terminal com menus numerados e mensagens de status claras.

### 🏗️ Estrutura do Projeto
- `model`: Representação das entidades (POJOs) mapeadas para as tabelas do banco.
- `repository`: Camada de acesso ao banco de dados (DAOs).
- `service`: Camada de regras de negócio e validações.
- `view`: Interface CLI para interação e entrada de dados.
- `exception`: Hierarquia de erros customizada para o domínio da aplicação.
- `Main`: Ponto de entrada do sistema.

## 🚀 Como Executar
```bash
# 1. Instale o MySQL e crie o banco conforme o script SQL fornecido.
# 2. Baixe o MySQL Connector/J e coloque-o na pasta /lib.

# Clonar o projeto
git clone https://github.com/uallace-macedo/medical-clinic-jdbc.git

# Compilar
javac -cp "lib/*" -d bin src/**/*.java

# Executar
java -cp "bin:lib/*" com.uallace.clinic.Main
```