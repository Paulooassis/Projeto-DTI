
# 🚁 Sistema de Entregas por Drones (Spring Boot)

Este é um sistema de simulação de entregas por drones em ambientes urbanos, desenvolvido com **Spring Boot** para o desafio técnico da **dti digital**.

---

## ✅ Como executar o projeto

### Pré-requisitos
- Java 17+
- Maven 3.8+
- IDE (recomendado: IntelliJ ou VSCode)

### Passos

1. Clone o repositório:

```bash
git clone https://github.com/seuusuario/seurepositorio.git
cd seurepositorio
```

2. Execute com Maven:

```bash
./mvnw spring-boot:run
```

3. Acesse a aplicação:

```
http://localhost:8080
```

4. Console do banco H2 (opcional):

```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
```

---

## 🧱 Arquitetura do Projeto

O sistema está estruturado em camadas:

```
📦 com.dti.demo
├── 📁 controller         -> Camada REST, endpoints HTTP
├── 📁 service            -> Lógica de negócio central
├── 📁 repository         -> Integração com banco (Spring Data JPA)
├── 📁 entity             -> Entidades JPA persistidas
├── 📁 dto                -> Data Transfer Objects (entrada/saída)
├── 📁 enums              -> Enumerações de domínio
└── 📁 exception          -> (Tratamento básico direto nos services)
```

Todos os dados são armazenados em um **banco H2 em memória**, simulando persistência real durante a execução da aplicação.

---

## 🚀 Funcionalidades implementadas

### 📦 Entregas
- Alocação de pedidos em drones disponíveis
- Priorização automática com base em peso, distância e prioridade
- Redução da bateria conforme distância
- Cálculo de tempo total de entrega
- Mudança automática de estado do drone (`IDLE`, `EM_VOO`, `ENTREGANDO`, `RETORNANDO`)

### 🗺️ Zonas de Exclusão Aérea
- Cadastro de áreas de exclusão (coordenadas + raio)
- Verificação automática ao alocar pedidos
- Endpoint para verificar coordenadas manualmente

### 📊 Relatórios
- Quantidade total de entregas
- Tempo médio por entrega
- Drone mais eficiente
- Mapa ASCII simplificado das entregas

---

## 🧪 Testes automatizados

Utilizamos o `SpringBootTest` para validar os principais fluxos do sistema:

- Criação de drone
- Adição de zona de exclusão
- Alocação de pedidos em entrega
- Geração de relatórios

Arquivo de testes:  
`src/test/java/com/dti/demo/DtiApplicationTests.java`

---

## 📥 Exemplo de entrada/saída - DTOs

### 🔹 Criar drone (POST /drones)

**Entrada:**
```json
{
  "pesoSuportado": 10.0,
  "distanciaSuportada": 100.0,
  "bateria": 100,
  "disponivel": true,
  "estado": "IDLE"
}
```

**Saída:**
```json
{
  "id": 1,
  "pesoSuportado": 10.0,
  "distanciaSuportada": 100.0,
  "bateria": 100,
  "disponivel": true,
  "estado": "IDLE"
}
```

### 🔹 Gerar relatório (GET /relatorio)

**Saída:**
```json
{
  "totalEntregas": 5,
  "tempoMedioEntrega": 8.5,
  "droneMaisEficiente": "Drone ID 2 (distância 160.0 km)",
  "mapaEntregas": "📦📦⬜⬜⬜📦"
}
```

---

## 🧠 Diferenciais do Projeto

- ✅ Arquitetura em camadas clara e separada
- ✅ Uso de DTOs em todas as comunicações entre camadas
- ✅ Validações simples sem frameworks adicionais
- ✅ Simulação realista com cálculo de tempo, distância e bateria
- ✅ Testes de integração cobrindo as principais regras
- ✅ Visualização de mapa ASCII
- ✅ Tratamento de zonas de exclusão aérea
- ✅ Endpoint para simulação e relatório final completo

---

## 💬 Autor

**Paulo Henrique Fonseca de Assis**  
Desenvolvedor Java • Spring Boot • SQL • REST APIs

---
