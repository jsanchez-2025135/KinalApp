package com.jesussanchez.kinalapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @Column(name = "dpi_cliente")
    @NotBlank(message = "El DPI es obligatorio")
    @Size(min = 8, max = 20, message = "El DPI debe tener entre 8 y 20 caracteres")
    private String dpiCliente;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombreCliente;

    @Column(nullable = false)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellidoCliente;

    @Column(nullable = false)
    @NotBlank(message = "La dirección es obligatoria")
    @Size(min = 5, max = 100, message = "La dirección debe tener entre 5 y 100 caracteres")
    private String direccion;

    @Column(nullable = false)
    @NotNull(message = "El estado es obligatorio")
    private int estado;

    public Cliente() {
    }

    public Cliente(String dpiCliente, String nombreCliente, String apellidoCliente, String direccion, int estado) {
        this.dpiCliente = dpiCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.direccion = direccion;
        this.estado = estado;
    }

    public String getDpiCliente() {
        return dpiCliente;
    }

    public void setDpiCliente(String dpiCliente) {
        this.dpiCliente = dpiCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}