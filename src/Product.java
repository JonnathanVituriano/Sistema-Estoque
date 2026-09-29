public abstract class Product implements Vendavel {
    private String nome;
    private double preco;
    private int quantidade;

    public Product(String nome, double preco, int quantidade) throws QuantidadeInvalidaException {
        if (preco < 0) {
            throw new QuantidadeInvalidaException("O preço não pode ser negativo: " + preco);
        }
        if (quantidade < 0) {
            throw new QuantidadeInvalidaException("A quantidade não pode ser negativa: " + quantidade);
        }
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    // Getters e Setters para manter o encapsulamento
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) throws QuantidadeInvalidaException {
        if (preco < 0) {
            throw new QuantidadeInvalidaException("O preço não pode ser negativo: " + preco);
        }
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) throws QuantidadeInvalidaException {
        if (quantidade < 0) {
            throw new QuantidadeInvalidaException("A quantidade não pode ser negativa: " + quantidade);
        }
        this.quantidade = quantidade;
    }

    // Método abstrato a ser implementado pelas subclasses (Polimorfismo dinâmico)
    public abstract double calcularValorTotal();

    // Método concreto que devolve nome, preço e quantidade formatados
    public String getDescricao() {
        return String.format("Produto: %-18s | Preço Unitário: R$ %7.2f | Estoque: %3d unid.",
                nome, preco, quantidade);
    }

    // Implementação do método da interface Vendavel
    @Override
    public void vender(int quantidadeDesejada) throws ProdutoIndisponivelException {
        if (quantidadeDesejada > this.quantidade) {
            throw new ProdutoIndisponivelException(
                    "Estoque insuficiente para '" + nome + "'. Solicitado: " + quantidadeDesejada +
                    ", Disponível: " + this.quantidade
            );
        }
        this.quantidade -= quantidadeDesejada;
    }

    // Sobrecarga (Polimorfismo Estático): versão 1 - aplicando desconto percentual direto
    public void aplicarDesconto(double percentual) {
        if (percentual < 0) {
            throw new IllegalArgumentException("Percentual de desconto não pode ser negativo: " + percentual);
        }
        double desconto = this.preco * (percentual / 100.0);
        this.preco = Math.max(0.0, this.preco - desconto);
    }

    // Sobrecarga (Polimorfismo Estático): versão 2 - aplicando desconto percentual com teto máximo
    public void aplicarDesconto(double percentual, double descontoMaximo) {
        if (percentual < 0 || descontoMaximo < 0) {
            throw new IllegalArgumentException("Percentual e teto de desconto não podem ser negativos.");
        }
        double descontoCalculado = this.preco * (percentual / 100.0);
        double descontoEfetivo = Math.min(descontoCalculado, descontoMaximo);
        this.preco = Math.max(0.0, this.preco - descontoEfetivo);
    }
}
