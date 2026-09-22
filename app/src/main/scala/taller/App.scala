package taller

object App {
  def main(args: Array[String]): Unit = {
    val c = new CifradosClasicos()
    println(c.cesar("casa", 3))
  }
}
