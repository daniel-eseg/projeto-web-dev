import java.time.LocalDate;

public class TransporteLogistica extends MovimentacaoProduto {

	private String idTransporte;

	private String modalTransporte;

	private String produto;

	private double volume;

	private double custoTransporte;

	private String statusEntrega;

	private LocalDate dataMovimentacao;

	private Refinaria refinariaOrigem;

	private Venda venda;

	public double calcularCustoTransporte(PrecoMercado precoMercado) {
		return 0;
	}

}
