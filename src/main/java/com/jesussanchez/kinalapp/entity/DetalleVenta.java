package com.jesussanchez.kinalapp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_ventas" )
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "codigo_detalle_venta")
    private Long codigoDetalleVenta;
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    @Column(nullable = false, precision = 10,scale = 2)
    private BigDecimal subtotal;

    public DetalleVenta() {
    }

    @ManyToOne
    @JoinColumn(name = "productos_codigo_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "ventas_codigo_venta", nullable = false)
    private Venta venta;

    public DetalleVenta(Long codigoDetalleVenta, int cantidad, BigDecimal precioUnitario, BigDecimal subtotal){
        this.codigoDetalleVenta = codigoDetalleVenta;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Long getCodigoDetalleVenta() {
        return codigoDetalleVenta;
    }

    public void setCodigoDetalleVenta(Long codigoDetalleVenta)
        {this.codigoDetalleVenta = codigoDetalleVenta;
    }

    public int getCantidad()
        {return cantidad;
    }

    public void setCantidad(int cantidad)
        {this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario()
        {return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario)
        {this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal()
        {return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal)
        {this.subtotal = subtotal;
    }

    public Producto getProducto()
        {return producto;
    }

    public void setProducto(Producto producto)
        {this.producto = producto;
    }

    public Venta getVenta()
        {return venta;
    }

    public void setVenta(Venta venta)
        {this.venta = venta;
    }
}
