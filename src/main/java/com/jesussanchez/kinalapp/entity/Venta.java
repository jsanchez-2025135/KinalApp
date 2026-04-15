package com.jesussanchez.kinalapp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_venta")
    private Long codigoVenta;
    @Column(name = "fecha_venta", nullable = false )
    private LocalDate fechaVenta;
    @Column( nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    @Column (nullable = false)
    private int estado;

    public Venta(){
    }

    @ManyToOne
    @JoinColumn(name = "clientes_dpi_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "usuarios_codigo_usuario", nullable = false)
    private Usuario usuario;

    public Venta(Long codigoVenta, LocalDate fechaVenta, BigDecimal total, int estado, Cliente cliente, Usuario usuario){
        this.codigoVenta = codigoVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = estado;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    public Long getCodigoVenta()
        {return codigoVenta;
    }

    public void setCodigoVenta(Long codigoVenta)
        {this.codigoVenta = codigoVenta;
    }

    public LocalDate getFechaVenta()
        {return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta)
        {this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal()
        {return total;
    }

    public void setTotal(BigDecimal total)
        {this.total = total;
    }

    public int getEstado()
        {return estado;
    }

    public void setEstado(int estado)
        {this.estado = estado;
    }

    public Cliente getCliente()
        {return cliente;
    }

    public void setCliente(Cliente cliente)
        {this.cliente = cliente;
    }

    public Usuario getUsuario()
        {return usuario;
    }

    public void setUsuario(Usuario usuario)
        {this.usuario = usuario;
    }
}