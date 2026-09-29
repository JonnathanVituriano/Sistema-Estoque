import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estoque {
    // Composição: Estoque TEM UMA lista de Product (não herda de Product)
    private final List<Product> produtos;

    public Estoque() {
        this.produtos = new ArrayList<>();
    }

    // Adiciona um produto ao estoque
    public void adicionarProduto(Product p) {
        if (p == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        produtos.add(p);
    }

    // Vende itens de um produto pelo índice, propagando ProdutoIndisponivelException
    public void venderProduto(int indice, int quantidade) throws ProdutoIndisponivelException {
        if (indice < 0 || indice >= produtos.size()) {
            throw new IndexOutOfBoundsException("Índice de produto inválido: " + indice);
        }
        Product produto = produtos.get(indice);
        produto.vender(quantidade);
    }

    // Percorre a lista somando calcularValorTotal() de cada produto (Polimorfismo dinâmico)
    public double calcularValorTotalEstoque() {
        double total = 0.0;
        for (Product produto : produtos) {
            total += produto.calcularValorTotal();
        }
        return total;
    }

    // Retorna a lista de produtos (somente leitura para proteger a integridade)
    public List<Product> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    public int getQuantidadeItensCadastrados() {
        return produtos.size();
    }

    // Exibe todos os produtos cadastrados com suas descrições e totais
    public void exibirEstoque() {
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.printf("%-4s | %-18s | %-16s | %-12s | %-20s | %-14s%n",
                "Item", "Produto", "Preço Unitário", "Quantidade", "Validade/Info", "Subtotal");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < produtos.size(); i++) {
            Product p = produtos.get(i);
            String infoAdicional = "-";
            if (p instanceof ProdutoPerecivel) {
                ProdutoPerecivel pp = (ProdutoPerecivel) p;
                infoAdicional = pp.getDiasParaVencer() + " dia(s)" + (pp.getDiasParaVencer() <= 3 ? " (20% OFF)" : "");
            }
            System.out.printf("[%2d] | %-18s | R$ %13.2f | %9d unid | %-20s | R$ %11.2f%n",
                    i, p.getNome(), p.getPreco(), p.getQuantidade(), infoAdicional, p.calcularValorTotal());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------");
        System.out.printf("VALOR TOTAL DO ESTOQUE: R$ %.2f%n", calcularValorTotalEstoque());
        System.out.println("-------------------------------------------------------------------------------------------------------");
    }
}
