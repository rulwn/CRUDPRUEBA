package Modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {
            val url = "jdbc:oracle:thin:@10.10.1.1:1521:xe"
            val usuario = "RAUL_DEVELOPER"
            val contrasena = "deltawins"
            val connection = DriverManager.getConnection(url, usuario, contrasena)
            return connection
        }catch (e: Exception){
            println("Error: $e")
            return null
        }
    }
}