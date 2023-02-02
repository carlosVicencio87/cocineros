package com.creathor.cocineros;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterContenidoPedido extends RecyclerView.Adapter<AdapterContenidoPedido.ViewHolderRecycler>{
    private ArrayList<ListaContenidoPedidos> contenidoPedidorecycler;
    ViewHolderRecycler viewholderContenidoPedido;
    private  RecyclerView recyclerView;
    private Context context;
    private String id,nombre,cantidad,total,precio,extras,imagen,seccion,strNotaMesero,strEstatus,strIdmesero,strMeseroAsignado,strIdPedido;
    private TextView aceptar_pedido;
    private EditText nota_mesero;
    private LinearLayout caja_contenedor_velo_mecero;
    private AdapterContenidoPedido activity;


    public AdapterContenidoPedido(ArrayList<ListaContenidoPedidos> contenidoPedidoreycler )
    {
        this.contenidoPedidorecycler =contenidoPedidoreycler;
    }
    @Override
    public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);
        context=parent.getContext();
        vista.setFocusable(true);
        return new ViewHolderRecycler(vista);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterContenidoPedido.ViewHolderRecycler holder, int position) {
        viewholderContenidoPedido =holder;
        id = contenidoPedidorecycler.get(position).getId();
        nombre= contenidoPedidorecycler.get(position).getNombre();
        cantidad= contenidoPedidorecycler.get(position).getCantidad();
        total= contenidoPedidorecycler.get(position).getTotal();
        precio= contenidoPedidorecycler.get(position).getPrecio();
        extras= contenidoPedidorecycler.get(position).getExtras();
        strNotaMesero=contenidoPedidorecycler.get(position).getNota_mesero();
        strEstatus=contenidoPedidorecycler.get(position).getEstatus();
        strIdmesero=contenidoPedidorecycler.get(position).getId_mesero();
        strMeseroAsignado=contenidoPedidorecycler.get(position).getMeseroAsignado();
        strIdPedido=contenidoPedidorecycler.get(position).getStrIdPedido();
        // fecha_final= pedidosrecycler.get(position).getFecha_final();

        holder.id_content.setText(id);
        holder.name.setText(nombre);
        holder.cant.setText(cantidad);
        holder.totl.setText(total);
        holder.price.setText(precio);
        holder.extrs.setText(extras);
        holder.nota_meser.setText(strNotaMesero);
        //holder.date_end.setText(fecha_final);
        holder.send_kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.box_content_velo_mecero.setVisibility(View.VISIBLE);


            }
        });
        holder.confirm_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.box_content_velo_mecero.setVisibility(View.GONE);

            }
        });
        holder.confirm_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.box_content_velo_mecero.setVisibility(View.GONE);

                int posicion=holder.getAdapterPosition();
                id = contenidoPedidorecycler.get(posicion).getId();
                Log.e("id","S"+id);
                nombre=contenidoPedidorecycler.get(posicion).getNombre();
                strIdPedido=contenidoPedidorecycler.get(posicion).getStrIdPedido();
                strIdmesero=contenidoPedidorecycler.get(posicion).getId_mesero();
                strMeseroAsignado=contenidoPedidorecycler.get(posicion).getMeseroAsignado();
                strEstatus=holder.status;
                Log.e("name",nombre);

                Log.e("status",strEstatus);
                Log.e("IDPED",strIdPedido);
                Log.e("IDMeser",strIdmesero);
                Log.e("MESER ",strMeseroAsignado);

                int indice_actual=holder.getAdapterPosition();
                Log.e("indice_de_pedido", String.valueOf(indice_actual));
                ((Estacion)context).enviarIndiceConNota(indice_actual,id,strEstatus,strIdmesero,strMeseroAsignado,strIdPedido);

            }
        });


        //holder.list_pedidos.setText(lista_pedidos);
    }
    public void pedidos_espera(String id_pedido){

        Log.e("id_activyty",""+id_pedido);
    }

    @Override
    public int getItemCount(){
        return contenidoPedidorecycler.size();

    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView id_content,name,cant,totl,price,extrs,image,section,solitatio_cancel,send_kitchen,confirm_yes,confirm_no,nota_meser;
        String status="preparado";
        String id_meser,meserAsignd;
        ConstraintLayout box_content_velo_mecero;
        public ViewHolderRecycler(View itemView) {
            super(itemView);
            id_content =(TextView)itemView.findViewById(R.id.id);
            name =(TextView)itemView.findViewById(R.id.nombre);
            cant =(TextView)itemView.findViewById(R.id.cantidad);
            totl =(TextView)itemView.findViewById(R.id.total);
            price =(TextView)itemView.findViewById(R.id.precio);
            extrs =(TextView)itemView.findViewById(R.id.extras);
            image =(TextView)itemView.findViewById(R.id.imagen);
            solitatio_cancel =(TextView)itemView.findViewById(R.id.solicitar_cancelacion);
            send_kitchen =(TextView)itemView.findViewById(R.id.comenzar_prepara_cocina);

            section =(TextView)itemView.findViewById(R.id.seccion);
            nota_meser=itemView.findViewById(R.id.nota_mesero);

            box_content_velo_mecero=itemView.findViewById(R.id.caja_contenedor_velo_mecero);
            confirm_yes=itemView.findViewById(R.id.confirmar_si);
            confirm_no=itemView.findViewById(R.id.confirmar_no);

        }
    }

}

