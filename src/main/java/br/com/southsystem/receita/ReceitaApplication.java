package br.com.southsystem.receita;

import br.com.southsystem.receita.model.ArquivoCSV;
import br.com.southsystem.receita.service.ProcessarArquivoCsvService;
import br.com.southsystem.receita.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class ReceitaApplication {

	private static final String ARQUIVO_ENTRADA_PATH = "src/main/resources/arquivo_entrada.csv";
	private static final String ARQUIVO_SAIDA_PATH = "src/main/resources/arquivo_saida.csv";
	private static final String STATUS_SUCESSO = "Sucesso";
	private static final String STATUS_ERROR = "Erro";

	@Autowired
	private ProcessarArquivoCsvService processarArquivo;

	@Autowired
	private ReceitaService receitaService;

	public static void main(String[] args) {
		SpringApplication.run(ReceitaApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			try {
				List<ArquivoCSV> arquivos = processarArquivo.carregarArquivoCSV(ARQUIVO_ENTRADA_PATH);

				List<ArquivoCSV> arquivosProcessados = arquivos
						.stream()
						.map(arquivo -> executarAtualizacaoConta(arquivo.getAgencia(),
								arquivo.getConta(), arquivo.getSaldo(), arquivo.getStatus()))
						.collect(Collectors.toList());

				processarArquivo.gerarArquivoCSV(arquivosProcessados, ARQUIVO_SAIDA_PATH);

			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		};
	}

	private ArquivoCSV executarAtualizacaoConta(String agencia, String conta, double saldo, String status) {
		try {
			if (receitaService.atualizarConta(agencia, conta, saldo, status)) {
				return new ArquivoCSV(agencia, conta, saldo, status, STATUS_SUCESSO);
			} else {
				return new ArquivoCSV(agencia, conta, saldo, status, STATUS_ERROR);
			}
		} catch (RuntimeException | InterruptedException e) {
			return new ArquivoCSV(agencia, conta, saldo, status, STATUS_ERROR);
		}
	}
}
