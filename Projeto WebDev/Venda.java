import java.time.LocalDate;

public class Venda extends MovimentacaoProduto {

	private String idVenda;

	private String produto;

	private double volume;

	private double valorTotal;

	private String mercado;

	private LocalDate dataVenda;

	private TransporteLogistica[] transportes;

	public double calcularValorTotal(PrecoMercado precoMercado) {
		return 0;
	}

}
