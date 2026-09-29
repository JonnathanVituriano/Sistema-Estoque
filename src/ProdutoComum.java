public class ProdutoComum extends Product {

    public ProdutoComum(String nome, double preco, int quantidade) throws QuantidadeInvalidaException {
        super(nome, preco, quantidade);
    }

    // Polimorfismo dinâmico: cálculo padrão (preço * quantidade)
    @Override
    public double calcularValorTotal() {
        return getPreco() * getQuantidade();
    }
}
