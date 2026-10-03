package taller

import scala.annotation.tailrec

/**
 * Taller 1 — cifrados clásicos con recursión.
 *
 * Solo se cifran las 26 letras minúsculas del alfabeto inglés; cualquier otro
 * carácter se copia sin cambio.
 */
class CifradosClasicos {

  type Mensaje = String
  type Clave = String

  // Una frecuencia asocia cada letra con las veces que aparece.
  type Frecuencias = List[(Char, Int)]

  val letras = 26
  val primera = 'a'.toInt

  def esMinuscula(c: Char): Boolean = c >= 'a' && c <= 'z'

  // Punto 1 -------------------------------------------------------------------

  /** César con recursión lineal: una operación pendiente por letra. */
  def cesar(m: Mensaje, k: Int): Mensaje = {
    // Función auxiliar para cifrar una sola letra
      def desplazar (c: Char): Char = {
        /**
        Para cifrar unicamente las minúsculas (desde la a hasta la z (abecedario inglés))
        (c - primera) es para la posicion de la letra (a=0 --- z= 25)
        +k define cuantas posiciones moveremos la letra
        % letras + letras) % letras): esta para evitar que el que el modulo nunca de negativo (sirve para k negativo y k > 26)
        y ya con primera + .... volvemos a letras
        */
        if (esMinuscula(c)) (primera + ((c - primera + k ) % letras + letras) % letras).toChar
        // espacios, digitos, mayusculas y signos pasan como si nada
        else c
      }

    // Caso por defecto, dara resultado vacio
    if (m.isEmpty) ""
    // recursion: se cifra la primera letra y se continua con la siguiente, la concatenación
    // queda pendiente: por ello crece la pila
    else desplazar(m.head).toString + cesar(m.tail, k)
  }

  // Punto 2 -------------------------------------------------------------------

  /**
   * El mismo César como proceso iterativo: espacio constante.
   * Cuando la función esté escrita, anótela con @tailrec: el compilador
   * comprueba que la llamada recursiva sea lo último que hace.
   */
  final def cesarCola(m: Mensaje, k: Int, acc: Mensaje = ""): Mensaje = ???

  // Punto 3 -------------------------------------------------------------------

  /**
   * Cuenta las letras minúsculas del mensaje, de mayor a menor frecuencia y,
   * en empate, en orden alfabético. El recorrido es recursivo de cola.
   */
  def frecuencias(m: Mensaje): Frecuencias = ???

  // Punto 4 -------------------------------------------------------------------

  /**
   * Supone que la letra más frecuente del mensaje cifrado es la 'e' del
   * original y devuelve la distancia entre las dos. Sin letras, cero.
   */
  def desplazamientoProbable(m: Mensaje): Int = ???

  def romperCesar(m: Mensaje): Mensaje = ???

  // Punto 5 -------------------------------------------------------------------

  /**
   * Cuántos mensajes de longitud n se forman con a letras sin dos iguales
   * seguidas.
   */
  def combinaciones(n: Int, a: Int): BigInt = ???

  /**
   * Vigenère: cada letra se corre según la letra de la clave que le toca. Lo
   * que no es letra minúscula se copia y no consume clave.
   */
  def vigenere(m: Mensaje, clave: Clave): Mensaje = ???
}
