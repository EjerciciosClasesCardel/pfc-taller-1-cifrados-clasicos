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
    def sumar(c: Char, l: Frecuencias): Frecuencias =
      l match {
        case Nil => List((c, 1))
        case (letra, cuenta) :: resto =>
          if (letra == c) (letra, cuenta + 1):: resto
          else (letra, cuenta) ::sumar(c, resto)
      }
    @tailrec
    def contar(i: Int, acc: Frecuencias): Frecuencias =
      if (i >= m.length) acc
      else if (esMinuscula(m(i))) contar(i + 1, sumar(m(i), acc))
      else contar(i + 1, acc)

    def vaAntes(letra1: Char, cuenta1: Int, letra2: Char, cuenta2: Int): Boolean =
      cuenta1 > cuenta2 || (cuenta1 == cuenta2 && letra1 < letra2)

    def insertar(c: Char, n: Int, l: Frecuencias): Frecuencias =
      l match {
        case Nil => List((c, n))
        case (letra, cuenta) :: resto =>
          if (vaAntes(c,n, letra, cuenta)) (c, n) :: l
          else (letra, cuenta) :: insertar(c, n, resto)
      }

    def ordenar(l: Frecuencias):Frecuencias =
      l match {
        case Nil => Nil
        case (letra, cuenta) :: resto => insertar(letra, cuenta, ordenar(resto))
      }
    ordenar(contar(0, Nil))
  }

  // Punto 4 ----------------------------------------------------------

  def desplazamientoProbable(m: Mensaje): Int = {
    def cuentaLetra(c: Char): Int = {
      @tailrec
      def cuenta(i: Int, acc: Int): Int =
        if (i >= m.length) acc
        else if (m(i) == c) cuenta(i + 1, acc + 1)
        else cuenta(i + 1, acc)

      cuenta(0, 0)
    }

    @tailrec
    def buscar(k: Int, kMejor: Int, cantMejor: Int): Int =
      if (k > 25) {
        if (cantMejor == 0) 0
        else (kMejor - 4 + 26) % 26
      }
      else {
        val n = cuentaLetra(('a' + k).toChar)
        if (n > cantMejor) buscar(k + 1, k, n)
        else buscar(k + 1, kMejor, cantMejor)
      }

    buscar(0, 0, 0)
  }

  def romperCesar(m: Mensaje): Mensaje =
    cesar(m, (26 - desplazamientoProbable(m)) % 26)

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

      aux(0, 0, "")
    }
  }

}
