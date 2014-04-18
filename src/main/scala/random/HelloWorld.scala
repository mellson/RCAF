package random

import rx.lang.scala.Observable
import scala.concurrent.duration._

object Main extends App {
  Observable.timer(1 seconds, 2 seconds).subscribe(n => println("n = " + n))

  while (true) {

  }
 }