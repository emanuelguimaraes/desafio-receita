package br.com.southsystem.receita.service;

import br.com.southsystem.receita.model.ArquivoCSV;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProcessarArquivoCsvService {

    private static final String DELIMITADOR = ";";
    private static final String VIRGULA = ",";
    private static final String PONTO = ".";
    private static final String HIFEN = "-";
    private static final String VAZIO = "";
    private static final String ARQUIVO_NAO_ENCONTRADO_ERROR = "O caminho do arquivo informado não foi encontrado";
    private static final String FORMATO_ARQUIVO_INVALIDO_ERROR = "O arquivo informado não possui o formato esperado (agencia;conta;saldo;status)";
    private static final String CARREGAMENTO_ARQUIVO_ERROR = "Ocorreu um erro ao carregar o arquivo. Tente novamente";
    private static final String GERACAO_ARQUIVO_ERROR = "Ocorreu um erro ao gerar o arquivo. Tente novamente";

    /**
     * Método responsável por carregar as informações de uma arquivo .csv e encapsula-lo
     * dentro de uma lista de {@link ArquivoCSV}
     *
     * @author Emanuel Brito
     *
     * @param pathOrigem
     * @return Lista de {@link ArquivoCSV}
     * @throws Exception
     */
    public List<ArquivoCSV> carregarArquivoCSV(String pathOrigem) throws Exception {
        try {
            return Files.lines(Paths.get(pathOrigem))
                    .skip(1)
                    .map(linha -> linha.split(DELIMITADOR))
                    .map(atributos -> new ArquivoCSV(atributos[0],
                            formatarConta(atributos[1]),
                            converterParaDouble(atributos[2]),
                            atributos[3]))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new Exception(ARQUIVO_NAO_ENCONTRADO_ERROR);
        } catch (IndexOutOfBoundsException e) {
            throw new Exception(FORMATO_ARQUIVO_INVALIDO_ERROR);
        } catch (Exception e) {
            throw new Exception(CARREGAMENTO_ARQUIVO_ERROR);
        }
    }

    /**
     * Método responsável por gerar uma arquivo .csv de acordo com as informações contidas na lista de {@link ArquivoCSV}
     *
     * @author Emanuel Brito
     *
     * @param informacoes
     * @param pathDestino
     * @throws Exception
     */
    public void gerarArquivoCSV(List<ArquivoCSV> informacoes, String pathDestino) throws Exception {
        try(Writer writer = Files.newBufferedWriter(Paths.get(pathDestino))) {
            StatefulBeanToCsv<ArquivoCSV> beanToCsv = new StatefulBeanToCsvBuilder<ArquivoCSV>(writer)
                    .withSeparator(DELIMITADOR.charAt(0))
                    .withApplyQuotesToAll(false)
                    .build();

            beanToCsv.write(informacoes);

            writer.flush();

        } catch (IOException e) {
            throw new Exception(ARQUIVO_NAO_ENCONTRADO_ERROR);
        } catch (Exception e) {
            throw new Exception(GERACAO_ARQUIVO_ERROR);
        }
    }

    private double converterParaDouble(String valor) {
        return Double.valueOf(valor.replace(VIRGULA, PONTO));
    }

    private String formatarConta(String conta) {
        return conta.replace(HIFEN, VAZIO);
    }
}
