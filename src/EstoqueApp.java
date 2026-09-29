public class EstoqueApp {

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("             SISTEMA DE GERENCIAMENTO DE ESTOQUE DE PRODUTOS            ");
        System.out.println("========================================================================\n");

        Estoque estoque = new Estoque();

        // -------------------------------------------------------------------------
        // 1. Cadastrar pelo menos 2 ProdutoComum e 2 ProdutoPerecivel
        //    (um deles com diasParaVencer <= 3 para acionar o desconto de 20%)
        // -------------------------------------------------------------------------
        System.out.println(">>> 1. CADASTRO DE PRODUTOS VÁLIDOS");
        try {
            // Produtos Comuns
            ProdutoComum p1 = new ProdutoComum("Teclado Mecânico", 250.00, 10);
            ProdutoComum p2 = new ProdutoComum("Mouse Gamer", 120.00, 15);

            // Produtos Perecíveis (p4 possui diasParaVencer <= 3 -> desconto de 20% no total)
            ProdutoPerecivel p3 = new ProdutoPerecivel("Café Especial 500g", 35.00, 20, 30);
            ProdutoPerecivel p4 = new ProdutoPerecivel("Leite Integral 1L", 5.00, 50, 2);

            estoque.adicionarProduto(p1);
            estoque.adicionarProduto(p2);
            estoque.adicionarProduto(p3);
            estoque.adicionarProduto(p4);

            System.out.println("✓ 4 produtos cadastrados com sucesso!\n");
        } catch (QuantidadeInvalidaException e) {
            System.err.println("Erro inesperado no cadastro inicial: " + e.getMessage());
        }

        // Exibe o estoque inicial e cálculo polimórfico
        System.out.println(">>> ESTOQUE INICIAL:");
        estoque.exibirEstoque();
        System.out.println();

        // -------------------------------------------------------------------------
        // 2. Tentar cadastrar um produto com quantidade negativa
        //    (Capturando QuantidadeInvalidaException)
        // -------------------------------------------------------------------------
        System.out.println(">>> 2. TESTE DE VALIDAÇÃO: CADASTRO COM QUANTIDADE NEGATIVA");
        try {
            System.out.println("Tentando instanciar ProdutoComum com quantidade = -5...");
            ProdutoComum produtoInvalido = new ProdutoComum("Monitor Ultrawide", 1500.00, -5);
            estoque.adicionarProduto(produtoInvalido);
            System.out.println("ERRO: O produto não deveria ter sido aceito!");
        } catch (QuantidadeInvalidaException e) {
            System.out.println("✓ Exceção esperada capturada com sucesso!");
            System.out.println("  [QuantidadeInvalidaException]: " + e.getMessage());
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 3. Teste de Sobrecarga: aplicarDesconto() (Polimorfismo Estático)
        // -------------------------------------------------------------------------
        System.out.println(">>> 3. DEMONSTRAÇÃO DE SOBRECARGA (aplicarDesconto)");
        Product teclado = estoque.getProdutos().get(0);
        Product mouse = estoque.getProdutos().get(1);

        System.out.println("Antes dos descontos:");
        System.out.println("  " + teclado.getDescricao());
        System.out.println("  " + mouse.getDescricao());

        // Versão 1: percentual simples (10% de desconto no teclado de R$ 250,00 -> R$ 225,00)
        teclado.aplicarDesconto(10.0);
        System.out.println("\nApós aplicarDesconto(10.0%) no Teclado:");
        System.out.println("  " + teclado.getDescricao());

        // Versão 2: percentual com teto máximo (50% de R$ 120,00 seria R$ 60,00 de desconto, mas teto é R$ 20,00 -> R$ 100,00)
        mouse.aplicarDesconto(50.0, 20.0);
        System.out.println("\nApós aplicarDesconto(50.0%, max R$ 20.00) no Mouse:");
        System.out.println("  " + mouse.getDescricao());
        System.out.println();

        // -------------------------------------------------------------------------
        // 4. Venda de quantidade válida
        // -------------------------------------------------------------------------
        System.out.println(">>> 4. TESTE DE VENDA BEM-SUCEDIDA");
        try {
            System.out.println("Vendendo 3 unidades do Teclado Mecânico (índice 0)...");
            estoque.venderProduto(0, 3);
            System.out.println("✓ Venda realizada com sucesso!");
            System.out.println("  Estoque atualizado: " + estoque.getProdutos().get(0).getDescricao());
        } catch (ProdutoIndisponivelException e) {
            System.err.println("Falha na venda: " + e.getMessage());
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 5. Tentar vender mais do que o disponível
        //    (Capturando ProdutoIndisponivelException)
        // -------------------------------------------------------------------------
        System.out.println(">>> 5. TESTE DE VENDA COM ESTOQUE INSUFICIENTE");
        try {
            // O Teclado tinha 10, vendeu 3, restam 7. Vamos tentar vender 15.
            System.out.println("Tentando vender 15 unidades do Teclado Mecânico (disponível: "
                    + estoque.getProdutos().get(0).getQuantidade() + ")...");
            estoque.venderProduto(0, 15);
            System.out.println("ERRO: A venda não deveria ter ocorrido!");
        } catch (ProdutoIndisponivelException e) {
            System.out.println("✓ Exceção esperada capturada com sucesso!");
            System.out.println("  [ProdutoIndisponivelException]: " + e.getMessage());
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 6. Demonstração da ordem dos catches (conforme pista do enunciado)
        //    Capturando QuantidadeInvalidaException, ProdutoIndisponivelException
        //    e EstoqueException (a mais genérica por último no mesmo bloco try)
        // -------------------------------------------------------------------------
        System.out.println(">>> 6. DEMONSTRAÇÃO DA HIERARQUIA DE CATCHES (Bloco Unificado)");
        System.out.println("Executando operação controlada com múltiplos riscos de exceção:");

        testarTratamentoExcecoes(estoque, 0, -2);   // Dispara QuantidadeInvalidaException
        testarTratamentoExcecoes(estoque, 0, 50);   // Dispara ProdutoIndisponivelException
        testarTratamentoExcecoes(estoque, 99, 1);   // Dispara EstoqueException (índice inexistente)
        System.out.println();

        // -------------------------------------------------------------------------
        // 7. Imprimir o valor total do estoque final (Polimorfismo Dinâmico)
        // -------------------------------------------------------------------------
        System.out.println(">>> 7. RELATÓRIO FINAL DO ESTOQUE E POLIMORFISMO DINÂMICO");
        System.out.println("Observação sobre o cálculo polimórfico:");
        System.out.println(" - Produtos comuns: Preço Unitário * Quantidade");
        System.out.println(" - Leite Integral (validade = 2 dias <= 3): Preço Unitário * Quantidade com 20% de desconto automático.");
        System.out.println();
        estoque.exibirEstoque();

        System.out.println("========================================================================");
        System.out.println("                     EXECUÇÃO CONCLUÍDA COM SUCESSO!                    ");
        System.out.println("========================================================================");
    }

    /**
     * Demonstração prática da regra dos catches:
     * O catch mais genérico (EstoqueException) DEVE vir por último!
     * Se colocado antes de QuantidadeInvalidaException ou ProdutoIndisponivelException,
     * o compilador Java acusará erro de código inalcançável (unreachable catch block).
     */
    public static void testarTratamentoExcecoes(Estoque estoque, int indice, int quantidade) {
        try {
            System.out.printf("Testando operação (índice: %d, quantidade: %d)...%n", indice, quantidade);
            realizarOperacaoSegura(estoque, indice, quantidade);
            System.out.println("  Sucesso na operação!");
        } catch (QuantidadeInvalidaException e) {
            // Específica 1
            System.out.println("  -> [CATCH ESPECÍFICO 1 - QuantidadeInvalidaException]: " + e.getMessage());
        } catch (ProdutoIndisponivelException e) {
            // Específica 2
            System.out.println("  -> [CATCH ESPECÍFICO 2 - ProdutoIndisponivelException]: " + e.getMessage());
        } catch (EstoqueException e) {
            // Mais genérica: OBRIGATORIAMENTE POR ÚLTIMO
            System.out.println("  -> [CATCH GENÉRICO 3 - EstoqueException]: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar que centraliza operações e pode disparar tanto as exceções específicas
     * quanto a exceção genérica EstoqueException.
     */
    public static void realizarOperacaoSegura(Estoque estoque, int indice, int quantidade) throws EstoqueException {
        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException("A quantidade solicitada deve ser maior que zero: " + quantidade);
        }
        if (indice < 0 || indice >= estoque.getQuantidadeItensCadastrados()) {
            throw new EstoqueException("Falha operacional: índice de produto fora do estoque (" + indice + ")");
        }
        estoque.venderProduto(indice, quantidade);
    }
}
