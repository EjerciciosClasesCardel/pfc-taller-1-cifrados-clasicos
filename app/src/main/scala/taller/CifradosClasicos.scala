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
    val abecedario = List (
      ('a', 0 ),
      ('b', 1 ),
      ('c', 2 ),
      ('d', 3 ),
      ('e', 4 ),
      ('f', 5 ),
      ('g', 6 ),
      ('h', 7 ),
      ('i', 8 ),
      ('j', 9 ),
      ('k', 10 ),
      ('l', 11 ),
      ('m', 12 ),
      ('n', 13),
      ('o', 14 ),
      ('p', 15),
      ('q', 16 ),
      ('r', 17 ),
      ('s', 18 ),
      ('t', 19 ),
      ('u', 20 ),
      ('v', 21 ),
      ('w', 22 ),
      ('x', 23 ),
      ('y', 24 ),
      ('z', 25 ),
    )
  def cesar(m: Mensaje, k: Int): Mensaje = {
    if (m == "") {
      ""
    }
    else {
      val letra = m(0)
      letra + cesar(m.tail, k)
    }
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
