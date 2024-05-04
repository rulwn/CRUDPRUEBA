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

        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                //Guardar datos
                val claseConexion = ClaseConexion().cadenaConexion()
                //crear una var que contenga un PreparedStatement
                val addProducto = claseConexion?.prepareStatement("insert into tbProductos(nombreProducto, precio, cantidad) values (?, ?, ?)")!!
                addProducto.setString(1, txtNombre.text.toString())
                addProducto.setInt(2, txtPrecio.text.toString().toInt())
                addProducto.setInt(3, txtCantidad.text.toString().toInt())
                addProducto.executeUpdate()
            }
        }

        //////////////////////////////////Mostrar//////////////////////////////////////
        val rcvProductos = findViewById<RecyclerView>(R.id.rcvProductos)
        //asignar un layout al rcv
        rcvProductos.layoutManager = LinearLayoutManager(this)

        fun obtenerDatos(): List<dataclassProductos>{
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultset = statement?.executeQuery("Select * from tbProductos")!!
            val productos = mutableListOf<dataclassProductos>()
            while (resultset.next()){
                val nombre = resultset.getString("nombreProducto")
                val producto = dataclassProductos(nombre)
                productos.add(producto)
            }
            return productos
        }
        //Asignar un adaptador
        CoroutineScope(Dispatchers.IO).launch{
           val productosDB = obtenerDatos()
           withContext(Dispatchers.Main){
               val miAdaptador = Adaptador.Adaptador(productosDB)
               rcvProductos.adapter = miAdaptador
           }
        }
    }
}