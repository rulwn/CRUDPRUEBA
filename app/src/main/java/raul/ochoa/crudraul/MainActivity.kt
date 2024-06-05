package raul.ochoa.crudraul

import Modelo.ClaseConexion
import Modelo.dataclassProductos
import RecyclerViewHelper.Adaptador
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.txtProductCard)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //1 Mandar a llamar todos los elementos de la pantalla
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        val txtCantidad = findViewById<EditText>(R.id.txtCantidad)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        fun limpiar() {
          txtNombre.setText("")
          txtPrecio.setText("")
          txtCantidad.setText("")
        }
        //////////////////////////////////TODO: Mostrar//////////////////////////////////////
        val rcvProductos = findViewById<RecyclerView>(R.id.rcvProductos)
        //asignar un layout al rcv
        rcvProductos.layoutManager = LinearLayoutManager(this)

        fun obtenerDatos(): List<dataclassProductos>{
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultset = statement?.executeQuery("Select * from tbProductos")!!
            val productos = mutableListOf<dataclassProductos>()
            while (resultset.next()){
                val uuid = resultset.getString("uuid")
                val nombre = resultset.getString("nombreProducto")
                val precio = resultset.getInt("precio")
                val cantidad = resultset.getInt("cantidad")
                val producto = dataclassProductos(uuid, nombre, precio, cantidad)
                productos.add(producto)
            }
            return productos
        }
        //Asignar un adaptador
        CoroutineScope(Dispatchers.IO).launch{
            val productosDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val miAdaptador = Adaptador(productosDB)
                rcvProductos.adapter = miAdaptador
            }
        }
        ////////////////////////////TODO: Guardardatos////////////////////////////////////////
        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                //Guardar datos
                val claseConexion = ClaseConexion().cadenaConexion()
                //crear una var que contenga un PreparedStatement
                val addProducto = claseConexion?.prepareStatement("insert into tbProductos(uuid, nombreProducto, precio, cantidad) values (?, ?, ?, ?)")!!
                addProducto.setString(1, UUID.randomUUID().toString())
                addProducto.setString(2, txtNombre.text.toString())
                addProducto.setInt(3, txtPrecio.text.toString().toInt())
                addProducto.setInt(4, txtCantidad.text.toString().toInt())
                addProducto.executeUpdate()

                val newproducto = obtenerDatos()
                withContext(Dispatchers.Main){
                    (rcvProductos.adapter as? Adaptador)?.Actuzalizarlista(newproducto)
                }
            }
            //limpiar()
        }


    }
}