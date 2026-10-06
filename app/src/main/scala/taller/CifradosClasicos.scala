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
  def cesar(m: Mensaje, k: Int): Mensaje =
    if (m.isEmpty) {
      ""
    } else {
      val c = m.head
      if (esMinuscula(c)) {
        val posicion = c - 'a'
        val nuevaposicion = (posicion + k).toInt % 26

        val ajustada = if (nuevaposicion < 0) nuevaposicion + 26 else nuevaposicion
        val nuevaLetra = ('a' + ajustada).toChar
        nuevaLetra.toString + cesar(m.tail, k)
      } else {
        c.toString + cesar(m.tail, k)
      }
    }

  // Punto 2 -------------------------------------------------------------------

  /**
   * El mismo César como proceso iterativo: espacio constante.
   * Cuando la función esté escrita, anótela con @tailrec: el compilador
   * comprueba que la llamada recursiva sea lo último que hace.
   */
  @tailrec
  final def cesarCola(m: Mensaje, k: Int, acc: Mensaje = ""): Mensaje =

    if (m.isEmpty)
      acc
    else {
      val c = m.head
      if (esMinuscula(c)) {
        val posicion = c - 'a'
        val nuevapo = (posicion + k).toInt % 26
        val ajustar = if (nuevapo < 0) nuevapo + 26 else nuevapo
        val nuevaletra = ('a' + ajustar).toChar
        cesarCola(m.tail, k, acc + nuevaletra)
      } else {
        cesarCola(m.tail, k, acc + c)
      }
    }
  // Punto 3 -------------------------------------------------------------------

  /**
   * Cuenta las letras minúsculas del mensaje, de mayor a menor frecuencia y,
   * en empate, en orden alfabético. El recorrido es recursivo de cola.
   */
  def frecuencias(m: Mensaje): Frecuencias = {
    @tailrec
    def contar(i: Int, acc: Frecuencias): Frecuencias =
      if (i >= m.length) {
        acc
      } else {
        val c = m(i)
        if (esMinuscula(c)) {
          val antes = acc.find(par => par._1 == c).map(par => par._2).getOrElse(0)
          val sinC = acc.filter(par => par._1 != c)
          contar(i + 1, (c, antes + 1) :: sinC)
        } else {
          contar(i + 1, acc)
        }
      }

    // Mayor frecuencia primero; en empate, orden alfabético.
    contar(0, Nil).sortBy(par => (-par._2, par._1))
  }



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
  def combinaciones(n: Int, a: Int): BigInt = {
    @tailrec
    def aux(i: Int, acc: BigInt): BigInt =
      if (i > n) acc
      else aux(i + 1, acc * (a - 1))

    if (n <= 0) BigInt(1)
    else if (n == 1) BigInt(a)
    else aux(2, BigInt(a))
  }

  /**
   * Vigenère: cada letra se corre según la letra de la clave que le toca. Lo
   * que no es letra minúscula se copia y no consume clave.
   */

  def vigenere(m: Mensaje, clave: Clave): Mensaje = {
    if (clave.isEmpty) {
      m
    } else {
      @tailrec
      def aux(i: Int, idxClave: Int, acc: Mensaje): Mensaje = {
        if (i >= m.length) {
          acc
        } else {
          val c = m(i)
          if (esMinuscula(c)) {
            val posicion = c - 'a'
            val k = clave(idxClave % clave.length) - 'a'
            val nuevaposicion = (posicion + k) % 26
            val ajustada = if (nuevaposicion < 0) nuevaposicion + 26 else nuevaposicion
            val nuevaLetra = ('a' + ajustada).toChar
            aux(i + 1, idxClave + 1, acc + nuevaLetra)
          } else {
            aux(i + 1, idxClave, acc + c)
          }
        }
      }

      aux(0, 0, "") // <--- ¡Aquí se inicia la recursión!
    }
  }

}
