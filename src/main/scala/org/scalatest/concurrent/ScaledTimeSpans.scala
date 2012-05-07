package org.scalatest.concurrent

import org.scalatest.time.Span
import org.scalatest.tools.Runner

trait ScaledTimeSpans {

  def scaled(span: Span): Span = span scaledBy spanScaleFactor

  def spanScaleFactor: Double = Runner.spanScaleFactor
}

