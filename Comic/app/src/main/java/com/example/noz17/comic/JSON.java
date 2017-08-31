package com.example.noz17.comic;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;


@SuppressWarnings("ConstantConditions")
public class JSON extends AppCompatActivity {
    ImageView Comic_Dia; //Es el view donde se presenta la imagen
    TextView Descripcion, Capitulo, Titulo, Dia; //Parte de texto descripcion, titulo, el dia del comic y el capitulo
    Button Boton_Siguiente, Boton_Anterior, Boton_Ir; //Botones de navegacion
    EditText Capitulo_Busqueda; //Donde se ingresa el numero de Comic a ir
    String[] Objeto_JSON = new String[5]; //Cadenas donde se guardarán temporalmente los textos y la URL de la imagen
    int Numero_Comic = 1, Numero_del_Dia = 0;
    //Numero comic es el numero de el comic actual Numero anterior es el comic
    //De antes y el numero del comic del dia para algunas validaciones
    String Direccion_Comic = "";//Aca se guarda la direccion(URL) del comic editada
    String Json_Comic = "https://xkcd.com/info.0.json";
    JSONObject Descripcion_Comic;
    int Boton_Presionado = 0;
    //Si Boton_Presionado es 0 se presionó Boton_Anterior
    //Si Boton_Presionado es 1 se presionó Boton_Siguiente
    //Si Boton_Presionado es 2 se presionó Boton_Ir

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide(); //Hace que se elimine la barra de titulo
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Oculta la ActionBar
        setContentView(R.layout.activity_json); //Aplica los cambios para la FullScreen
        //Se inicializan los view correspodiente
        Capitulo_Busqueda = (EditText) findViewById(R.id.Capitulo_Busqueda);
        Descripcion = (TextView) findViewById(R.id.textView);
        Capitulo = (TextView) findViewById(R.id.Capitulo);
        Dia = (TextView) findViewById(R.id.Dia);
        Titulo = (TextView) findViewById(R.id.Titulo);
        Comic_Dia = (ImageView) findViewById(R.id.imageView);
        Comic_Dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FullScreen = new Intent(JSON.this, Comic_FullScreen.class);
                FullScreen.putExtra("URL",Objeto_JSON[3]);
                startActivity(FullScreen);
            }
        });
        Boton_Siguiente = (Button) findViewById(R.id.Boton_Siguiente);//Casting para convertir el boton XML en Objeto Java
        Boton_Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//Se crea un CallBack para el boton
                Numero_Comic = Numero_Comic + 1; //Se aumenta en uno el Numero de comic
                Direccion_Comic = Numero_Comic + "/"; //Se pone el numero ya aumentado en 1 y se le agrega el "/"
                Json_Comic = "https://xkcd.com/" + Direccion_Comic + "info.0.json";//Se agrega el numero a la URL
                Boton_Presionado = 1; //Variable bandera para obtener que boton se utilizó (Mas adelante se explica la funcion)
                new Operador_JSON().execute(Json_Comic); //Se crea un nuevo proceso Asincrono para cargar el comic siguiente
            }
        });

        Boton_Anterior = (Button) findViewById(R.id.Boton_Anterior);//Casting para convertir el boton XML en Objeto Java
        Boton_Anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Numero_Comic == 1) { //Validando que no se pueda ir un comic antes del primero
                    Toast T = Toast.makeText(JSON.this, "No existe un comic anterior", Toast.LENGTH_SHORT);//Crea mensaje de error
                    T.show();//Muestra el mensaje
                }

                Numero_Comic = Numero_Comic - 1; //Se le resta 1 al numero del comic
                Direccion_Comic = Numero_Comic + "/"; //Se agrega el nuevo numero mas "/"
                Json_Comic = "https://xkcd.com/" + Direccion_Comic + "info.0.json"; //Se agrega el numero para obtener la direccion final
                Boton_Presionado = 0; //Variable bandera para obtener que boton se utilizó (Mas adelante se explica la funcion)
                new Operador_JSON().execute(Json_Comic); //Crea un nuevo proceso Asincrono para cargar el comic anterior
            }
        });

        Boton_Ir = (Button) findViewById(R.id.Boton_Ir); //Casting para convertir el boton XML en Objeto Java
        Boton_Ir.setOnClickListener(new View.OnClickListener() {//Se crea un CallBack para el boton
            @Override
            public void onClick(View view) { //Metodo a realizar cuando presionamos el boton ir
                Numero_Comic = Integer.parseInt("0" + String.valueOf(Capitulo_Busqueda.getText()));
                //Numero Comic siempre va a contener un 0 esto nos ayuda a evitar errores de busqueda o MissMatch
                //Se castea a entero cualquier valor que tenga el EditText
                Capitulo_Busqueda.setText("");//Se limpia el EditText para la siguiente iteraccion

                if (Numero_Comic == 0 || Numero_Comic > Numero_del_Dia) { //Se valida que el numero de comic no sea mayor al ultimo Comic ni sea 0 retornado de el metodo anterior
                    Toast T = Toast.makeText(JSON.this, "No existe este comic :)", Toast.LENGTH_SHORT);//Se crea un mensaje de error
                    T.show();//Muestra el mensaje de error
                }

                Direccion_Comic = Numero_Comic + "/"; //Se concatena el numero antes casteado mas "/"
                if (Numero_Comic >= Numero_del_Dia) { //Si el numero es mayor o igual al Comic de Hoy Direccion será la direccion de comic actual
                    Direccion_Comic = ""; //Se borra lo que se tenia antes
                }

                Json_Comic = "https://xkcd.com/" + Direccion_Comic + "info.0.json";
                //Si hay direccion valida se añadirá a la URL el numero mas "/"
                //Si no hay unicamente quedará la URL principal

                Boton_Presionado = 2;//Variable Bandera para obtener el boton presionado
                new Operador_JSON().execute(Json_Comic);//Se crea una tarea Asincrona que nos lleva al comic que deseamos
            }
        });
        new Operador_JSON().execute(Json_Comic); //Este es del metodo OnCreate y es la primer tarea que se crea siempre
    }

    private class Operador_JSON extends AsyncTask<String, String, String[]> {//Clase Asincrona que recibe parametros Cadena en sus metodos
        boolean Validacion_Booleando; //Variable bandera

        @Override
        protected void onPreExecute() {//Este metodo de la tarea Asincrona es el primero en ejecutarse
            if (Validacion_Booleando = ConexionInternet.verificaConexion(JSON.this)) {//Se hace mencion a otra clase
                //Se desactivan los botones para evitar que el usuario pueda crashear la app
                Boton_Siguiente.setEnabled(false);
                Boton_Anterior.setEnabled(false);
                Boton_Ir.setEnabled(false);

            } else { //Si la validacion retorna falso significa que no hay conexion a Internet
                this.cancel(true);//Se cancela la tarea para evitar errores
                Intent i = new Intent(getApplicationContext(), Espera.class); //Se crea una nueva Activity de error de conexion
                startActivity(i);//Se lanza la activity de error de conexion
            }
        }

        @Override
        protected String[] doInBackground(String... url) {//Este metodo se empieza luego de el onPreExecute y es el que siempre se ejecuta durante la app este corriendo
            try {//Se pone un Try-Catch para evitar que la app al lanzar un error se cierre
                Descripcion_Comic = JsonParser.readJsonFromUrl(url[0]);//Se utiliza un Objeto para guardar las cadenas del objeto JSON desde la web
                Objeto_JSON[0] = Descripcion_Comic.getString("alt");        //Se obtiene la descripcion del comic
                Objeto_JSON[1] = Descripcion_Comic.getString("num");        //Se obtiene el numero del comic
                Objeto_JSON[2] = Descripcion_Comic.getString("safe_title"); //Se obtiene el titulo del comic
                Objeto_JSON[3] = Descripcion_Comic.getString("img");        //Se obtiene la URL de la imagen
                Objeto_JSON[4] = Descripcion_Comic.getString("day");        //Se obtiene el dia en que fue subido
            } catch (JSONException | IOException e) {//Obtiene los errores de conexion
                e.printStackTrace();
                Objeto_JSON[0] = "No existe"; //Se asigna a la cadena 0 para validar la existencia del comic
            }

            return Objeto_JSON;//Se retorna un objeto en este caso al onPostExecute
        }

        @Override
        protected void onPostExecute(String[] stringFromDoInBackground) {//Este metodo es el que se ejecuta luego del doBackgroun y es el final
            if (Objeto_JSON[0].equals("No existe")) {//Validamos si se retorno que el comic no existia
                if (Boton_Presionado == 0)           //Si el boton presionado fue 0 (Boton_Anterior)
                    Boton_Anterior.callOnClick();    //Se llama al CallBack(Se hace click en el boton)
                else if (Boton_Presionado == 1) {    //Si el boton presionado fue 1 (Boton_Siguiente)
                    Boton_Siguiente.callOnClick();   //Se llama al CallBack(Se hace click en el boton)
                } else if (Boton_Presionado == 2) {  //Si el boton presionado fue 2 (Boton_Ir)
                    Toast.makeText(JSON.this, "No existe el comic", Toast.LENGTH_SHORT).show(); //Se manda advertencia
                    Capitulo_Busqueda.setText("1");  //Se le asigna un numero al EditText para que nos lleve al primer comic
                    Boton_Ir.callOnClick();          //Se llama al CallBack
                }
            } else { //Si en cambio se logra la conexion significa que el comic existe
                Descripcion.setText(stringFromDoInBackground[0]);//Se asigna la descripcion obtenida
                Numero_Comic = Integer.parseInt(stringFromDoInBackground[1]);//Se castea el numero y se actualiza
                if (Numero_del_Dia == 0)//Cuando se accede por primera vez y el numero del dia no esta definido
                    Numero_del_Dia = Numero_Comic;//Se asigna el numero del primer comic abierto(Que siempre será el del dia)
                Capitulo.setText("Capitulo (N°" + Numero_Comic + ")"); //Se pone el numero obtenido en el TextView
                Titulo.setText(stringFromDoInBackground[2]);        //Se pone el titulo
                Picasso.with(JSON.this).load(stringFromDoInBackground[3]).into(Comic_Dia); //Se carga en el ImageView con Picasso la imagen desde el URL obtenido
                Dia.setText("Dia " + stringFromDoInBackground[4]);
                //Se activan los botones
                Boton_Siguiente.setEnabled(true);
                Boton_Anterior.setEnabled(true);
                Boton_Ir.setEnabled(true);

                if (Numero_Comic == 1)                  //Si el numero del comic es 1 se valida que no pueda ir hacia atras
                    Boton_Anterior.setEnabled(false);   //desactivando el boton

                if (Numero_Comic == Numero_del_Dia)     //Si el numero del comic es el comic del dia
                    Boton_Siguiente.setEnabled(false);  //Se desactiva el boton
            }
        }
    }
}
