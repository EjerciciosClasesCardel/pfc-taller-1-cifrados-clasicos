package taller

import  org.scalatest.funsuite.AnyFunSuite
import  org.junit.runner.RunWith
import  org.scalatestplus.junit.JUnitRunner

// Casos de Pruebas para el punto 1 y 2, espero que cumpla con lo exigido

@RunWith(classOf[JUnitRunner])
class CifradosClasicosTestExtras extends  AnyFunSuite {

  val c = new CifradosClasicos()
  import c._

  // Punto 1: cesar --------------------------------

  test("cesar: xyz con 3 da abc (completa un giro al alfabeto)") {
    assert(cesar("xyz", 3) == "abc")
  }

  test("cesar: abc con -1 da zab (porque retrocede y da una vuelta al abecedario)") {
    assert(cesar("abc", -1) == "zab")
  }

  test("cesar: un desplazamiento de 26 deja el mensaje igual (abecedario inglés = 26 letras)") {
    assert(cesar("hola", 26) == "hola")
  }

  test("cesar: un desplazamiento negativo grande equivale a su módulo") {
    // -29 equivale a -3, que a su vez equivale a 23
    assert(cesar("hola", -29) == "elix")
    assert(cesar("hola", -29) == cesar("hola", 23))
  }
  test("cesar: prueba de desplazamientos grandes, positivo y negativo") {
    assert(cesar("abc", 1000) == "mno")   // 1000 mod 26 = 12
    assert(cesar("abc", -1000) == "opq")  // -1000 equivale a -12, es decir 14
  }

  test("cesar: ROT13 con texto mixto y su propio inverso") {
    assert(cesar("hello, world 2026!", 13) == "uryyb, jbeyq 2026!")
    assert(cesar(cesar("hello, world 2026!", 13), 13) == "hello, world 2026!") //este es simple, simplemente se llamo 2
    //veces a la misma def cesar y una vez terminado el primer cesar, se completa el siguiente 13 + 13 = 26 == abecedario inglés

  }

  test("cesar: las letras que no son del abecedario inglés se mantienen igual") {
    assert(cesar("ñandú", 1) == "ñboeú")
  }

  test("cesar: dos cifrados seguidos suman sus desplazamientos") {
    val m = "programacion funcional"
    assert(cesar(cesar(m, 5), 9) == cesar(m, 14))
  }

  // Punto 2: cesarCola --------------------------------

  test("cesarCola: xyz con 3 da abc") {
    assert(cesarCola("xyz", 3) == "abc")
  }

  test("cesarCola: abc con -1 da zab") {
    assert(cesarCola("abc", -1) == "zab")
  }

  test("cesarCola: el mensaje vacío sale vacío") {
    assert(cesarCola("", 7) == "")
  }

  test("cesarCola: mayúsculas, espacios, comas y dígitos son ignorados y terminan igual") {
    assert(cesarCola("Hola, Mundo 2026", 5) == "Htqf, Mzsit 2026")
  }

  test("cesarCola: el acumulador inicial se conserva al principio del resultado") {
    assert(cesarCola("bc", 1, "xy") == "xycd")
  }

  test("cesarCola: descifrar con -k recupera el mensaje original, basicamente cifrado y descifrado") {
    val m = "cifrado de cola, version 2"
    assert(cesarCola(cesarCola(m, 17), -17) == m)
  }

  test("cesarCola: coincide con cesar para muchos desplazamientos") {
    val m = "el rapido zorro marron salta, 3 veces!"
    assert((-60 to 60).forall(k => cesarCola(m, k) == cesar(m, k)))
  }

  test("cesarCola: un mensaje largo de una sola letra") {
    val largo = "a" * 100000
    assert(cesarCola(largo, 1) == "b" * 100000)
  }

  // Punto 3: Frecuencias ---------------------
}