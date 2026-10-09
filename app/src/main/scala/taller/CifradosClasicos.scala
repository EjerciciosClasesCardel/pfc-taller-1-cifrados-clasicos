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
    def desplazar(c: Char, d: Int): Char = {
      val corr = ((c - primera + d) % letras + letras) % letras
      (primera + corr).toChar
    }

    val desplazamiento = ((k % letras) + letras) % letras

    if (m.isEmpty) ""
    else {
      val c = m.head
      val cifrado =
        if (esMinuscula(c)) desplazar(c, desplazamiento)
        else c
      cifrado + cesar(m.tail, desplazamiento)
    }
  }

  // Punto 2 -------------------------------------------------------------------

  /**
   * El mismo César como proceso iterativo: espacio constante.
   * Aquí la recursión es de cola y se marca con @tailrec.
   */
  @tailrec
  final def cesarCola(m: Mensaje, k: Int, acc: Mensaje = ""): Mensaje = {
    def desplazar(c: Char, d: Int): Char = {
      val corr = ((c - primera + d) % letras + letras) % letras
      (primera + corr).toChar
    }

    val desplazamiento = ((k % letras) + letras) % letras

    if (m.isEmpty) acc
    else {
      val c = m.head
      val cifrado =
        if (esMinuscula(c)) desplazar(c, desplazamiento)
        else c
      cesarCola(m.tail, k, acc + cifrado)
    }
  }

  // Punto 3 -------------------------------------------------------------------

  /**
   * Cuenta las letras minúsculas del mensaje, de mayor a menor frecuencia y,
   * en empate, en orden alfabético. El recorrido es recursivo de cola.
   */
  def frecuencias(m: Mensaje): Frecuencias = {
    @tailrec
    def contarLetra(indice: Int, letra: Char, cantidad: Int): Int =
      if (indice >= m.length) cantidad
      else if (m.charAt(indice) == letra)
        contarLetra(indice + 1, letra, cantidad + 1)
      else contarLetra(indice + 1, letra, cantidad)

    @tailrec
    def insertar(resto: Frecuencias, antes: Frecuencias, par: (Char, Int)): Frecuencias =
      if (resto.isEmpty) antes.reverse ::: List(par)
      else if (par._2 > resto.head._2 ||
        (par._2 == resto.head._2 && par._1 < resto.head._1))
        antes.reverse ::: (par :: resto)
      else insertar(resto.tail, resto.head :: antes, par)

    @tailrec
    def recorrerLetras(numero: Int, resultado: Frecuencias): Frecuencias =
      if (numero >= letras) resultado
      else {
        val letra = (primera + numero).toChar
        val cantidad = contarLetra(0, letra, 0)
        val nuevo =
          if (cantidad == 0) resultado
          else insertar(resultado, List(), (letra, cantidad))
        recorrerLetras(numero + 1, nuevo)
      }

    recorrerLetras(0, List())
  }

  // Punto 4 -------------------------------------------------------------------

  /**
   * Supone que la letra más frecuente del mensaje cifrado es la 'e' del
   * original y devuelve la distancia entre las dos. Sin letras, cero.
   */
  def desplazamientoProbable(m: Mensaje): Int = {
    val fs = frecuencias(m)
    if (fs.isEmpty) 0
    else {
      val letra = fs.head._1
      val corr = letra - 'e'
      if (corr >= 0) corr
      else letras + corr
    }
  }

  def romperCesar(m: Mensaje): Mensaje = {
    cesarCola(m, -desplazamientoProbable(m))
  }

  // Punto 5 -------------------------------------------------------------------

  /**
   * Cuántos mensajes de longitud n se forman con a letras sin dos iguales
   * seguidas.
   */
  def combinaciones(n: Int, a: Int): BigInt = {
    if (n == 0) BigInt(1)
    else if (a == 0) BigInt(0)
    else if (n == 1) BigInt(a)
    else (a - 1) * combinaciones(n - 1, a)
  }

  /**
   * Vigenère: cada letra se corre según la letra de la clave que le toca. Lo
   * que no es letra minúscula se copia y no consume clave.
   */
  def vigenere(m: Mensaje, clave: Clave): Mensaje = {
    def desplazar(c: Char, d: Int): Char = {
      val corr = ((c - primera + d) % letras + letras) % letras
      (primera + corr).toChar
    }

    def cifrar(rest: Mensaje, i: Int, acc: Mensaje): Mensaje = {
      if (rest.isEmpty) acc
      else {
        val c = rest.head
        if (esMinuscula(c)) {
          val d = clave.charAt(i % clave.length) - primera
          val nuevo = desplazar(c, d)
          cifrar(rest.tail, i + 1, acc + nuevo)
        }
        else cifrar(rest.tail, i, acc + c)
      }
    }

    if (clave.isEmpty) m
    else cifrar(m, 0, "")
  }
}
