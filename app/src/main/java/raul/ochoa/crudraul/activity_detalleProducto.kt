package raul.ochoa.crudraul

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_detalleProducto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_producto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtUUIDDetalle = findViewById<TextView>(R.id.txtUuidDetalle)
        val txtNombreDetalle = findViewById<TextView>(R.id.txtNombreDetalle)
        val txtPrecioDetalle = findViewById<TextView>(R.id.txtPreciodetalle)
        val txtcantidadDetalle = findViewById<TextView>(R.id.txtCantidadDetalle)

        val UUIDProductos = intent.getStringExtra("uuid")
        val NombreProductos = intent.getStringExtra("nombreProducto")
        val PrecioProductos = intent.getIntExtra("precio", 0)
        val cantidadProductos = intent.getIntExtra("cantidad", 0)

        txtUUIDDetalle.text = UUIDProductos
        txtNombreDetalle.text = NombreProductos
        txtPrecioDetalle.text = PrecioProductos.toString()
        txtcantidadDetalle.text = cantidadProductos.toString()
        val imgAtras = findViewById<ImageView>(R.id.imgAtras)
        imgAtras.setOnClickListener {
            val pantallaatras = Intent(this, MainActivity::class.java)
            startActivity(pantallaatras)
        }
    }
}