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

  // Función auxiliar privada para desplazar un carácter de forma segura
  private def desplazar(c: Char, k: Int): Char = {
    if (esMinuscula(c)) {
      val des = (k % letras + letras) % letras
      ((c - 'a' + des) % letras + 'a').toChar
    } else {
      c
    }
  }

  // Punto 1 -------------------------------------------------------------------

  /** César con recursión lineal: una operación pendiente por letra. */
  def cesar(m: Mensaje, k: Int): Mensaje = {
    if (m.isEmpty) ""
    else desplazar(m.head, k).toString + cesar(m.tail, k)
  }

  // Punto 2 -------------------------------------------------------------------

  /**
   * El mismo César como proceso iterativo: espacio constante.
   * Cuando la función esté escrita, anótela con @tailrec: el compilador
   * comprueba que la llamada recursiva sea lo último que hace.
   */
  @tailrec
  final def cesarCola(m: Mensaje, k: Int, acc: Mensaje = ""): Mensaje = {
    if (m.isEmpty) acc
    else cesarCola(m.tail, k, acc + desplazar(m.head, k))
  }

  // Punto 3 -------------------------------------------------------------------

  /**
   * Cuenta las letras minúsculas del mensaje, de mayor a menor frecuencia y,
   * en empate, en orden alfabético. El recorrido es recursivo de cola.
   */
  def frecuencias(m: Mensaje): Frecuencias = {
    // Recorrido recursivo de cola para construir el mapa de frecuencias
    @tailrec
    def contar(restante: String, mapa: Map[Char, Int]): Map[Char, Int] = {
      if (restante.isEmpty) mapa
      else {
        val c = restante.head
        if (esMinuscula(c)) {
          contar(restante.tail, mapa + (c -> (mapa.getOrElse(c, 0) + 1)))
        } else {
          contar(restante.tail, mapa)
        }
      }
    }

    val mapaFrecuencias = contar(m, Map.empty)

    // Ordenamiento usando métodos de la biblioteca:
    // 1. Mayor a menor frecuencia (-count)
    // 2. Orden alfabético en caso de empate (char)
    mapaFrecuencias.toList.sortWith { case ((char1, count1), (char2, count2)) =>
      if (count1 != count2) count1 > count2
      else char1 < char2
    }
  }

  // Punto 4 -------------------------------------------------------------------

  /**
   * Supone que la letra más frecuente del mensaje cifrado es la 'e' del
   * original y devuelve la distancia entre las dos. Sin letras, cero.
   */
  def desplazamientoProbable(m: Mensaje): Int = {
    val frecs = frecuencias(m)
    if (frecs.isEmpty) 0
    else {
      val masFrecuente = frecs.head._1
      val dist = (masFrecuente - 'e') % letras
      (dist + letras) % letras
    }
  }

  def romperCesar(m: Mensaje): Mensaje = {
    val kEstimado = desplazamientoProbable(m)
    cesarCola(m, -kEstimado)
  }

  // Punto 5 -------------------------------------------------------------------

  /**
   * Cuántos mensajes de longitud n se forman con a letras sin dos iguales
   * seguidas.
   */
  def combinaciones(n: Int, a: Int): BigInt = {
    if (n == 0) BigInt(1)
    else if (n == 1) BigInt(a)
    else BigInt(a - 1) * combinaciones(n - 1, a)
  }

  /**
   * Vigenère: cada letra se corre según la letra de la clave que le toca. Lo
   * que no es letra minúscula se copia y no consume clave.
   */
  def vigenere(m: Mensaje, clave: Clave): Mensaje = {
    if (clave.isEmpty) return m

    @tailrec
    def aux(restanteM: String, idxClave: Int, acc: String): String = {
      if (restanteM.isEmpty) acc
      else {
        val c = restanteM.head
        if (esMinuscula(c)) {
          val k = clave(idxClave % clave.length) - 'a'
          val cCifrado = desplazar(c, k)
          aux(restanteM.tail, idxClave + 1, acc + cCifrado)
        } else {
          aux(restanteM.tail, idxClave, acc + c)
        }
      }
    }

    aux(m, 0, "")
  }
}