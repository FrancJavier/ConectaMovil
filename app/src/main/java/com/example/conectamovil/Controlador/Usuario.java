package com.example.conectamovil.Controlador;


public class Usuario {

    private String nombres, correo, pass;

    public Usuario(){}

    public Usuario(String nombres, String correo, String pass) {
        this.nombres = nombres;
        this.correo = correo;
        this.pass = pass;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


}
