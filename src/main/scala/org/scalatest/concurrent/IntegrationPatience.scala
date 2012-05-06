package org.scalatest.concurrent

import org.scalatest.time.{Millis, Seconds, Span}

trait IntegrationPatience extends AbstractPatienceConfiguration { this: PatienceConfiguration =>

  implicit val patienceConfig =
    PatienceConfig(
      timeout = scaled(Span(15, Seconds)),
      interval = scaled(Span(150, Millis))
    )
}
