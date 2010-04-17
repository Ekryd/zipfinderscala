package zipfinder.logger

object WaitCharacterTest2 {
  def main(args: Array[String]) {
    testNext
  }

  def testNext {
    println(Wait1.next)
    assert(Wait2 == Wait1.next);

  }
}
