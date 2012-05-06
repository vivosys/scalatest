package org.scalatest.concurrent

import org.scalatest.time.{Millis, Seconds, Span}

trait IntegrationPatience extends AbstractPatienceConfiguration { this: PatienceConfiguration =>

  private val defaultPatienceConfig =
    PatienceConfig(
      timeout = scaled(Span(15, Seconds)),
      interval = scaled(Span(150, Millis))
    )
  implicit val patienceConfig = defaultPatienceConfig
}
