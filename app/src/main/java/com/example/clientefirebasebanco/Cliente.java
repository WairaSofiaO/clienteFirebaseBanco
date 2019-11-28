package com.example.clientefirebasebanco;

public class Cliente {

    private String codCliente;
    private String nombres;
    private String apellidos;
    private int saldo;
    private String usuario;
    private String clave;

    public Cliente() {
    }

    public Cliente(String codCliente, String nombres, String apellidos, int saldo, String usuario, String clave) {
        this.codCliente = codCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.saldo = saldo;
        this.usuario = usuario;
        this.clave = clave;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }





}
