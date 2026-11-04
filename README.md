# 🏦 Motor de Análise de Risco e Core Bancário - ApexCred Matrix

## 🎯 Escopo do Sistema

O sistema simula um motor digital corporativo (ApexCred) focado em controle de contas, processamento de transações de caixa eletrônico e análise automatizada de risco de crédito com base no Score de adimplência do usuário. Rompendo com modelos simplistas de persistência rígida, a aplicação adota uma arquitetura orientada a objetos de alta resiliência baseada em **Abstração por Contratos, Polimorfismo Dinâmico e Governança de Dados via Exceções Customizadas**. O operador gerencia uma coleção de frotas de contas em memória RAM através de um encapsulamento multinível (`Banco`), sendo capaz de emitir cartões, processar transações financeiras protegidas e despachar ordens para duas máquinas de estado distintas: **concessão de empréstimos sob contas de débito** ou **expansão de crédito rotativo**. O sistema barra automaticamente violações de regras operacionais de negócio e falhas de input por hardware de forma defensiva antes de comprometer o buffer de memória.

### 🔄 Fluxo de Negócio e Tolerância a Falhas
```text
[ Entrada de Dados ] ➔ [ Validação de Tipagem ] ➔ [ Processamento de Negócio ] ➔ [ Falha de Regra / Input? ]
         │                        │                             │                         │
         ▼                        ▼                             ▼                         ▼
(Captura: MismatchException)  (Downcasting)            (Polimorfismo: operacao())  (Disparo: BusinessException)
```

---

## 🎨 Arquitetura do Software (Hierarquia, Sobrecarga & Downcasting)

O design deste ecossistema demonstra maturidade de engenharia corporativa ao isolar o modelo abstrato das especializações físicas de contas, utilizando uma camada agregadora para controlar os estados de análise de crédito.

```text
========================================================================================
  DIAGRAMA DE ARQUITETURA DE CLASSES E BUFFER DE MEMÓRIA (POO)
========================================================================================

                 ┌───────────────────────────┐
                 │       «Abstract»          │
                 │          Conta            │ (Contrato Base do Sistema)
                 ├───────────────────────────┤
                 │ - NumeroConta: double     │
                 │ - NomeTitular: String     │
                 │ - Score: int              │
                 ├───────────────────────────┤
                 │ + operacao(valor)*        │◄────────────────────┐
                 └─────────────▲─────────────┘                     │
                               │                                   │
               ┌───────────────┴───────────────┐                   │
               │                               │ (Herança)         │ (Invocação Polimórfica
     ┌─────────┴─────────┐           ┌─────────┴─────────┐         │  via Contrato Base)
     │      debito       │           │     credito       │         │
     ├───────────────────┤           ├───────────────────┤         │
     │ - SaldoConta: dbl │           │ - LimiteDisp: dbl │         │
     │ + sacar(quantia)  │           │                   │         │
     └───────────────────┘           └───────────────────┘         │
               ▲                               ▲                   │
               └───────────────┬───────────────┘                   │
                               │ (Ponteiro Polimórfico Injetado)    │
                               ▼                                   │
     ┌────────────────────────────────────────────────────────┐    │
     │                        Banco                           │    │
     ├────────────────────────────────────────────────────────┤    │
     │  - conta: Conta (Composição Polimórfica) ──────────────┼────┘
     │  - statusDebito: EtapaEmprestimo  [PENDENTE/APROV/RECUS]│
     │  - statusCredito: EtapaLimite     [PENDENTE/AUMENT/NEGAD]│
     └────────────────────────────────────────────────────────┘
```

### 📋 Inventário de Componentes e Responsabilidades

| Componente Técnico | Tipo | Atuação e Responsabilidade no Sistema |
| :--- | :--- | :--- |
| 🚀 `Main.java` | Controlador | Orquestra a interface de terminal por console, gerencia o menu iterativo e centraliza o barramento global de tratamento de exceções (`try-catch`). |
| 🛡️ `BusinessException.java`| Exceção | Especialização de erro em tempo de execução (`RuntimeException`) utilizada para interceptar e notificar violações lógicas das regras bancárias. |
| 🏦 `Banco.java` | Agregador Core | Camada de orquestração estrutural. Utiliza **Sobrecarga de Construtores** para isolar propostas de crédito e débito, gerenciando suas flags. |
| 👑 `Conta.java` | Classe Abstrata | Superclasse base de domínio. Proíbe instanciações incompletas e estabelece o contrato polimórfico de movimentação (`operacao`). |
| 💳 `credito.java` | Subclasse | Extensão focada em crédito rotativo, controlando o teto de limites e disparando erros caso o input transacional seja inválido. |
| 💰 `debito.java` | Subclasse | Extensão focada em movimentações de conta corrente, incorporando validações de saques defensivos e depósitos comerciais. |
| ⚙️ `Enums` | Estrutura | `EtapaEmprestimo` e `EtapaLimite` atuam como travas tipadas delimitando as bordas e estados das aprovações financeiras. |

---

## 🧠 Conceitos Avançados Praticados

* **Mecanismos de Defesa Global (`try-catch`):** Implementação de capturas multicamadas que isolam falhas físicas de digitação do teclado (`InputMismatchException`), barram quebras de regras comerciais (`BusinessException`) e salvaguardam o loop principal contra panes críticas inesperadas (`Exception`).
* **Design de Domínio Abstrato (`abstract`):** Acoplamento estrutural por contrato que impede a violação das fronteiras do sistema, obrigando as subclasses a implementar suas próprias metodologias de cálculo fiscal e faturamento de saldos.
* **Sobrecarga de Construtores Baseada em Contexto:** Flexibilidade polimórfica estática onde a classe agregadora configura dinamicamente o mapa de atributos válidos na memória e anula os buffers irrelevantes da operação corrente.
* **Downcasting Seguro com Operador de Instância (`instanceof`):** Filtragem dinâmica em tempo de execução que permite à classe controladora navegar e extrair métodos restritos e exclusivos dos tipos filhos (`sacar`, `depositar`) a partir de referências de ponteiros genéricos.

---

## 💻 Demonstração Operacional (Terminal)

Veja uma simulação analítica do comportamento do console processando barramentos de erro de negócio (saque acima do saldo) e erros de hardware (digitação incorreta):

```text
## 💻 Demonstração Operacional (Terminal Corporativo)

Veja a simulação analítica e detalhada do console do sistema operando em cenários de conformidade nominal (sucesso) e disparando o barramento defensivo global em cenários de falhas críticas:

```text
====== BANCO CENTRAL JAVA ======

===== Menu ======
1 - Cadastrar conta 
2 - Verificar conta do usuário 
3 - Contas registradas 
4 - Saque 
5 - Deposito 
6 - Empréstimos 
7 - Limite de Crédito 
8 - Sair
Escolha uma opção: 1

====== Cadastrar conta ========
Qual tipo do cartão: (1 - Crédito / 2 - Débito): 2
Nome do cliente: Carlos Eduardo s s
Numero da conta: 102030
Score da conta: 8
Insira o saldo da conta: 500.00

===== Menu ======
1 - Cadastrar conta 
...
Escolha uma opção: 1

====== Cadastrar conta ========
Qual tipo do cartão: (1 - Crédito / 2 - Débito): 1
Nome do cliente: Ana Beatriz Silva
Numero da conta: 405060
Score da conta: 5
Insira o limite do cartão de credito: 1500.00

===== Menu ======
...
Escolha uma opção: 3

====== Contas registradas ========
[ CONTA ] Titular: Carlos Eduardo s s | Conta: 102030.0 | Score: 8 | Saldo Atual: R\$ 500.00 || STATUS DO PROCESSO: Empréstimo/Débito [PENDENTE]
[ CONTA ] Titular: Ana Beatriz Silva | Conta: 405060.0 | Score: 5 | Limite Disponível: R\$ 1500.00 || STATUS DO PROCESSO: Crédito [PENDENTE]

===== Menu ======
...
Escolha uma opção: 6

====== Empréstimos ========
Insira o numero da conta para analise de Empréstimos: 102030
Quanto deseja de Emprestimo: 2500.00

Emprestimo de R\$ 2500.00 realizado com sucesso!

===== Menu ======
...
Escolha uma opção: 7

====== Limite de Crédito ========
Insira o numero da conta para analise de Limite: 405060
Score muito baixo para aumento. Limite NEGADO

===== Menu ======
...
Escolha uma opção: 3

====== Contas registradas ========
[ CONTA ] Titular: Carlos Eduardo s s | Conta: 102030.0 | Score: 8 | Saldo Atual: R\$ 3000.00 || STATUS DO PROCESSO: Empréstimo/Débito [APROVADO]
[ CONTA ] Titular: Ana Beatriz Silva | Conta: 405060.0 | Score: 5 | Limite Disponível: R\$ 1500.00 || STATUS DO PROCESSO: Crédito [NEGADO]

===== Menu ======
...
Escolha uma opção: 4

====== Saque ========
Insira o numero da conta para saque: 102030
Quanto deseja sacar: 3500.00

 Erro de Negócio: Erro de Saque: Saldo insuficiente!

===== Menu ======
...
Escolha uma opção: 4

====== Saque ========
Insira o numero da conta para saque: 405060
Operação inválida: Esta conta é de CRÉDITO e não aceita saque.

===== Menu ======
...
Escolha uma opção: OITO

Erro de Sistema: Você digitou letras onde eram esperados números!

===== Menu ======
...
Escolha uma opção: 8
Programa Finalizado

Encerrando os serviços do terminal bancário de forma segura
```

