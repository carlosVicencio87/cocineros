package com.creathor.cocineros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Estacion extends AppCompatActivity {
    private LinearLayout caja_cocina_pedidos_recycler,caja_recycler_cocina;
    private ConstraintLayout caja_detalle_pedido;
    private RecyclerView lista_cocina_recycler,contenido_pedido_recycler;
    private AdapterPedidosCocina adapterPedidosCocina;
    private ArrayList <ListaPedidosCocinaRecycler>listaPedidosCocinaRecyclers;
    private AdapterContenidoPedido adapterContenidoPedido;
    private ArrayList <ListaContenidoPedidos> listaContenidoPedidos;
    private SharedPreferences id_SesionSher,idSher,nombreCocineroSher;
    private String idSesion,id_mesero,cocineroAsignado,id_pedido_actual,strContenido,strNotaMesero;
    private static String SERVIDOR_CONTROLADOR;
    private Context context;
    private Estacion activity;
    private JSONArray json_pedidos_cocina,json_contenido_pedido;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_estacion);

        caja_cocina_pedidos_recycler=findViewById(R.id.caja_cocina_pedidos_recycler);
        caja_recycler_cocina=findViewById(R.id.caja_recycler_cocina);
        lista_cocina_recycler=findViewById(R.id.lista_cocina_recycler);
        caja_detalle_pedido=findViewById(R.id.caja_detalle_pedido);
        contenido_pedido_recycler=findViewById(R.id.contenido_pedido_recycler);

        activity = this;
        context=this;
        SERVIDOR_CONTROLADOR = new Servidor().getIplocal();

        listaPedidosCocinaRecyclers=new ArrayList<>();
        lista_cocina_recycler.setLayoutManager(new LinearLayoutManager(Estacion.this,LinearLayoutManager.VERTICAL,false));
        listaContenidoPedidos=new ArrayList<>();
        contenido_pedido_recycler.setLayoutManager(new LinearLayoutManager(Estacion.this,LinearLayoutManager.VERTICAL,false));

        id_SesionSher=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        idSesion= id_SesionSher.getString("idSesion","no hay");
        idSher=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        id_mesero = idSher.getString("id","no hay");

        nombreCocineroSher=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        cocineroAsignado = nombreCocineroSher.getString("nombre","no hay");
        pedir_pedidos();
   }
   public  void pedir_pedidos(){
       RequestQueue requestQueue= Volley.newRequestQueue(this);
       StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"pedidosCocinerosEstacion.php",
               new Response.Listener<String>()
               {
                   @Override
                   public void onResponse(String response) {

                       String limpio=response;
                       Log.e("jsonObject33333:",""+response);
                       Log.e("jsonObject333333:",""+limpio);



                       try {

                           json_pedidos_cocina=new JSONArray(response);
                           for (int i=0;i<json_pedidos_cocina.length();i++){
                               JSONObject jsonObject = json_pedidos_cocina.getJSONObject(i);

                               //Log.e("nombreMovies", String.valueOf(jsonObject));

                               String strId = jsonObject.getString("id");
                               String strMesa = jsonObject.getString("mesa");
                               String strComanda = jsonObject.getString("comanda");
                               String strPrecio= jsonObject.getString("precio");
                               String strFecha_ingreso = jsonObject.getString("fecha_ingreso");
                               String strId_mesero=jsonObject.getString("id_mesero");
                               String strMecero=jsonObject.getString("meseroAsignado");

                               String strEstado=jsonObject.getString("estado");
                               strContenido=jsonObject.getString("contenido");
                               strNotaMesero=jsonObject.getString("nota_mesero");

                               String strFecha_entrega = jsonObject.getString("fecha_entrega");
                               String strFecha_final = jsonObject.getString("fecha_final");

                               listaPedidosCocinaRecyclers.add(new ListaPedidosCocinaRecycler(strId,strMesa,strComanda,strPrecio,strFecha_ingreso,strId_mesero,strMecero,strEstado,strContenido,strNotaMesero));
                               Log.e("idm",strId);
                               Log.e("idmesero",strId_mesero);
                               Log.e("contenidos",strContenido);
                               Log.e("notamesero",strNotaMesero);

                           }

                           adapterPedidosCocina=new AdapterPedidosCocina(listaPedidosCocinaRecyclers);
                           lista_cocina_recycler.setAdapter(adapterPedidosCocina);
                           //recycler_movies.scrollToPosition(0);



                           //recycler_movies.getChildAt(0).findViewById(R.id.contenedor).requestFocus();
                           //bloquearMenu();


                       } catch (JSONException e) {
                           Log.e("errorRespuestaMovies", String.valueOf(e));
                       }
                       Log.e("jsonapedidos:",""+ json_pedidos_cocina);
                   }
               },
               new Response.ErrorListener() {

                   @Override
                   public void onErrorResponse(VolleyError error) {

                   }



               }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               HashMap<String,String> map = new HashMap<>();
               map.put("id_mesero", id_mesero);
               map.put("cocineroAsignado",cocineroAsignado);
               return map;
           }
       };
       requestQueue.add(request);
   }
   public void verificar_detalle_pedido(String id_pedido){
        caja_cocina_pedidos_recycler.setVisibility(View.GONE);
       caja_detalle_pedido.setVisibility(View.VISIBLE);
       id_pedido_actual=id_pedido;

       Log.e("id_activyty",""+id_pedido);
       try {
           json_contenido_pedido=new JSONArray(strContenido);
           for(int i=0;i<json_contenido_pedido.length();i++){
               JSONObject jsonObject = json_contenido_pedido.getJSONObject(i);
               Log.e("jsonObject2:",""+jsonObject);
               String strId = jsonObject.getString("id");
               String strNombre = jsonObject.getString("nombre");
               String strCantidad = jsonObject.getString("cantidad");
               String strTotal= jsonObject.getString("total");
               String strPrecio = jsonObject.getString("precio");
               String strExtras=jsonObject.getString("extras");
               String strImagen=jsonObject.getString("imagen");
               String strSeccion=jsonObject.getString("seccion");

               Log.e("ID",strId);
               Log.e("NOMBRE",strNombre);
               Log.e("CANTIDAD",strCantidad);
               Log.e("TOTAL",strTotal);
               Log.e("PRECIO",strPrecio);
               Log.e("EXTRAS",strExtras);
               Log.e("IMAGEN",strImagen);
               Log.e("SECCION",strSeccion);
               listaContenidoPedidos.add(new ListaContenidoPedidos(strId,strNombre,strCantidad,strTotal,strPrecio,strExtras,strImagen,strSeccion,strNotaMesero));


           }
           adapterContenidoPedido=new AdapterContenidoPedido(listaContenidoPedidos);
           contenido_pedido_recycler.setAdapter(adapterContenidoPedido);
           Log.e("jsonContendio",""+json_contenido_pedido);

       } catch (JSONException e) {
           Log.e("errorRespuesJSON", String.valueOf(e));
       }

   }
   public void comenzarPreparacion(String id_cocina){
       id_contenido_actual=id_contenido;
       notaMesero_actual=notaMesero;
       caja_pedido_cliente.setVisibility(View.GONE);
       caja_lista_pedidos_recycler.setVisibility(View.VISIBLE);
       for (int i=0;i<listaPedidosRecyclers.size();i++){
           String id_tmp=listaPedidosRecyclers.get(i).getId();

           if(id_tmp.equals(id_pedido_actual)){
               id_encontrada=listaPedidosRecyclers.get(i).getId();
               listaPedidosRecyclers.remove(i);


               executorService.execute(new Runnable() {
                   @Override
                   public void run() {
                       enviar_pedido_cocina();
                       Log.e("entnedi_señor_calamardo","y esto igual");
                   }
               });
           }
       }
       lista_pedidos_recycler.setAdapter(adapterListaPedidos);
       adapterListaEspera=new AdapterListaPedidos(listaPedidosAsignados);
       lista_espera_recycler.setAdapter(adapterListaEspera);



   }
}