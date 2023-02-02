package com.creathor.cocineros;

import androidx.appcompat.app.AppCompatActivity;

public class ListaContenidoPedidos extends AppCompatActivity {

    private String id,nombre,cantidad,total,precio,extras,imagen,seccion,nota_mesero,estatus,id_mesero,meseroAsignado,strIdPedido;



    public String getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getCantidad() {
        return cantidad;
    }
    public String getTotal() {
        return total;
    }
    public String getPrecio() { return precio;}
    public String getExtras() { return extras; }
    public String getImagen() { return imagen; }
    public String getSeccion() { return seccion; }
    public String getNota_mesero() { return nota_mesero; }
    public String getEstatus(){return estatus;}
    public String getId_mesero(){return id_mesero;}
    public String getMeseroAsignado(){return  meseroAsignado;}

    public String getStrIdPedido() {return strIdPedido;}
    /* public String getFecha_entrega() { return fecha_entrega; }
    public String getFecha_final() { return fecha_final;}*/



    public void setId(String id){this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setCantidad(String cantidad) {this.cantidad = cantidad;}
    public void setTotal(String total) { this.total = total; }
    public void setPrecio(String precio) {this.precio = precio;}
    public void setExtras(String extras) { this.extras = extras;}
    public void setImagen(String imagen) { this.imagen = imagen;}
    public void setSeccion(String seccion) { this.seccion = seccion;}
    public void setNota_mesero(String nota_mesero) { this.nota_mesero = nota_mesero;}
    public  void setEstatus(String estatus){this.estatus=estatus;}
    public void  setId_mesero(String id_mesero){this.id_mesero=id_mesero;}
    public void setMeseroAsignado(String meseroAsignado) {this.meseroAsignado = meseroAsignado;}

    public void setStrIdPedido(String strIdPedido) {this.strIdPedido = strIdPedido;}
    /* public void setFecha_entrega(String fecha_entrega) { this.fecha_entrega = fecha_entrega;}
    public void setFecha_final(String fecha_final) {this.fecha_final = fecha_final;}*/



    public ListaContenidoPedidos(String id_content, String name, String cant, String totl, String price,String extrs,String note_mesero,String status,String id_meser,String meserAsignad,String id_pedid){

        this.id =id_content;
        this.nombre=name;
        this.cantidad=cant;
        this.total=totl;
        this.precio=price;
        this.extras =extrs;
        this.nota_mesero=note_mesero;
        this.estatus=status;
        this.id_mesero=id_meser;
        this.meseroAsignado=meserAsignad;
        this.strIdPedido=id_pedid;
       /* this.fecha_entrega=date_entrega;
        this.fecha_final=date_end;*/

    }
}
