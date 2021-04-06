package br.com.southsystem.receita.model;

import com.opencsv.bean.CsvBindByPosition;

public class ArquivoCSV {

    @CsvBindByPosition(position = 0, required = true)
    private String agencia;

    @CsvBindByPosition(position = 1, required = true)
    private String conta;

    @CsvBindByPosition(position = 2, required = true)
    private double saldo;

    @CsvBindByPosition(position = 3, required = true)
    private String status;

    @CsvBindByPosition(position = 4, required = false)
    private String resultado;

    public ArquivoCSV(String agencia, String conta, double saldo, String status) {
        this.agencia = agencia;
        this.conta = conta;
        this.saldo = saldo;
        this.status = status;
    }

    public ArquivoCSV(String agencia, String conta, double saldo, String status, String resultado) {
        this.agencia = agencia;
        this.conta = conta;
        this.saldo = saldo;
        this.status = status;
        this.resultado = resultado;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
