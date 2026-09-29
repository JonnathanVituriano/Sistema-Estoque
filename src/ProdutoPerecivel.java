public class ProdutoPerecivel extends Product {
    private int diasParaVencer;

    public ProdutoPerecivel(String nome, double preco, int quantidade, int diasParaVencer)
            throws QuantidadeInvalidaException {
        super(nome, preco, quantidade);
        if (diasParaVencer < 0) {
            throw new QuantidadeInvalidaException("Dias para vencer não pode ser negativo: " + diasParaVencer);
        }
        this.diasParaVencer = diasParaVencer;
    }

    public int getDiasParaVencer() {
        return diasParaVencer;
    }

    public void setDiasParaVencer(int diasParaVencer) throws QuantidadeInvalidaException {
        if (diasParaVencer < 0) {
            throw new QuantidadeInvalidaException("Dias para vencer não pode ser negativo: " + diasParaVencer);
        }
        this.diasParaVencer = diasParaVencer;
    }

    // Polimorfismo dinâmico: aplica 20% de desconto automático se diasParaVencer <= 3
    @Override
    public double calcularValorTotal() {
        double valorBase = getPreco() * getQuantidade();
        if (diasParaVencer <= 3) {
            return valorBase * 0.80; // 20% de desconto por proximidade da data de vencimento
        }
        return valorBase;
    }

    // Sobrescrita de getDescricao() para incluir a informação de validade
    @Override
    public String getDescricao() {
        String avisoPromocao = (diasParaVencer <= 3) ? " (Promoção: 20% OFF aplicado no total)" : "";
        return super.getDescricao() + String.format(" | Validade: %2d dia(s)%s", diasParaVencer, avisoPromocao);
    }
}
