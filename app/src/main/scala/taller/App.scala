package taller

object App {
  def main(args: Array[String]): Unit = {
    val cifrador = new CifradosClasicos()

    println("--- PUNTO 1: Cesar Lineal ---")
    println(cifrador.cesar("casa", 3)) // Debería imprimir: fdvd

    println("\n--- PUNTO 2: Cesar Cola ---")
    println(cifrador.cesarCola("hola mundo", 1)) // Debería imprimir: ipmb nuoep

    println("\n--- PUNTO 3: Frecuencias ---")
    println(cifrador.frecuencias("casa")) // List(('a', 2), ('c', 1), ('s', 1))

    println("\n--- PUNTO 4: Desplazamiento Probable & Romper Cesar ---")
    val cifrado = cifrador.cesar("el mensaje secreto", 7)
    println(s"Mensaje Cifrado: $cifrado")
    println(s"Desplazamiento Estimado: ${cifrador.desplazamientoProbable(cifrado)}")
    println(s"Mensaje Rompido/Descifrado: ${cifrador.romperCesar(cifrado)}")

    println("\n--- PUNTO 5: Combinaciones & Vigenere ---")
    println(s"Combinaciones(3, 26): ${cifrador.combinaciones(3, 26)}") // 16250
    println(s"Vigenere('ataque', 'sol'): ${cifrador.vigenere("ataque", "sol")}") // shliip
  }
}