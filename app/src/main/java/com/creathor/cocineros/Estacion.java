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
import android.widget.TextView;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Estacion extends AppCompatActivity {

    private ExecutorService executorService;
    private LinearLayout caja_cocina_pedidos_recycler,caja_recycler_cocina,caja_lista_pedidos,caja_lista_espera_recycler,caja_pedidos_espera_l;
    private ConstraintLayout caja_detalle_pedido,empezarPreparacion,entregarPedido;
    private RecyclerView lista_cocina_recycler,contenido_pedido_recycler,lista_espera_recycler;
    private AdapterPedidosCocina adapterPedidosCocina;
    private ArrayList <ListaPedidosCocinaRecycler>listaPedidosCocinaRecyclers;
    private AdapterContenidoPedido adapterContenidoPedido;
    private ArrayList <ListaContenidoPedidos> listaContenidoPedidos,listaContenidoPreparado;
    private AdapterContenidoPedidoPreparado adapterContenidoPedidoPreparado;
    private TextView confirmar_comienzo_preparacion,confirmar_no,no_entregar,confirmar_entrega_pedido;
    private SharedPreferences id_SesionSher,idSher,nombreCocineroSher;
    private String idSesion,id_mesero,cocineroAsignado,id_pedido_actual,
            strContenido,strEstatus,id_encontrada,strId_mesero,strMecero, strIdPedido,strIdProducto,id_producto2;
    private static String SERVIDOR_CONTROLADOR;
    private Context context;
    private Estacion activity;
    private JSONArray json_pedidos_cocina,json_contenido_pedido;
    private int indice_actualStr;

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
        executorService= Executors.newSingleThreadExecutor();
        caja_lista_espera_recycler=findViewById(R.id.caja_lista_espera_recycler);
        caja_pedidos_espera_l=findViewById(R.id.caja_pedidos_espera_l);
        caja_lista_pedidos=findViewById(R.id.caja_lista_pedidos);
        lista_espera_recycler=findViewById(R.id.lista_espera_recycler);
        empezarPreparacion=findViewById(R.id.empezarPreparacion);
        confirmar_no=findViewById(R.id.confirmar_no);
        entregarPedido=findViewById(R.id.entregarPedido);
        no_entregar=findViewById(R.id.no_entregar);
        confirmar_comienzo_preparacion=findViewById(R.id.confirmar_comienzo_preparacion);
        confirmar_entrega_pedido=findViewById(R.id.confirmar_entrega_pedido);
        activity = this;
        context=this;
        SERVIDOR_CONTROLADOR = new Servidor().getIplocal();
        listaContenidoPedidos=new ArrayList<>();
        lista_cocina_recycler.setLayoutManager(new LinearLayoutManager(Estacion.this,LinearLayoutManager.VERTICAL,false));
        listaPedidosCocinaRecyclers=new ArrayList<>();
        contenido_pedido_recycler.setLayoutManager(new LinearLayoutManager(Estacion.this,LinearLayoutManager.VERTICAL,false));
        listaContenidoPreparado=new ArrayList<>();
        lista_espera_recycler.setLayoutManager(new LinearLayoutManager(Estacion.this,LinearLayoutManager.VERTICAL,false));

        id_SesionSher=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        idSesion= id_SesionSher.getString("idSesion","no hay");
        idSher=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        id_mesero = idSher.getString("id","no hay");

        nombreCocineroSher=getSharedPreferences("Usuario",this.MODE_PRIVATE);
        cocineroAsignado = nombreCocineroSher.getString("nombre","no hay");
        pedir_pedidos();

        caja_pedidos_espera_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja_lista_espera_recycler.setVisibility(View.VISIBLE);
                caja_recycler_cocina.setVisibility(View.GONE);
                pedir_pedidos();
            }
        });
        caja_lista_pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja_lista_espera_recycler.setVisibility(View.GONE);
                caja_recycler_cocina.setVisibility(View.VISIBLE);
                pedir_pedidos();
            }
        });
        confirmar_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja_cocina_pedidos_recycler.setVisibility(View.VISIBLE);
                empezarPreparacion.setVisibility(View.GONE);
            }
        });
        confirmar_comienzo_preparacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    for (int i=0;i<json_pedidos_cocina.length();i++){
                        JSONObject jsonObject = json_pedidos_cocina.getJSONObject(i);
                        Log.e("todos los pedidos1", String.valueOf(jsonObject));
                        String strIdPedido = jsonObject.getString("id");
                        Log.e("strIdsPEDIDOS",strIdPedido);
                        if (strIdPedido==id_pedido_actual){
                            String strContendio = jsonObject.getString("contenido");
                            Log.e("strIdsContenido",strContendio);
                            json_contenido_pedido=new JSONArray(strContendio);
                            for (int i2=0;i2<json_contenido_pedido.length();i2++){
                                JSONObject jsonObject2 = json_contenido_pedido.getJSONObject(i2);
                                Log.e("productos individuales", String.valueOf(jsonObject2));
                                strIdProducto = jsonObject2.getString("id");
                                Log.e("idCiclada",strIdProducto);
                                Log.e("idCiclada",id_producto2);
                                String strNombreProducto = jsonObject2.getString("nombre");
                                if (strIdProducto.equals(id_producto2)){
                                    String strEstatus2 = jsonObject2.getString("estatus");
                                    Log.e("esta",strEstatus2);
                                    jsonObject2.put("estatus",strEstatus);
                                    Log.e("aaaaa",jsonObject2.get("estatus").toString());
                                    strContenido= String.valueOf(json_contenido_pedido);
                                    Log.e("nuevoContenido",strContenido);
                                    executorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            actualizar_pedido_cocina();

                                            Log.e("se envio a cocina","revisar cocina");
                                        }
                                    });
                                }
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        confirmar_entrega_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    for (int i=0;i<json_pedidos_cocina.length();i++){
                        JSONObject jsonObject = json_pedidos_cocina.getJSONObject(i);
                        Log.e("todos los pedidos1", String.valueOf(jsonObject));
                        String strIdPedido = jsonObject.getString("id");
                        Log.e("strIdsPEDIDOS",strIdPedido);
                        if (strIdPedido==id_pedido_actual){
                            String strContendio = jsonObject.getString("contenido");
                            Log.e("strIdsContenido",strContendio);
                            json_contenido_pedido=new JSONArray(strContendio);
                            for (int i2=0;i2<json_contenido_pedido.length();i2++){
                                JSONObject jsonObject2 = json_contenido_pedido.getJSONObject(i2);
                                Log.e("productos individuales", String.valueOf(jsonObject2));
                                strIdProducto = jsonObject2.getString("id");
                                Log.e("idCiclada",strIdProducto);
                                Log.e("idCiclada",id_producto2);
                                String strNombreProducto = jsonObject2.getString("nombre");
                                if (strIdProducto.equals(id_producto2)){
                                    String strEstatus2 = jsonObject2.getString("estatus");
                                    Log.e("esta",strEstatus2);
                                    jsonObject2.put("estatus",strEstatus);
                                    Log.e("aaaaa",jsonObject2.get("estatus").toString());
                                    strContenido= String.valueOf(json_contenido_pedido);
                                    Log.e("nuevoContenido",strContenido);
                                    executorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            actualizar_pedido_cocina_preparado();

                                            Log.e("se envio a cocina","revisar cocina");
                                        }
                                    });
                                }
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        no_entregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja_cocina_pedidos_recycler.setVisibility(View.VISIBLE);
                entregarPedido.setVisibility(View.GONE);
            }
        });
   }

   public  void pedir_pedidos(){
       RequestQueue requestQueue= Volley.newRequestQueue(this);
       StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"pedidosCocinerosEstacion.php",
               new Response.Listener<String>()
               {
                   @Override
                   public void onResponse(String response) {
                       listaContenidoPedidos.clear();
                       listaContenidoPreparado.clear();
                       String limpio=response;
                       Log.e("jsonObject33333:",""+response);
                       Log.e("jsonObject333333:",""+limpio);



                       try {

                           json_pedidos_cocina=new JSONArray(response);
                           for (int i=0;i<json_pedidos_cocina.length();i++){
                               JSONObject jsonObject = json_pedidos_cocina.getJSONObject(i);
                               Log.e("nombreMocontenidovies", String.valueOf(jsonObject));

                               //Log.e("nombreMovies", String.valueOf(jsonObject));

                               String strIdPedido = jsonObject.getString("id");
                               String strMesa = jsonObject.getString("mesa");
                               String strComanda = jsonObject.getString("comanda");
                               String strPrecio= jsonObject.getString("precio");
                               String strFecha_ingreso = jsonObject.getString("fecha_ingreso");
                               String strId_mesero=jsonObject.getString("id_mesero");
                               String strMecero=jsonObject.getString("meseroAsignado");

                               String strEstado=jsonObject.getString("estado");
                               strContenido=jsonObject.getString("contenido");
                               json_contenido_pedido=new JSONArray(strContenido);
                               for (int i2=0;i2<json_contenido_pedido.length();i2++){
                                   JSONObject jsonObject2 = json_contenido_pedido.getJSONObject(i2);
                                   Log.e("contenido", String.valueOf(jsonObject2));
                                   String strID=jsonObject2.getString("id");
                                   String strNombre = jsonObject2.getString("nombre");
                                   String strCantidad = jsonObject2.getString("cantidad");
                                   String strTotal = jsonObject2.getString("total");
                                   String strPrecio2 = jsonObject2.getString("precio");
                                   String strExtras = jsonObject2.getString("extras");
                                   String strNota_mesero=jsonObject2.getString("nota_mesero");
                                   String strEstatus=jsonObject2.getString("estatus");
                                   Log.e("estatus",strEstatus);

                                   if (strEstatus.equals(" ")){
                                       listaContenidoPedidos.add(new ListaContenidoPedidos(strID,strNombre,strCantidad,strTotal,strPrecio2,strExtras,strNota_mesero,strEstatus,strId_mesero,strMecero,strIdPedido));

                                   }

                                   if (strEstatus.equals("preparando")){
                                       listaContenidoPreparado.add(new ListaContenidoPedidos(strID,strNombre,strCantidad,strTotal,strPrecio2,strExtras,strNota_mesero,strEstatus,strId_mesero,strMecero,strIdPedido));

                                   }
                                   Log.e("nombre",strNombre);
                                   Log.e("cantidad",strCantidad);
                                   Log.e("total",strTotal);
                                   Log.e("precio2",strPrecio2);
                                   Log.e("extras",strExtras);
                                   Log.e("notaMesero",strNota_mesero);
                               }
                               adapterContenidoPedido=new AdapterContenidoPedido(listaContenidoPedidos);
                               lista_cocina_recycler.setAdapter(adapterContenidoPedido);
                               adapterContenidoPedidoPreparado=new AdapterContenidoPedidoPreparado(listaContenidoPreparado);
                               lista_espera_recycler.setAdapter(adapterContenidoPedidoPreparado);
                               String strFecha_entrega = jsonObject.getString("fecha_entrega");
                               String strFecha_final = jsonObject.getString("fecha_final");

                               Log.e("idm", strIdPedido);


                           }


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
public void entregarPedidoIndice(int indice_actual,String id_producto,String estatus,String idMeser,String meseroAsignd, String idPedid){

        Log.e("id_contenido","s"+id_producto);
        Log.e("estatus",estatus);
        Log.e("idMeser",idMeser);
        Log.e("idPedid",idPedid);
        caja_cocina_pedidos_recycler.setVisibility(View.GONE);
        entregarPedido.setVisibility(View.VISIBLE);
        Log.e("meseroAsignd",meseroAsignd);
        Log.e("indice", String.valueOf(indice_actual));
        strId_mesero=idMeser;
        strMecero=meseroAsignd;
        indice_actualStr=indice_actual;
        strEstatus="preparado";
        id_pedido_actual=idPedid;
        id_producto2=id_producto;
        Log.e("idContenido",id_producto2);
        Log.e("estatus",strEstatus);
        Log.e("idmesero",strId_mesero);
        Log.e("mesero",strMecero);
        Log.e("idPedido",id_pedido_actual);
        Log.e("estatus", String.valueOf(indice_actual));


    }

    public void enviarIndiceConNota(int indice_actual,String id_producto,String estatus,String idMeser,String meseroAsignd, String idPedid){

       Log.e("id_contenido","s"+id_producto);
       Log.e("estatus",estatus);
       Log.e("idMeser",idMeser);
       Log.e("idPedid",idPedid);
       caja_cocina_pedidos_recycler.setVisibility(View.GONE);
       empezarPreparacion.setVisibility(View.VISIBLE);
       Log.e("meseroAsignd",meseroAsignd);
       Log.e("indice", String.valueOf(indice_actual));
       strId_mesero=idMeser;
       strMecero=meseroAsignd;
       indice_actualStr=indice_actual;
       strEstatus=estatus;
       id_pedido_actual=idPedid;
       id_producto2=id_producto;
       Log.e("idContenido",id_producto2);
       Log.e("estatus",strEstatus);
       Log.e("idmesero",strId_mesero);
       Log.e("mesero",strMecero);
       Log.e("idPedido",id_pedido_actual);
       Log.e("estatus", String.valueOf(indice_actual));


   }

    public void actualizar_pedido_cocina()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"actualizar_pedido_cocina.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta4:",response);
                        if(response.equals("actualizado")){
                            caja_cocina_pedidos_recycler.setVisibility(View.VISIBLE);
                            empezarPreparacion.setVisibility(View.GONE);
                            Log.e("respuesta5:", String.valueOf(indice_actualStr));
                            listaContenidoPedidos.remove(indice_actualStr);
                            adapterContenidoPedido=new AdapterContenidoPedido(listaContenidoPedidos);
                            lista_cocina_recycler.setAdapter(adapterContenidoPedido);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("respuesta4Error:",error + "error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",  id_pedido_actual);
                map.put("id_mesero", strId_mesero);
                map.put("meseroAsignado",strMecero);
                map.put("contenido",strContenido);
                Log.e("id", id_pedido_actual);
                Log.e("idmesero",strId_mesero);
                Log.e("mesero",strMecero);
                Log.e("contenido",strContenido);

//                Log.e("nota",notaMesero_actual);

                return map;
            }
        };
        requestQueue.add(request);
    }

    public void actualizar_pedido_cocina_preparado()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,  SERVIDOR_CONTROLADOR+"actualizar_pedido_cocina_entregaado.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("respuesta4:",response);
                        if(response.equals("actualizado")){
                            caja_cocina_pedidos_recycler.setVisibility(View.VISIBLE);
                            empezarPreparacion.setVisibility(View.GONE);
                            Log.e("respuesta5:", String.valueOf(indice_actualStr));
                            listaContenidoPreparado.remove(indice_actualStr);
                            adapterContenidoPedidoPreparado=new AdapterContenidoPedidoPreparado(listaContenidoPreparado);
                            lista_espera_recycler.setAdapter(adapterContenidoPedidoPreparado);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("respuesta4Error:",error + "error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",  id_pedido_actual);
                map.put("id_mesero", strId_mesero);
                map.put("meseroAsignado",strMecero);
                map.put("contenido",strContenido);
                Log.e("id", id_pedido_actual);
                Log.e("idmesero",strId_mesero);
                Log.e("mesero",strMecero);
                Log.e("contenido",strContenido);

//                Log.e("nota",notaMesero_actual);

                return map;
            }
        };
        requestQueue.add(request);
    }
}