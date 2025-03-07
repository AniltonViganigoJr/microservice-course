package io.github.cursoms.msclientes.application.representation;

import io.github.cursoms.msclientes.domain.Cliente;

import java.util.Objects;

public class ClienteSaveRequest {
    private String cpf;
    private String nome;
    private Integer idade;

    public ClienteSaveRequest(){}

    public ClienteSaveRequest(String cpf, String nome, Integer idade) {
        this.cpf = cpf;
        this.nome = nome;
        this.idade = idade;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClienteSaveRequest that = (ClienteSaveRequest) o;
        return Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cpf);
    }

    public Cliente toModel(){
        return new Cliente(cpf, nome, idade);
    }
}