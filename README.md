# Sistema de Estoque de Produtos — Java ☕

Projeto desenvolvido como exercício prático de Programação Orientada a Objetos (POO) em Java, demonstrando a aplicação sólida de:
- **Classes Abstratas**
- **Herança**
- **Interfaces**
- **Polimorfismo Dinâmico (Sobrescrita)**
- **Polimorfismo Estático (Sobrecarga)**
- **Composição**
- **Tratamento e Hierarquia de Exceções Personalizadas**

---

## 📁 Estrutura do Repositório

```text
sistema-estoque/
├── .gitignore
├── README.md
└── src/
    ├── EstoqueException.java             # Exceção base do domínio de estoque
    ├── QuantidadeInvalidaException.java  # Exceção para quantidades/preços inválidos (< 0)
    ├── ProdutoIndisponivelException.java # Exceção para tentativas de venda sem estoque
    ├── Vendavel.java                     # Interface de contrato para itens vendáveis
    ├── Product.java                      # Classe abstrata base com encapsulamento e sobrecarga
    ├── ProdutoComum.java                 # Subclasse comum com cálculo direto de valor
    ├── ProdutoPerecivel.java             # Subclasse perecível com validade e desconto de 20%
    ├── Estoque.java                      # Classe com composição (TEM UMA List<Product>)
    └── EstoqueApp.java                   # Aplicação principal (main) demonstrando cenários e catches
```

---

## 🧩 Conceitos de Orientação a Objetos Aplicados

### 1. Hierarquia de Exceções (`EstoqueException`, `QuantidadeInvalidaException`, `ProdutoIndisponivelException`)
- `EstoqueException` estende `Exception` (checked exception), servindo como base genérica para qualquer erro operacional do estoque.
- `QuantidadeInvalidaException` herda de `EstoqueException` e é disparada caso valores numéricos como preço ou quantidade sejam negativos.
- `ProdutoIndisponivelException` herda de `EstoqueException` e é disparada caso a quantidade solicitada para venda exceda o estoque disponível.
- **Ordem dos blocos catch**: No tratamento de exceções, os blocos mais específicos (`QuantidadeInvalidaException`, `ProdutoIndisponivelException`) são posicionados antes do bloco genérico (`EstoqueException`), prevenindo código inalcançável (unreachable code).

### 2. Interface (`Vendavel`)
Define o contrato de venda para qualquer item comercializável no sistema:
```java
public interface Vendavel {
    void vender(int quantidadeDesejada) throws ProdutoIndisponivelException;
}
```

### 3. Classe Abstrata e Encapsulamento (`Product`)
- Implementa `Vendavel`.
- Possui atributos privados (`nome`, `preco`, `quantidade`) acessíveis apenas via métodos getters e setters que validam a consistência dos dados.
- O construtor valida e impede a criação de produtos com preço ou estoque negativos.
- Fornece o método abstrato `public abstract double calcularValorTotal();` para forçar as subclasses a implementarem sua própria regra de negócio.
- Fornece o método concreto `getDescricao()` formatado.

### 4. Polimorfismo Estático — Sobrecarga (`aplicarDesconto`)
Na classe `Product`, duas assinaturas do mesmo método coexistem com propósitos distintos:
1. `aplicarDesconto(double percentual)`: Aplica um percentual direto sobre o preço unitário.
2. `aplicarDesconto(double percentual, double descontoMaximo)`: Aplica o percentual, porém limitando o abatimento a um valor financeiro máximo especificado.

### 5. Herança e Polimorfismo Dinâmico — Sobrescrita
- `ProdutoComum extends Product`:
  - Implementa `calcularValorTotal()` retornando `preco * quantidade`.
- `ProdutoPerecivel extends Product`:
  - Possui o atributo adicional `diasParaVencer`.
  - Sobrescreve `calcularValorTotal()` aplicando **20% de desconto automático** se `diasParaVencer <= 3`.
  - Sobrescreve `getDescricao()` para incluir a validade do lote e o aviso promocional.

### 6. Composição (`Estoque`)
- A classe `Estoque` **não herda** de `Product`, mas sim **TEM UMA lista** (`List<Product>`).
- No método `calcularValorTotalEstoque()`, o estoque itera pela lista e chama `p.calcularValorTotal()`. Graças ao polimorfismo dinâmico em tempo de execução, cada objeto calcula seu total com base na sua implementação específica (comum ou perecível), desacoplando o estoque dos detalhes de cada tipo.

---

## 🚀 Como Compilar e Executar

### Pré-requisitos
- JDK 8 ou superior instalado (JDK 11, 17 ou 21 recomendados).

### Passo 1: Navegar até o diretório do projeto
```bash
cd sistema-estoque/src
```

### Passo 2: Compilar todas as classes
```bash
javac *.java
```

### Passo 3: Executar a aplicação principal
```bash
java EstoqueApp
```

---

## 🖥️ Exemplo de Saída Esperada no Console

```text
========================================================================
             SISTEMA DE GERENCIAMENTO DE ESTOQUE DE PRODUTOS            
========================================================================

>>> 1. CADASTRO DE PRODUTOS VÁLIDOS
✓ 4 produtos cadastrados com sucesso!

>>> ESTOQUE INICIAL:
-------------------------------------------------------------------------------------------------------
Item | Produto            | Preço Unitário   | Quantidade   | Validade/Info        | Subtotal      
-------------------------------------------------------------------------------------------------------
[ 0] | Teclado Mecânico   | R$        250,00 |         10 unid | -                    | R$     2500,00
[ 1] | Mouse Gamer        | R$        120,00 |         15 unid | -                    | R$     1800,00
[ 2] | Café Especial 500g | R$         35,00 |         20 unid | 30 dia(s)            | R$      700,00
[ 3] | Leite Integral 1L  | R$          5,00 |         50 unid | 2 dia(s) (20% OFF)   | R$      200,00
-------------------------------------------------------------------------------------------------------
VALOR TOTAL DO ESTOQUE: R$ 5200,00
-------------------------------------------------------------------------------------------------------

>>> 2. TESTE DE VALIDAÇÃO: CADASTRO COM QUANTIDADE NEGATIVA
Tentando instanciar ProdutoComum com quantidade = -5...
✓ Exceção esperada capturada com sucesso!
  [QuantidadeInvalidaException]: A quantidade não pode ser negativa: -5

>>> 3. DEMONSTRAÇÃO DE SOBRECARGA (aplicarDesconto)
Antes dos descontos:
  Produto: Teclado Mecânico   | Preço Unitário: R$  250,00 | Estoque:  10 unid.
  Produto: Mouse Gamer        | Preço Unitário: R$  120,00 | Estoque:  15 unid.

Após aplicarDesconto(10.0%) no Teclado:
  Produto: Teclado Mecânico   | Preço Unitário: R$  225,00 | Estoque:  10 unid.

Após aplicarDesconto(50.0%, max R$ 20.00) no Mouse:
  Produto: Mouse Gamer        | Preço Unitário: R$  100,00 | Estoque:  15 unid.

>>> 4. TESTE DE VENDA BEM-SUCEDIDA
Vendendo 3 unidades do Teclado Mecânico (índice 0)...
✓ Venda realizada com sucesso!
  Estoque atualizado: Produto: Teclado Mecânico   | Preço Unitário: R$  225,00 | Estoque:   7 unid.

>>> 5. TESTE DE VENDA COM ESTOQUE INSUFICIENTE
Tentando vender 15 unidades do Teclado Mecânico (disponível: 7)...
✓ Exceção esperada capturada com sucesso!
  [ProdutoIndisponivelException]: Estoque insuficiente para 'Teclado Mecânico'. Solicitado: 15, Disponível: 7

>>> 6. RELATÓRIO FINAL DO ESTOQUE E POLIMORFISMO
Observação sobre o cálculo polimórfico:
 - Produtos comuns: Preço Unitário * Quantidade
 - Leite Integral (validade = 2 dias <= 3): Preço Unitário * Quantidade com 20% de desconto automático.

-------------------------------------------------------------------------------------------------------
Item | Produto            | Preço Unitário   | Quantidade   | Validade/Info        | Subtotal      
-------------------------------------------------------------------------------------------------------
[ 0] | Teclado Mecânico   | R$        225,00 |          7 unid | -                    | R$     1575,00
[ 1] | Mouse Gamer        | R$        100,00 |         15 unid | -                    | R$     1500,00
[ 2] | Café Especial 500g | R$         35,00 |         20 unid | 30 dia(s)            | R$      700,00
[ 3] | Leite Integral 1L  | R$          5,00 |         50 unid | 2 dia(s) (20% OFF)   | R$      200,00
-------------------------------------------------------------------------------------------------------
VALOR TOTAL DO ESTOQUE: R$ 3975,00
-------------------------------------------------------------------------------------------------------
========================================================================
                     EXECUÇÃO CONCLUÍDA COM SUCESSO!                    
========================================================================
```

---

## 📤 Como Subir no GitHub

Execute no terminal dentro da pasta `sistema-estoque`:

```bash
git init
git add .
git commit -m "feat: implementacao completa do sistema de estoque com POO e excecoes"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/NOME_DO_REPOSITORIO.git
git push -u origin main
```
