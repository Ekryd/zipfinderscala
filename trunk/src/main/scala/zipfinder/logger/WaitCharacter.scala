package zipfinder.logger

sealed abstract class WaitCharacter(ch: Char) {
  val printString = ch + "\b"

  def print = System.out.print(printString)

  def next: WaitCharacter
}

case object Wait1 extends WaitCharacter('/') {
  def next = Wait2
}

case object Wait2 extends WaitCharacter('-') {
  def next = Wait3
}

case object Wait3 extends WaitCharacter('\\') {
  def next = Wait4
}

case object Wait4 extends WaitCharacter('|') {
  def next = Wait1
}

