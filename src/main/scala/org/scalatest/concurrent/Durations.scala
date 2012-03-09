/*
 * Copyright 2001-2012 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatest.concurrent

import org.scalatest.concurrent.Durations.Duration

trait Durations {

  sealed abstract class TimeUnits

  case object Nanosecond extends TimeUnits
  case object Nanoseconds extends TimeUnits
  case object Microsecond extends TimeUnits
  case object Microseconds extends TimeUnits
  case object Millisecond extends TimeUnits
  case object Milliseconds extends TimeUnits
  case object Millis extends TimeUnits
  case object Second extends TimeUnits
  case object Seconds extends TimeUnits
  case object Minute extends TimeUnits
  case object Minutes extends TimeUnits
  case object Hour extends TimeUnits
  case object Hours extends TimeUnits
  case object Day extends TimeUnits
  case object Days extends TimeUnits

  case class Duration private (m: Long, n: Int = 0) extends DurationConcept(m, n)

  // Can be used when implicitly converting infinity to a DurationConcept
  def maxDuration = Duration(Long.MaxValue, Millis)

  object Duration {
    private final val NanosDivisor = 1000000
    private final val MicrosDivisor = 1000

    private def singularErrorMsg(unitsString: String) = {
      "Singular form of " + unitsString +
        " (i.e., without the trailing s) can only be used with the value 1. Use " +
        unitsString + "s (i.e., with an s) instead."
    }

    // TODO: write test for: Can't pass anything but zero for nanos if Long.MaxInt is passed for millis.
    def apply(length: Long, units: TimeUnits): Duration = {

      val MaxSeconds = Long.MaxValue / 1000
      val MaxMinutes = Long.MaxValue / 1000 / 60
      val MaxHours = Long.MaxValue / 1000 / 60 / 60
      val MaxDays = Long.MaxValue / 1000 / 60 / 60 / 24

      require(length >= 0, "length must be greater than or equal to zero, but was: " + length)

      require(units != Nanosecond || length == 1, singularErrorMsg("Nanosecond"))
      require(units != Microsecond || length == 1, singularErrorMsg("Microsecond"))
      require(units != Millisecond || length == 1, singularErrorMsg("Millisecond"))
      require(units != Second || length == 1, singularErrorMsg("Second"))
      require(units != Minute || length == 1, singularErrorMsg("Minute"))
      require(units != Hour || length == 1, singularErrorMsg("Hour"))
      require(units != Day || length == 1, singularErrorMsg("Day"))

      require(units != Seconds || length <= MaxSeconds, "Passed length, " + length + ", is larger than the largest expressible number of seconds: Long.MaxValue / 1000")
      require(units != Minutes || length <= MaxMinutes, "Passed length, " + length + ", is larger than the largest expressible number of minutes: Long.MaxValue / 1000 / 60")
      require(units != Hours || length <= MaxHours, "Passed length, " + length + ", is larger than the largest expressible number of hours: Long.MaxValue / 1000 / 60 / 60")
      require(units != Days || length <= MaxDays, "Passed length, " + length + ", is larger than the largest expressible number of days: Long.MaxValue / 1000 / 60 / 60 / 24")

      units match {
        case Nanosecond | Nanoseconds => // Document: Truncates. Doesn't attempt to round up.
          new Duration(length / NanosDivisor, (length % NanosDivisor).toInt)
        case Microsecond | Microseconds =>
          new Duration(length / MicrosDivisor, (length % MicrosDivisor).toInt * 1000)
        case Millisecond | Milliseconds | Millis =>
          new Duration(length, 0)
        case Second | Seconds =>
          new Duration(length * 1000, 0)
        case Minute | Minutes =>
          new Duration(length * 1000 * 60, 0)
        case Hour | Hours =>
          new Duration(length * 1000 * 60 * 60, 0)
        case Day | Days =>
          new Duration(length * 1000 * 60 * 60 * 24, 0)
      }
    }

    def apply(length: Double, units: TimeUnits): Duration = {

      val MaxNanoseconds = (Long.MaxValue).toDouble

      val MaxSeconds = (Long.MaxValue / 1000).toDouble
      val MaxMinutes = (Long.MaxValue / 1000 / 60).toDouble
      val MaxHours = (Long.MaxValue / 1000 / 60 / 60).toDouble
      val MaxDays = (Long.MaxValue / 1000 / 60 / 60 / 24).toDouble

      require(length >= 0, "length must be greater than or equal to zero, but was: " + length)

      require(units != Nanosecond || length == 1.0, singularErrorMsg("Nanosecond"))
      require(units != Microsecond || length == 1.0, singularErrorMsg("Microsecond"))
      require(units != Millisecond || length == 1.0, singularErrorMsg("Millisecond"))
      require(units != Second || length == 1.0, singularErrorMsg("Second"))
      require(units != Minute || length == 1.0, singularErrorMsg("Minute"))
      require(units != Hour || length == 1.0, singularErrorMsg("Hour"))
      require(units != Day || length == 1.0, singularErrorMsg("Day"))

      require(units != Nanoseconds || length <= MaxNanoseconds, "Passed length, " + length + ", is larger than the largest expressible number of nanoseconds: Long.MaxValue")
      // TODO: Am I missing some here? Think so.
      require(units != Seconds || length <= MaxSeconds, "Passed length, " + length + ", is larger than the largest expressible number of seconds: Long.MaxValue / 1000")
      require(units != Minutes || length <= MaxMinutes, "Passed length, " + length + ", is larger than the largest expressible number of minutes: Long.MaxValue / 1000 / 60")
      require(units != Hours || length <= MaxHours, "Passed length, " + length + ", is larger than the largest expressible number of hours: Long.MaxValue / 1000 / 60 / 60")
      require(units != Days || length <= MaxDays, "Passed length, " + length + ", is larger than the largest expressible number of days: Long.MaxValue / 1000 / 60 / 60 / 24")

      units match {
        case Nanosecond | Nanoseconds =>
          new Duration((length / NanosDivisor).toLong, (length % NanosDivisor).toInt)
        case Microsecond | Microseconds =>
          new Duration((length / MicrosDivisor).toLong, ((length % MicrosDivisor) * 1000).toInt)
        case Millisecond | Milliseconds | Millis =>
          new Duration(length.toLong, ((length % 1) * 1000000).toInt)
        case Second | Seconds =>
          new Duration((length * 1000).toLong, (((length * 1000) % 1) * 1000000).toInt)
        case Minute | Minutes =>
          new Duration((length * 1000 * 60).toLong, (((length * 1000 * 60) % 1) * 1000000).toInt)
        case Hour | Hours =>
          new Duration((length * 1000 * 60 * 60).toLong, (((length * 1000 * 60 * 60) % 1) * 1000000).toInt)
        case Day | Days =>
          new Duration((length * 1000 * 60 * 60 * 24).toLong, (((length * 1000 * 60 * 60 * 24) % 1) * 1000000).toInt)
        case _ => new Duration(0)
      }
    }
  }        // TODO: elim duplica
}

object Durations extends Durations