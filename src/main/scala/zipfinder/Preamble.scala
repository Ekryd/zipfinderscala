package zipfinder

object Preamble {
  implicit def convertArray[T](arr : Array[T]):List[T] = List.fromArray(arr)
}
