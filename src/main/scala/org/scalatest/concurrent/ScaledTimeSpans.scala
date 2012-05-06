package org.scalatest.concurrent

import org.scalatest.time.Span

trait ScaledTimeSpans {

  def scaled(span: Span): Span = span scaledBy spanScaleFactor

  def spanScaleFactor: Double = 1.0
}

