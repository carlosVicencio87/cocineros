package com.creathor.cocineros;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterPedidosCocina extends RecyclerView.Adapter<AdapterPedidosCocina.ViewHolderRecycler>{
private ArrayList<ListaPedidosCocinaRecycler> pedidosCocinaRecycler;
        ViewHolderRecycler viewholderListaPedidos;
private  RecyclerView recyclerView;
private Context context;
private String id,mesa,comanda,precio,fecha_ingreso,mecero_asignado,id_mesero,fecha_entrega,fecha_final,estadoPedido;
private TextView aceptar_pedido;
private AdapterPedidosCocina activity;
private LinearLayout caja_mecero_asignado;


public AdapterPedidosCocina(ArrayList<ListaPedidosCocinaRecycler> pedidosCocinaRecycler )
        {
        this.pedidosCocinaRecycler =pedidosCocinaRecycler;
        }
@Override
public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1,parent,false);
        context=parent.getContext();
        vista.setFocusable(true);
        return new ViewHolderRecycler(vista);

        }

@Override
public void onBindViewHolder(@NonNull AdapterPedidosCocina.ViewHolderRecycler holder, int position) {
        viewholderListaPedidos =holder;
        id = pedidosCocinaRecycler.get(position).getId();
        mesa= pedidosCocinaRecycler.get(position).getMesa();
        comanda= pedidosCocinaRecycler.get(position).getComanda();
        precio= pedidosCocinaRecycler.get(position).getPrecio();
        fecha_ingreso= pedidosCocinaRecycler.get(position).getFecha_ingreso();
        mecero_asignado= pedidosCocinaRecycler.get(position).getMecero_asignado();
        id_mesero=pedidosCocinaRecycler.get(position).getId_mesero();
        estadoPedido= pedidosCocinaRecycler.get(position).getEstadoPedido();

        /*fecha_entrega= pedidosrecycler.get(position).getFecha_entrega();
        fecha_final= pedidosrecycler.get(position).getFecha_final();*/




        holder.id_food.setText(id);
        holder.mes.setText(mesa);
        holder.comand.setText(comanda);
        holder.price.setText(precio);
        holder.date_star.setText(fecha_ingreso);
        holder.mecer_asigned.setText(mecero_asignado);
        holder.id_meser.setText(id_mesero);
         holder.statePedido.setText(estadoPedido);


      /*  holder.date_entrega.setText(fecha_entrega);
        holder.date_end.setText(fecha_final);*/

       /* if (!estadoPedido.equals("")){

            holder.box_decisiones.setVisibility(View.GONE);
            holder.box_preparando.setVisibility(View.VISIBLE);
            holder.box_mecero_asignado.setVisibility(View.VISIBLE);
            holder.mecer_asigned.setText(mecero_asignado);

            Log.e("ya_esta_mecero","2"+mecero_asignado);

        }
        else{
            holder.mecer_asigned.setVisibility(View.GONE);
            holder.box_decisiones.setVisibility(View.VISIBLE);
            holder.box_preparando.setVisibility(View.GONE);

        }*/

        holder.verify_order.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        int posicion=holder.getAdapterPosition();
        id = pedidosCocinaRecycler.get(posicion).getId();
        Log.e("id","2"+id);
        ((Estacion)context).verificar_detalle_pedido(id);



        }
        });
        Log.e("meceero","1"+mecero_asignado);


        //holder.list_pedidos.setText(lista_pedidos);
        }

@Override
public int getItemCount(){
        return pedidosCocinaRecycler.size();

        }
public class ViewHolderRecycler extends RecyclerView.ViewHolder {
    TextView id_food,mes,comand,price,date_star,mecer_asigned,verify_order,rech_pedido,statePedido,id_meser;
    LinearLayout box_mecero_asignado,box_decisiones,box_preparando;


    public ViewHolderRecycler(View itemView) {
        super(itemView);
        id_food =(TextView)itemView.findViewById(R.id.id);
        verify_order =(TextView)itemView.findViewById(R.id.verificar_detalle_pedido);
        rech_pedido =(TextView)itemView.findViewById(R.id.rechazar_pedido);
        mes =(TextView)itemView.findViewById(R.id.mesa);
        comand =(TextView)itemView.findViewById(R.id.comanda);
        price =(TextView)itemView.findViewById(R.id.precio);
        date_star =(TextView)itemView.findViewById(R.id.fecha_ingreso);
        mecer_asigned =(TextView)itemView.findViewById(R.id.mecero_asignado);
        id_meser=itemView.findViewById(R.id.id_mesero);
        statePedido=itemView.findViewById(R.id.estadoPedido);
        box_decisiones = (LinearLayout) itemView.findViewById(R.id.caja_decisiones);



    }
}

}