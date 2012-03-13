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
package org.scalatest.time

import org.scalatest.{SeveredStackTraces, FunSpec}
import org.scalatest.matchers.ShouldMatchers

class SpanSugarSpec extends FunSpec with SpanMatchers with ShouldMatchers with SeveredStackTraces {
  
  import SpanSugar._
 
  describe("The SpanSugar trait") {
    
    it("should provide implicit conversions for Int time spans") {
      assert((1 nanosecond) === Span(1, Nanosecond))
      assert((2 nanoseconds) === Span(2, Nanoseconds))
      assert((1 microsecond) === Span(1, Microsecond))
      assert((2 microseconds) === Span(2, Microseconds))
      assert((1 millisecond) === Span(1, Millisecond))
      assert((2 milliseconds) === Span(2, Milliseconds))
      assert((2 millis) === Span(2, Millis))
      assert((2 seconds) === Span(2, Seconds))
      assert((1 second) === Span(1, Second))
      assert((2 seconds) === Span(2, Seconds))
      assert((1 minute) === Span(1, Minute))
      assert((2 minutes) === Span(2, Minutes))
      assert((1 hour) === Span(1, Hour))
      assert((2 hours) === Span(2, Hours))
      assert((1 day) === Span(1, Day))
      assert((2 days) === Span(2, Days))
    }
    
    it("should provide implicit conversions for Long time spans") {
      assert((1L nanosecond) === Span(1, Nanosecond))
      assert((2L nanoseconds) === Span(2, Nanoseconds))
      assert((1L microsecond) === Span(1, Microsecond))
      assert((2L microseconds) === Span(2, Microseconds))
      assert((1L millisecond) === Span(1, Millisecond))
      assert((2L milliseconds) === Span(2, Milliseconds))
      assert((2L millis) === Span(2, Millis))
      assert((2L seconds) === Span(2, Seconds))
      assert((1L second) === Span(1, Second))
      assert((2L seconds) === Span(2, Seconds))
      assert((1L minute) === Span(1, Minute))
      assert((2L minutes) === Span(2, Minutes))
      assert((1L hour) === Span(1, Hour))
      assert((2L hours) === Span(2, Hours))
      assert((1L day) === Span(1, Day))
      assert((2L days) === Span(2, Days))
    }

    it("should provide an implicit conversion from GrainOfTime to Long") {
      def getALong(aSpan: Span) = aSpan.totalNanos
      assert(getALong(1 nanosecond) === 1L)
      assert(getALong(2 nanoseconds) === 2L)
      assert(getALong(1 microsecond) === 1L * 1000)
      assert(getALong(2 microseconds) === 2L * 1000)
      assert(getALong(1 millisecond) === 1L * 1000 * 1000)
      assert(getALong(2 milliseconds) === 2L * 1000 * 1000)
      assert(getALong(2 millis) === 2L * 1000 * 1000)
      assert(getALong(2 seconds) === 2L * 1000 * 1000 * 1000)
      assert(getALong(1 second) === 1000L * 1000 * 1000)
      assert(getALong(2 seconds) === 2L * 1000 * 1000 * 1000)
      assert(getALong(1 minute) === 1000L * 60 * 1000 * 1000)
      assert(getALong(2 minutes) === 2L * 1000 * 60 * 1000 * 1000)
      assert(getALong(1 hour) === 1000L * 60 * 60 * 1000 * 1000)
      assert(getALong(2 hours) === 2L * 1000 * 60 * 60 * 1000 * 1000)
      assert(getALong(1 day) === 1000L * 60 * 60 * 24 * 1000 * 1000)
      assert(getALong(2 days) === 2L * 1000 * 60 * 60 * 24 * 1000 * 1000)
      assert(getALong(1L millisecond) === 1L * 1000 * 1000)
      assert(getALong(2L milliseconds) === 2L * 1000 * 1000)
      assert(getALong(2L millis) === 2L * 1000 * 1000)
      assert(getALong(2L seconds) === 2L * 1000 * 1000 * 1000)
      assert(getALong(1L second) === 1000L * 1000 * 1000)
      assert(getALong(2L seconds) === 2L * 1000 * 1000 * 1000)
      assert(getALong(1L minute) === 1000L * 60 * 1000 * 1000)
      assert(getALong(2L minutes) === 2L * 1000 * 60 * 1000 * 1000)
      assert(getALong(1L hour) === 1000L * 60 * 60 * 1000 * 1000)
      assert(getALong(2L hours) === 2L * 1000 * 60 * 60 * 1000 * 1000)
      assert(getALong(1L day) === 1000L * 60 * 60 * 24 * 1000 * 1000)
      assert(getALong(2L days) === 2L * 1000 * 60 * 60 * 24 * 1000 * 1000)
    }
  }
  describe("A Span creating via SpanSugar") {

    it("should produce IAE if a negative length is passed") {
      for (f <- Seq((i: Long) => i nanosecond, (i: Long) => i nanoseconds, (i: Long) => i microsecond, (i: Long) => i microseconds,
        (i: Long) => i millisecond, (i: Long) => i milliseconds, (i: Long) => i millis, (i: Long) => i second, (i: Long) => i seconds,
        (i: Long) => i minute, (i: Long) => i minutes, (i: Long) => i hour, (i: Long) => i hours, (i: Long) => i day, (i: Long) => i days)) {
        for (i <- Seq(-1, -2, -3, Long.MinValue)) {
          withClue("i was: " + i) {
            intercept[IllegalArgumentException] {
              f(i)
            }
          }
        }
      }
      for (f <- Seq((d: Double) => d nanosecond, (d: Double) => d nanoseconds, (d: Double) => d microsecond, (d: Double) => d microseconds,
        (d: Double) => d millisecond, (d: Double) => d milliseconds, (d: Double) => d millis, (d: Double) => d second, (d: Double) => d seconds,
        (d: Double) => d minute, (d: Double) => d minutes, (d: Double) => d hour, (d: Double) => d hours, (d: Double) => d day, (d: Double) => d days)) {
        for (d <- Seq(-1, -2, -3, -1.5, -9.98, Double.MinValue)) {
          withClue("d was: " + d) {
            intercept[IllegalArgumentException] {
              f(d)
            }
          }
        }
      }
    }

    it("should produce IAE if anything other than 1 is passed for singular units forms") {
      for (u <- Seq(Nanosecond, Microsecond, Millisecond, Second, Minute, Hour, Day)) {
        for (i <- Seq(0, 2, 3, Long.MaxValue)) {
          withClue("u was: " + u + "; i was: " + i) {
            intercept[IllegalArgumentException] {
              Span(i, u)
            }
          }
        }
        for (d <- Seq(0.0, 0.1, 1.1, 2.0, 9.98, Double.MaxValue)) {
          withClue("u was: " + u + "; d was: " + d) {
            intercept[IllegalArgumentException] {
              Span(d, u)
            }
          }
        }
      }
    }

    it("should construct with valid nanoseconds passed") {

      Span(0, Nanoseconds) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1, Nanosecond) should have (totalNanos(1), millisPart(0), nanosPart(1))
      Span(1, Nanoseconds) should have (totalNanos(1), millisPart(0), nanosPart(1))
      Span(2, Nanoseconds) should have (totalNanos(2), millisPart(0), nanosPart(2))
      Span(Long.MaxValue, Nanoseconds) should have (
        totalNanos(1000L * 1000 * 9223372036854L + 775807),
        millisPart(9223372036854L),
        nanosPart(775807)
      )

      Span(0.0, Nanoseconds) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1.0, Nanosecond) should have (totalNanos(1), millisPart(0), nanosPart(1))
      Span(1.0, Nanoseconds) should have (totalNanos(1), millisPart(0), nanosPart(1))
      Span(2.0, Nanoseconds) should have (totalNanos(2), millisPart(0), nanosPart(2))
      Span(0.1, Nanoseconds) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1.1, Nanoseconds) should have (totalNanos(1), millisPart(0), nanosPart(1))
      Span(1.2, Nanoseconds) should have (totalNanos(1), millisPart(0), nanosPart(1))
      Span(1.499, Nanoseconds) should have (totalNanos(1), millisPart(0), nanosPart(1))
      Span(1.5, Nanoseconds) should have (totalNanos(1), millisPart(0), nanosPart(1))
      Span(1.9, Nanoseconds) should have (totalNanos(1), millisPart(0), nanosPart(1))
      Span(2.2, Nanoseconds) should have (totalNanos(2), millisPart(0), nanosPart(2))
      Span(Long.MaxValue.toDouble, Nanoseconds) should have (
        totalNanos(1000L * 1000 * 9223372036854L + 775807),
        millisPart(9223372036854L),
        nanosPart(775807)
      )
    }

    it("should throw IAE if a Double nanos value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue.toDouble
      for (d <- Seq(biggest + 10000, biggest + 20000, biggest + 30000, Double.MaxValue)) {
        withClue("d was: " + d) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(d, Nanoseconds)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should construct with valid microseconds passed") {

      Span(0, Microseconds) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1, Microsecond) should have (totalNanos(1000), millisPart(0), nanosPart(1000))
      Span(1, Microseconds) should have (totalNanos(1000), millisPart(0), nanosPart(1000))
      Span(2, Microseconds) should have (totalNanos(2000), millisPart(0), nanosPart(2000))
      Span(1000, Microseconds) should have (totalNanos(1000 * 1000), millisPart(1), nanosPart(0))
      Span(1001, Microseconds) should have (totalNanos(1001L * 1000), millisPart(1), nanosPart(1000))
      Span(1002, Microseconds) should have (totalNanos(1002L * 1000), millisPart(1), nanosPart(2000))
      Span(2000, Microseconds) should have (totalNanos(2000 * 1000), millisPart(2), nanosPart(0))
      Span(2001, Microseconds) should have (totalNanos(2001 * 1000), millisPart(2), nanosPart(1000))
      Span(2002, Microseconds) should have (totalNanos(2002 * 1000), millisPart(2), nanosPart(2000))
      Span(Long.MaxValue / 1000, Microseconds) should have (
        totalNanos(1000L * 1000 * 9223372036854L + 775000),
        millisPart(9223372036854L),
        nanosPart(775000)
      )

      Span(0.0, Microseconds) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1.0, Microsecond) should have (totalNanos(1000), millisPart(0), nanosPart(1000))
      Span(1.0, Microseconds) should have (totalNanos(1000), millisPart(0), nanosPart(1000))
      Span(2.0, Microseconds) should have (totalNanos(2000), millisPart(0), nanosPart(2000))
      Span(1000.0, Microseconds) should have (totalNanos(1000L * 1000), millisPart(1), nanosPart(0))
      Span(1001.0, Microseconds) should have (totalNanos(1001L * 1000), millisPart(1), nanosPart(1000))
      Span(1002.0, Microseconds) should have (totalNanos(1002L * 1000), millisPart(1), nanosPart(2000))
      Span(2000.0, Microseconds) should have (totalNanos(2000 * 1000), millisPart(2), nanosPart(0))
      Span(2001.0, Microseconds) should have (totalNanos(2001 * 1000), millisPart(2), nanosPart(1000))
      Span(2002.0, Microseconds) should have (totalNanos(2002 * 1000), millisPart(2), nanosPart(2000))
      Span(0.1, Microseconds) should have (totalNanos(100), millisPart(0), nanosPart(100))
      Span(1.1, Microseconds) should have (totalNanos(1100), millisPart(0), nanosPart(1100))
      Span(1.2, Microseconds) should have (totalNanos(1200), millisPart(0), nanosPart(1200))
      Span(1.499, Microseconds) should have (totalNanos(1499), millisPart(0), nanosPart(1499))
      Span(1.5, Microseconds) should have (totalNanos(1500), millisPart(0), nanosPart(1500))
      Span(1.9, Microseconds) should have (totalNanos(1900), millisPart(0), nanosPart(1900))
      Span(2.2, Microseconds) should have (totalNanos(2200), millisPart(0), nanosPart(2200))
      Span((Long.MaxValue / 1000).toDouble, Microseconds) should have (
        totalNanos(1000L * 1000 * 9223372036854L + 775807),
        millisPart(9223372036854L),
        nanosPart(775807)
      )
    }

    it("should throw IAE if a microseconds value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue / 1000
      for (i <- Seq(biggest + 1, biggest + 2, biggest + 3, Long.MaxValue)) {
        withClue("i was: " + i) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(i, Microseconds)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should throw IAE if a Double microseconds value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue.toDouble / 1000
      for (d <- Seq(biggest + 10, biggest + 20, biggest + 30, Double.MaxValue)) {
        withClue("d was: " + d) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(d, Microseconds)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should construct with valid milliseconds passed") {

      Span(0, Milliseconds) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1, Millisecond) should have (totalNanos(1000L * 1000), millisPart(1), nanosPart(0))
      Span(1, Milliseconds) should have (totalNanos(1000L * 1000), millisPart(1), nanosPart(0))
      Span(2, Milliseconds) should have (totalNanos(2 * 1000 * 1000), millisPart(2), nanosPart(0))
      Span(1000, Milliseconds) should have (totalNanos(1000L * 1000 * 1000), millisPart(1000), nanosPart(0))
      Span(1001, Milliseconds) should have (totalNanos(1001 * 1000 * 1000), millisPart(1001), nanosPart(0))
      Span(1002, Milliseconds) should have (totalNanos(1002L * 1000 * 1000), millisPart(1002), nanosPart(0))
      Span(2000, Milliseconds) should have (totalNanos(2000L * 1000 * 1000), millisPart(2000), nanosPart(0))
      Span(2001, Milliseconds) should have (totalNanos(2001L * 1000 * 1000), millisPart(2001), nanosPart(0))
      Span(2002, Milliseconds) should have (totalNanos(2002L * 1000 * 1000), millisPart(2002), nanosPart(0))
      Span(Long.MaxValue / 1000 / 1000, Milliseconds) should have (
        totalNanos(1000L * 1000 * 9223372036854L),
        millisPart(9223372036854L),
        nanosPart(0)
      )

      Span(0.0, Milliseconds) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1.0, Millisecond) should have (totalNanos(1000L * 1000), millisPart(1), nanosPart(0))
      Span(1.0, Milliseconds) should have (totalNanos(1000L * 1000), millisPart(1), nanosPart(0))
      Span(2.0, Milliseconds) should have (totalNanos(2 * 1000 * 1000), millisPart(2), nanosPart(0))
      Span(1000.0, Milliseconds) should have (totalNanos(1000L * 1000 * 1000), millisPart(1000), nanosPart(0))
      Span(1001.0, Milliseconds) should have (totalNanos(1001 * 1000 * 1000), millisPart(1001), nanosPart(0))
      Span(1002.0, Milliseconds) should have (totalNanos(1002L * 1000 * 1000), millisPart(1002), nanosPart(0))
      Span(2000.0, Milliseconds) should have (totalNanos(2000L * 1000 * 1000), millisPart(2000), nanosPart(0))
      Span(2001.0, Milliseconds) should have (totalNanos(2001L * 1000 * 1000), millisPart(2001), nanosPart(0))
      Span(2002.0, Milliseconds) should have (totalNanos(2002L * 1000 * 1000), millisPart(2002), nanosPart(0))
      Span(0.1, Milliseconds) should have (totalNanos(100L * 1000), millisPart(0), nanosPart(100000))
      Span(1.1, Milliseconds) should have (totalNanos(1100L * 1000), millisPart(1), nanosPart(100000))
      Span(1.2, Milliseconds) should have (totalNanos(1200L * 1000), millisPart(1), nanosPart(200000))
      Span(1.499, Milliseconds) should have (totalNanos(1499L * 1000), millisPart(1), nanosPart(499000))
      Span(1.5, Milliseconds) should have (totalNanos(1500L * 1000), millisPart(1), nanosPart(500000))
      Span(1.9, Milliseconds) should have (totalNanos(1900L * 1000), millisPart(1), nanosPart(900000))
      Span(2.2, Milliseconds) should have (totalNanos(2200 * 1000), millisPart(2), nanosPart(200000))
      Span(Long.MaxValue.toDouble / 1000 / 1000, Milliseconds) should have (
        totalNanos(1000L * 1000 * 9223372036854L + 775807),
        millisPart(9223372036854L),
        nanosPart(775807)
      )
    }

    it("should throw IAE if a milliseconds value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue / 1000 / 1000
      for (i <- Seq(biggest + 1, biggest + 2, biggest + 3, Long.MaxValue)) {
        withClue("i was: " + i) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(i, Milliseconds)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should throw IAE if a Double milliseconds value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue.toDouble / 1000 / 1000
      for (d <- Seq(biggest + 1, biggest + 2, biggest + 3, Double.MaxValue)) {
        withClue("d was: " + d) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(d, Milliseconds)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should construct with valid milliseconds passed when used with the shorthand, Millis") {

      Span(0, Millis) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1, Millis) should have (totalNanos(1000L * 1000), millisPart(1), nanosPart(0))
      Span(1, Millis) should have (totalNanos(1000L * 1000), millisPart(1), nanosPart(0))
      Span(2, Millis) should have (totalNanos(2 * 1000 * 1000), millisPart(2), nanosPart(0))
      Span(1000, Millis) should have (totalNanos(1000L * 1000 * 1000), millisPart(1000), nanosPart(0))
      Span(1001, Millis) should have (totalNanos(1001 * 1000 * 1000), millisPart(1001), nanosPart(0))
      Span(1002, Millis) should have (totalNanos(1002L * 1000 * 1000), millisPart(1002), nanosPart(0))
      Span(2000, Millis) should have (totalNanos(2000L * 1000 * 1000), millisPart(2000), nanosPart(0))
      Span(2001, Millis) should have (totalNanos(2001L * 1000 * 1000), millisPart(2001), nanosPart(0))
      Span(2002, Millis) should have (totalNanos(2002L * 1000 * 1000), millisPart(2002), nanosPart(0))
      Span(Long.MaxValue / 1000 / 1000, Millis) should have (
        totalNanos(1000L * 1000 * 9223372036854L),
        millisPart(9223372036854L),
        nanosPart(0)
      )

      Span(0.0, Millis) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1.0, Millisecond) should have (totalNanos(1000L * 1000), millisPart(1), nanosPart(0))
      Span(1.0, Millis) should have (totalNanos(1000L * 1000), millisPart(1), nanosPart(0))
      Span(2.0, Millis) should have (totalNanos(2 * 1000 * 1000), millisPart(2), nanosPart(0))
      Span(1000.0, Millis) should have (totalNanos(1000L * 1000 * 1000), millisPart(1000), nanosPart(0))
      Span(1001.0, Millis) should have (totalNanos(1001 * 1000 * 1000), millisPart(1001), nanosPart(0))
      Span(1002.0, Millis) should have (totalNanos(1002L * 1000 * 1000), millisPart(1002), nanosPart(0))
      Span(2000.0, Millis) should have (totalNanos(2000L * 1000 * 1000), millisPart(2000), nanosPart(0))
      Span(2001.0, Millis) should have (totalNanos(2001L * 1000 * 1000), millisPart(2001), nanosPart(0))
      Span(2002.0, Millis) should have (totalNanos(2002L * 1000 * 1000), millisPart(2002), nanosPart(0))
      Span(0.1, Millis) should have (totalNanos(100L * 1000), millisPart(0), nanosPart(100000))
      Span(1.1, Millis) should have (totalNanos(1100L * 1000), millisPart(1), nanosPart(100000))
      Span(1.2, Millis) should have (totalNanos(1200L * 1000), millisPart(1), nanosPart(200000))
      Span(1.499, Millis) should have (totalNanos(1499L * 1000), millisPart(1), nanosPart(499000))
      Span(1.5, Millis) should have (totalNanos(1500L * 1000), millisPart(1), nanosPart(500000))
      Span(1.9, Millis) should have (totalNanos(1900L * 1000), millisPart(1), nanosPart(900000))
      Span(2.2, Millis) should have (totalNanos(2200 * 1000), millisPart(2), nanosPart(200000))
      Span(Long.MaxValue.toDouble / 1000 / 1000, Millis) should have (
        totalNanos(1000L * 1000 * 9223372036854L + 775807),
        millisPart(9223372036854L),
        nanosPart(775807)
      )
    }

    it("should throw IAE if a milliseconds value larger than the largest expressible amount is passed when used with the shorthand, Millis.") {
      val biggest = Long.MaxValue / 1000 / 1000
      for (i <- Seq(biggest + 1, biggest + 2, biggest + 3, Long.MaxValue)) {
        withClue("i was: " + i) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(i, Millis)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should throw IAE if a Double milliseconds value larger than the largest expressible amount is passed when used with the shorthand, Millis.") {
      val biggest = Long.MaxValue.toDouble / 1000 / 1000
      for (d <- Seq(biggest + 1, biggest + 2, biggest + 3, Double.MaxValue)) {
        withClue("d was: " + d) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(d, Millis)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should construct with valid seconds passed") {

      Span(0, Seconds) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1, Second) should have (totalNanos(1000L * 1000 * 1000), millisPart(1000), nanosPart(0))
      Span(1, Seconds) should have (totalNanos(1000L * 1000 * 1000), millisPart(1000), nanosPart(0))
      Span(2, Seconds) should have (totalNanos(2000L * 1000 * 1000), millisPart(2000), nanosPart(0))
      Span(1000, Seconds) should have (totalNanos(1000L * 1000 * 1000000), millisPart(1000 * 1000), nanosPart(0))
      Span(1001, Seconds) should have (totalNanos(1000L * 1000 * 1001000), millisPart(1001 * 1000), nanosPart(0))
      Span(1002, Seconds) should have (totalNanos(1000L * 1000 * 1002000), millisPart(1002 * 1000), nanosPart(0))
      Span(2000, Seconds) should have (totalNanos(1000L * 1000 * 2000000), millisPart(2000 * 1000), nanosPart(0))
      Span(2001, Seconds) should have (totalNanos(1000L * 1000 * 2001000), millisPart(2001 * 1000), nanosPart(0))
      Span(2002, Seconds) should have (totalNanos(1000L * 1000 * 2002000), millisPart(2002 * 1000), nanosPart(0))
      Span(Long.MaxValue / 1000 / 1000 / 1000, Seconds) should have (
        totalNanos(1000L * 1000 * 9223372036000L),
        millisPart(9223372036000L),
        nanosPart(0)
      )

      Span(0.0, Seconds) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1.0, Second) should have (totalNanos(1000L * 1000 * 1000), millisPart(1000), nanosPart(0))
      Span(1.0, Seconds) should have (totalNanos(1000L * 1000 * 1000), millisPart(1000), nanosPart(0))
      Span(2.0, Seconds) should have (totalNanos(2000L * 1000 * 1000), millisPart(2000), nanosPart(0))
      Span(1000.0, Seconds) should have (totalNanos(1000L * 1000 * 1000000), millisPart(1000 * 1000), nanosPart(0))
      Span(1001.0, Seconds) should have (totalNanos(1000L * 1000 * 1001000), millisPart(1001 * 1000), nanosPart(0))
      Span(1002.0, Seconds) should have (totalNanos(1000L * 1000 * 1002000), millisPart(1002 * 1000), nanosPart(0))
      Span(2000.0, Seconds) should have (totalNanos(1000L * 1000 * 2000000), millisPart(2000 * 1000), nanosPart(0))
      Span(2001.0, Seconds) should have (totalNanos(1000L * 1000 * 2001000), millisPart(2001 * 1000), nanosPart(0))
      Span(2002.0, Seconds) should have (totalNanos(1000L * 1000 * 2002000), millisPart(2002 * 1000), nanosPart(0))
      Span(0.1, Seconds) should have (totalNanos(1000L * 1000 * 100), millisPart(100), nanosPart(0))
      Span(1.1, Seconds) should have (totalNanos(1000L * 1000 * 1100), millisPart(1100), nanosPart(0))
      Span(1.2, Seconds) should have (totalNanos(1000L * 1000 * 1200), millisPart(1200), nanosPart(0))
      Span(1.499, Seconds) should have (totalNanos(1000L * 1000 * 1499), millisPart(1499), nanosPart(0))
      Span(1.5, Seconds) should have (totalNanos(1000L * 1000 * 1500), millisPart(1500), nanosPart(0))
      Span(1.9, Seconds) should have (totalNanos(1000L * 1000 * 1900), millisPart(1900), nanosPart(0))
      Span(2.2, Seconds) should have (totalNanos(1000L * 1000 * 2200), millisPart(2200), nanosPart(0))
      Span(0.001, Seconds) should have (totalNanos(1000L * 1000), millisPart(1), nanosPart(0))
      Span(88.0001, Seconds) should have (totalNanos(1000L * 1000 * 88000 + 100000), millisPart(88 * 1000), nanosPart(100000))
      Span(88.000001, Seconds) should have (totalNanos(1000L * 1000 * 88000 + 1000), millisPart(88 * 1000), nanosPart(1000))
      Span(88.000000001, Seconds) should have (totalNanos(1000L * 1000 * 88000 + 1), millisPart(88 * 1000), nanosPart(1))
      Span(Long.MaxValue.toDouble / 1000 / 1000 / 1000, Seconds) should have (
        totalNanos(1000L * 1000 * 9223372036854L + 775807),
        millisPart(9223372036854L),
        nanosPart(775807)
      )
    }

    it("should throw IAE if a seconds value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue / 1000 / 1000 / 1000
      for (i <- Seq(biggest + 1, biggest + 2, biggest + 3, Long.MaxValue)) {
        withClue("i was: " + i) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(i, Seconds)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should throw IAE if a Double seconds value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue.toDouble / 1000 / 1000 / 1000
      for (d <- Seq(biggest + 1, biggest + 2, biggest + 3, Double.MaxValue)) {
        withClue("d was: " + d) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(d, Seconds)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should construct with valid minutes passed") {

      Span(0, Minutes) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1, Minute) should have (totalNanos(1000L * 1000 * 1000 * 60), millisPart(60 * 1000), nanosPart(0))
      Span(1, Minutes) should have (totalNanos(1000L * 1000 * 1000 * 60), millisPart(60 * 1000), nanosPart(0))
      Span(2, Minutes) should have (totalNanos(1000L * 1000 * 2 * 1000 * 60), millisPart(2 * 60 * 1000), nanosPart(0))
      Span(1000, Minutes) should have (totalNanos(1000L * 1000 * 1000 * 1000 * 60), millisPart(1000 * 60 * 1000), nanosPart(0))
      Span(1001, Minutes) should have (totalNanos(1000L * 1000 * 1001 * 1000 * 60), millisPart(1001 * 60 * 1000), nanosPart(0))
      Span(1002, Minutes) should have (totalNanos(1000L * 1000 * 1002 * 1000 * 60), millisPart(1002 * 60 * 1000), nanosPart(0))
      Span(2000, Minutes) should have (totalNanos(1000L * 1000 * 2000 * 1000 * 60), millisPart(2000 * 60 * 1000), nanosPart(0))
      Span(2001, Minutes) should have (totalNanos(1000L * 1000 * 2001 * 1000 * 60), millisPart(2001 * 60 * 1000), nanosPart(0))
      Span(2002, Minutes) should have (totalNanos(1000L * 1000 * 2002 * 1000 * 60), millisPart(2002 * 60 * 1000), nanosPart(0))
      Span(Long.MaxValue / 1000 / 1000 / 1000 / 60, Minutes) should have (
        totalNanos(1000L * 1000 * 9223372020000L),
        millisPart(9223372020000L),
        nanosPart(0)
      )

      Span(0.0, Minutes) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1.0, Minute) should have (totalNanos(1000L * 1000 * 1000 * 60), millisPart(60 * 1000), nanosPart(0))
      Span(1.0, Minutes) should have (totalNanos(1000L * 1000 * 1000 * 60), millisPart(60 * 1000), nanosPart(0))
      Span(2.0, Minutes) should have (totalNanos(1000L * 1000 * 2 * 1000 * 60), millisPart(2 * 60 * 1000), nanosPart(0))
      Span(1000.0, Minutes) should have (totalNanos(1000L * 1000 * 1000 * 1000 * 60), millisPart(1000 * 60 * 1000), nanosPart(0))
      Span(1001.0, Minutes) should have (totalNanos(1000L * 1000 * 1001 * 1000 * 60), millisPart(1001 * 60 * 1000), nanosPart(0))
      Span(1002.0, Minutes) should have (totalNanos(1000L * 1000 * 1002 * 1000 * 60), millisPart(1002 * 60 * 1000), nanosPart(0))
      Span(2000.0, Minutes) should have (totalNanos(1000L * 1000 * 2000 * 1000 * 60), millisPart(2000 * 60 * 1000), nanosPart(0))
      Span(2001.0, Minutes) should have (totalNanos(1000L * 1000 * 2001 * 1000 * 60), millisPart(2001 * 60 * 1000), nanosPart(0))
      Span(2002.0, Minutes) should have (totalNanos(1000L * 1000 * 2002 * 1000 * 60), millisPart(2002 * 60 * 1000), nanosPart(0))
      Span(0.1, Minutes) should have (totalNanos(1000L * 1000 * 100 * 60), millisPart(100 * 60), nanosPart(0))
      Span(1.1, Minutes) should have (totalNanos(1000L * 1000 * 1100 * 60), millisPart(1100 * 60), nanosPart(0))
      Span(1.2, Minutes) should have (totalNanos(1000L * 1000 * 1200 * 60), millisPart(1200 * 60), nanosPart(0))
      Span(1.499, Minutes) should have (totalNanos(1000L * 1000 * 1499 * 60), millisPart(1499 * 60), nanosPart(0))
      Span(1.5, Minutes) should have (totalNanos(1000L * 1000 * 1500 * 60), millisPart(1500 * 60), nanosPart(0))
      Span(1.9, Minutes) should have (totalNanos(1000L * 1000 * 1900 * 60), millisPart(1900 * 60), nanosPart(0))
      Span(2.2, Minutes) should have (totalNanos(1000L * 1000 * 2200 * 60), millisPart(2200 * 60), nanosPart(0))
      Span(0.001, Minutes) should have (totalNanos(1000L * 1000 * 60), millisPart(60), nanosPart(0))
      Span(88.0001, Minutes) should have (totalNanos(1000L * 1000 * 5280006), millisPart(88 * 1000 * 60 + 6), nanosPart(0))
      Span(88.000001, Minutes) should have (totalNanos(1000L * 1000 * 5280000 + 60000), millisPart(88 * 1000 * 60), nanosPart(60000))
      Span(88.000000001, Minutes) should have (totalNanos(1000L * 1000 * 5280000 + 60), millisPart(88 * 1000 * 60), nanosPart(60))
      Span(Long.MaxValue.toDouble / 1000 / 1000 / 1000 / 60, Minutes) should have (
        totalNanos(1000L * 1000 * 9223372036854L + 773760),
        millisPart(9223372036854L),
        nanosPart(773760)
      )
    }

    it("should throw IAE if a minutes value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue / 1000 / 1000 / 1000 / 60
      for (i <- Seq(biggest + 1, biggest + 2, biggest + 3, Long.MaxValue)) {
        withClue("i was: " + i) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(i, Minutes)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should throw IAE if a Double minutes value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue.toDouble / 1000 / 1000 / 1000 / 60
      for (d <- Seq(biggest + 1, biggest + 2, biggest + 3, Double.MaxValue)) {
        withClue("d was: " + d) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(d, Minutes)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should construct with valid hours passed") {

      Span(0, Hours) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1, Hour) should have (totalNanos(1000L * 1000 * 1000 * 60 * 60), millisPart(60 * 60 * 1000), nanosPart(0))
      Span(1, Hours) should have (totalNanos(1000L * 1000 * 1000 * 60 * 60), millisPart(60 * 60 * 1000), nanosPart(0))
      Span(2, Hours) should have (totalNanos(1000L * 1000 * 2 * 1000 * 60 * 60), millisPart(2 * 60 * 60 * 1000), nanosPart(0))
      Span(1000, Hours) should have (totalNanos(1000L * 1000 * 1000L * 1000 * 60 * 60), millisPart(1000L * 60 * 60 * 1000), nanosPart(0))
      Span(1001, Hours) should have (totalNanos(1000L * 1000 * 1001L * 1000 * 60 * 60), millisPart(1001L * 60 * 60 * 1000), nanosPart(0))
      Span(1002, Hours) should have (totalNanos(1000L * 1000 * 1002L * 1000 * 60 * 60), millisPart(1002L * 60 * 60 * 1000), nanosPart(0))
      Span(2000, Hours) should have (totalNanos(1000L * 1000 * 2000L * 1000 * 60 * 60), millisPart(2000L * 60 * 60 * 1000), nanosPart(0))
      Span(2001, Hours) should have (totalNanos(1000L * 1000 * 2001L * 1000 * 60 * 60), millisPart(2001L * 60 * 60 * 1000), nanosPart(0))
      Span(2002, Hours) should have (totalNanos(1000L * 1000 * 2002L * 1000 * 60 * 60), millisPart(2002L * 60 * 60 * 1000), nanosPart(0))
      Span(Long.MaxValue / 1000 / 1000 / 1000 / 60 / 60, Hours) should have (
        totalNanos(1000L * 1000 * 9223369200000L),
        millisPart(9223369200000L),
        nanosPart(0)
      )

      Span(0.0, Hours) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1.0, Hour) should have (totalNanos(1000L * 1000 * 1000 * 60 * 60), millisPart(60 * 60 * 1000), nanosPart(0))
      Span(1.0, Hours) should have (totalNanos(1000L * 1000 * 1000 * 60 * 60), millisPart(60 * 60 * 1000), nanosPart(0))
      Span(2.0, Hours) should have (totalNanos(1000L * 1000 * 2 * 1000 * 60 * 60), millisPart(2 * 60 * 60 * 1000), nanosPart(0))
      Span(1000.0, Hours) should have (totalNanos(1000L * 1000 * 1000L * 1000 * 60 * 60), millisPart(1000L * 60 * 60 * 1000), nanosPart(0))
      Span(1001.0, Hours) should have (totalNanos(1000L * 1000 * 1001L * 1000 * 60 * 60), millisPart(1001L * 60 * 60 * 1000), nanosPart(0))
      Span(1002.0, Hours) should have (totalNanos(1000L * 1000 * 1002L * 1000 * 60 * 60), millisPart(1002L * 60 * 60 * 1000), nanosPart(0))
      Span(2000.0, Hours) should have (totalNanos(1000L * 1000 * 2000L * 1000 * 60 * 60), millisPart(2000L * 60 * 60 * 1000), nanosPart(0))
      Span(2001.0, Hours) should have (totalNanos(1000L * 1000 * 2001L * 1000 * 60 * 60), millisPart(2001L * 60 * 60 * 1000), nanosPart(0))
      Span(2002.0, Hours) should have (totalNanos(1000L * 1000 * 2002L * 1000 * 60 * 60), millisPart(2002L * 60 * 60 * 1000), nanosPart(0))
      Span(0.1, Hours) should have (totalNanos(1000L * 1000 * 100 * 60 * 60), millisPart(100 * 60 * 60), nanosPart(0))
      Span(1.1, Hours) should have (totalNanos(1000L * 1000 * 1100 * 60 * 60), millisPart(1100 * 60 * 60), nanosPart(0))
      Span(1.2, Hours) should have (totalNanos(1000L * 1000 * 1200 * 60 * 60), millisPart(1200 * 60 * 60), nanosPart(0))
      Span(1.499, Hours) should have (totalNanos(1000L * 1000 * 1499 * 60 * 60), millisPart(1499 * 60 * 60), nanosPart(0))
      Span(1.5, Hours) should have (totalNanos(1000L * 1000 * 1500 * 60 * 60), millisPart(1500 * 60 * 60), nanosPart(0))
      Span(1.9, Hours) should have (totalNanos(1000L * 1000 * 1900 * 60 * 60), millisPart(1900 * 60 * 60), nanosPart(0))
      Span(2.2, Hours) should have (totalNanos(1000L * 1000 * 2200 * 60 * 60), millisPart(2200 * 60 * 60), nanosPart(0))
      Span(0.001, Hours) should have (totalNanos(1000L * 1000 * 60 * 60), millisPart(60 * 60), nanosPart(0))
      Span(88.0001, Hours) should have (totalNanos(1000L * 1000 * 5280006 * 60), millisPart(88 * 1000 * 60 * 60 + 6 * 60), nanosPart(0))
      Span(88.000001, Hours) should have (totalNanos(1000L * 1000 * 316800003 + 600000), millisPart(88 * 1000 * 60 * 60 + 3), nanosPart(600000))
      Span(88.000000001, Hours) should have (totalNanos(1000L * 1000 * 5280000 * 60 + 3600), millisPart(88 * 1000 * 60 * 60), nanosPart(3600))
      Span(Long.MaxValue.toDouble / 1000 / 1000 / 1000 / 60 / 60, Hours) should have (
        totalNanos(1000L * 1000 * 9223372036854L + 775807),
        millisPart(9223372036854L),
        nanosPart(775807)
      )
    }

    it("should throw IAE if an hours value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue / 1000 / 1000 / 1000 / 60 / 60
      for (i <- Seq(biggest + 1, biggest + 2, biggest + 3, Long.MaxValue)) {
        withClue("i was: " + i) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(i, Hours)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should throw IAE if a Double hours value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue.toDouble / 1000 / 1000 / 1000 / 60 / 60
      for (d <- Seq(biggest + 1, biggest + 2, biggest + 3, Double.MaxValue)) {
        withClue("d was: " + d) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(d, Hours)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should construct with valid days passed") {

      Span(0, Days) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1, Day) should have (totalNanos(1000L * 1000 * 1000 * 60 * 60 * 24), millisPart(60 * 60 * 24 * 1000), nanosPart(0))
      Span(1, Days) should have (totalNanos(1000L * 1000 * 1000 * 60 * 60 * 24), millisPart(60 * 60 * 24 * 1000), nanosPart(0))
      Span(2, Days) should have (totalNanos(1000L * 1000 * 2 * 1000 * 60 * 60 * 24), millisPart(2 * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(1000, Days) should have (totalNanos(1000L * 1000 * 1000L * 1000 * 60 * 60 * 24), millisPart(1000L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(1001, Days) should have (totalNanos(1000L * 1000 * 1001L * 1000 * 60 * 60 * 24), millisPart(1001L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(1002, Days) should have (totalNanos(1000L * 1000 * 1002L * 1000 * 60 * 60 * 24), millisPart(1002L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(2000, Days) should have (totalNanos(1000L * 1000 * 2000L * 1000 * 60 * 60 * 24), millisPart(2000L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(2001, Days) should have (totalNanos(1000L * 1000 * 2001L * 1000 * 60 * 60 * 24), millisPart(2001L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(2002, Days) should have (totalNanos(1000L * 1000 * 2002L * 1000 * 60 * 60 * 24), millisPart(2002L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(Long.MaxValue / 1000 / 1000 / 1000 / 60 / 60 / 24, Days) should have (
        totalNanos(1000L * 1000 * 9223286400000L),
        millisPart(9223286400000L),
        nanosPart(0)
      )

      Span(0.0, Days) should have (totalNanos(0), millisPart(0), nanosPart(0))
      Span(1.0, Day) should have (totalNanos(1000L * 1000 * 1000 * 60 * 60 * 24), millisPart(60 * 60 * 24 * 1000), nanosPart(0))
      Span(1.0, Days) should have (totalNanos(1000L * 1000 * 1000 * 60 * 60 * 24), millisPart(60 * 60 * 24 * 1000), nanosPart(0))
      Span(2.0, Days) should have (totalNanos(1000L * 1000 * 2 * 1000 * 60 * 60 * 24), millisPart(2 * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(1000.0, Days) should have (totalNanos(1000L * 1000 * 1000L * 1000 * 60 * 60 * 24), millisPart(1000L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(1001.0, Days) should have (totalNanos(1000L * 1000 * 1001L * 1000 * 60 * 60 * 24), millisPart(1001L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(1002.0, Days) should have (totalNanos(1000L * 1000 * 1002L * 1000 * 60 * 60 * 24), millisPart(1002L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(2000.0, Days) should have (totalNanos(1000L * 1000 * 2000L * 1000 * 60 * 60 * 24), millisPart(2000L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(2001.0, Days) should have (totalNanos(1000L * 1000 * 2001L * 1000 * 60 * 60 * 24), millisPart(2001L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(2002.0, Days) should have (totalNanos(1000L * 1000 * 2002L * 1000 * 60 * 60 * 24), millisPart(2002L * 60 * 60 * 24 * 1000), nanosPart(0))
      Span(0.1, Days) should have (totalNanos(1000L * 1000 * 100 * 60 * 60 * 24), millisPart(100 * 60 * 60 * 24), nanosPart(0))
      Span(1.1, Days) should have (totalNanos(1000L * 1000 * 1100 * 60 * 60 * 24), millisPart(1100 * 60 * 60 * 24), nanosPart(0))
      Span(1.2, Days) should have (totalNanos(1000L * 1000 * 1200 * 60 * 60 * 24), millisPart(1200 * 60 * 60 * 24), nanosPart(0))
      Span(1.499, Days) should have (totalNanos(1000L * 1000 * 1499 * 60 * 60 * 24), millisPart(1499 * 60 * 60 * 24), nanosPart(0))
      Span(1.5, Days) should have (totalNanos(1000L * 1000 * 1500 * 60 * 60 * 24), millisPart(1500 * 60 * 60 * 24), nanosPart(0))
      Span(1.9, Days) should have (totalNanos(1000L * 1000 * 1900 * 60 * 60 * 24), millisPart(1900 * 60 * 60 * 24), nanosPart(0))
      Span(2.2, Days) should have (totalNanos(1000L * 1000 * 2200 * 60 * 60 * 24), millisPart(2200 * 60 * 60 * 24), nanosPart(0))
      Span(0.001, Days) should have (totalNanos(1000L * 1000 * 60 * 60 * 24), millisPart(60 * 60 * 24), nanosPart(0))
      Span(88.0001, Days) should have (totalNanos(1000L * 1000 * 5280006L * 60 * 24), millisPart(88L * 1000 * 60 * 60 * 24 + 6 * 60 * 24), nanosPart(0))
      Span(88.000001, Days) should have (totalNanos(1000L * 1000 * 7603200086L + 400000), millisPart(88L * 1000 * 60 * 60 * 24 + 86), nanosPart(400000))
      Span(88.000000001, Days) should have (totalNanos(1000L * 1000 * 5280000L * 60 * 24 + 86400), millisPart(88L * 1000 * 60 * 60 * 24), nanosPart(86400))
      Span(Long.MaxValue.toDouble / 1000 / 1000 / 1000 / 60 / 60 / 24, Days) should have (
        totalNanos(1000L * 1000 * 9223372036854L + 775807),
        millisPart(9223372036854L),
        nanosPart(775807)
      )
    }

    it("should throw IAE if a days value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue / 1000 / 1000 / 1000 / 60 / 60 / 24
      for (i <- Seq(biggest + 1, biggest + 2, biggest + 3, Long.MaxValue)) {
        withClue("i was: " + i) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(i, Days)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should throw IAE if a Double days value larger than the largest expressible amount is passed.") {
      val biggest = Long.MaxValue.toDouble / 1000 / 1000 / 1000 / 60 / 60 / 24
      for (d <- Seq(biggest + 1, biggest + 2, biggest + 3, Double.MaxValue)) {
        withClue("d was: " + d) {
          val caught =
            intercept[IllegalArgumentException] {
              Span(d, Days)
            }
          caught.getMessage should include ("Passed length")
        }
      }
    }

    it("should give pretty, localized toStrings reflecting what went in") {
      Span(1, Nanosecond).prettyString should be ("1 nanosecond")
      Span(1, Nanoseconds).prettyString should be ("1 nanosecond")
      Span(2, Nanoseconds).prettyString should be ("2 nanoseconds")
      Span(1.0, Nanosecond).prettyString should be ("1.0 nanosecond")
      Span(1.0, Nanoseconds).prettyString should be ("1.0 nanosecond")
      Span(1.1, Nanoseconds).prettyString should be ("1.1 nanoseconds")
      Span(2.0, Nanoseconds).prettyString should be ("2.0 nanoseconds")

      Span(1, Microsecond).prettyString should be ("1 microsecond")
      Span(1, Microseconds).prettyString should be ("1 microsecond")
      Span(2, Microseconds).prettyString should be ("2 microseconds")
      Span(1.0, Microsecond).prettyString should be ("1.0 microsecond")
      Span(1.0, Microseconds).prettyString should be ("1.0 microsecond")
      Span(1.1, Microseconds).prettyString should be ("1.1 microseconds")
      Span(2.0, Microseconds).prettyString should be ("2.0 microseconds")

      Span(1, Millisecond).prettyString should be ("1 millisecond")
      Span(1, Milliseconds).prettyString should be ("1 millisecond")
      Span(2, Milliseconds).prettyString should be ("2 milliseconds")
      Span(1.0, Millisecond).prettyString should be ("1.0 millisecond")
      Span(1.0, Milliseconds).prettyString should be ("1.0 millisecond")
      Span(1.1, Milliseconds).prettyString should be ("1.1 milliseconds")
      Span(2.0, Milliseconds).prettyString should be ("2.0 milliseconds")

      Span(1, Millis).prettyString should be ("1 millisecond")
      Span(2, Millis).prettyString should be ("2 milliseconds")
      Span(1.0, Millis).prettyString should be ("1.0 millisecond")
      Span(1.1, Millis).prettyString should be ("1.1 milliseconds")
      Span(2.0, Millis).prettyString should be ("2.0 milliseconds")

      Span(1, Second).prettyString should be ("1 second")
      Span(1, Seconds).prettyString should be ("1 second")
      Span(2, Seconds).prettyString should be ("2 seconds")
      Span(1.0, Second).prettyString should be ("1.0 second")
      Span(1.0, Seconds).prettyString should be ("1.0 second")
      Span(1.1, Seconds).prettyString should be ("1.1 seconds")
      Span(2.0, Seconds).prettyString should be ("2.0 seconds")

      Span(1, Minute).prettyString should be ("1 minute")
      Span(1, Minutes).prettyString should be ("1 minute")
      Span(2, Minutes).prettyString should be ("2 minutes")
      Span(1.0, Minute).prettyString should be ("1.0 minute")
      Span(1.0, Minutes).prettyString should be ("1.0 minute")
      Span(1.1, Minutes).prettyString should be ("1.1 minutes")
      Span(2.0, Minutes).prettyString should be ("2.0 minutes")

      Span(1, Hour).prettyString should be ("1 hour")
      Span(1, Hours).prettyString should be ("1 hour")
      Span(2, Hours).prettyString should be ("2 hours")
      Span(1.0, Hour).prettyString should be ("1.0 hour")
      Span(1.0, Hours).prettyString should be ("1.0 hour")
      Span(1.1, Hours).prettyString should be ("1.1 hours")
      Span(2.0, Hours).prettyString should be ("2.0 hours")

      Span(1, Day).prettyString should be ("1 day")
      Span(1, Days).prettyString should be ("1 day")
      Span(2, Days).prettyString should be ("2 days")
      Span(1.0, Day).prettyString should be ("1.0 day")
      Span(1.0, Days).prettyString should be ("1.0 day")
      Span(1.1, Days).prettyString should be ("1.1 days")
      Span(2.0, Days).prettyString should be ("2.0 days")
    }

    it("should have a pretty toString") {
      Span(1, Nanosecond).toString should be ("Span(1, Nanosecond)")
      Span(1, Nanoseconds).toString should be ("Span(1, Nanoseconds)")
      Span(2, Nanoseconds).toString should be ("Span(2, Nanoseconds)")
      Span(1.0, Nanosecond).toString should be ("Span(1.0, Nanosecond)")
      Span(1.0, Nanoseconds).toString should be ("Span(1.0, Nanoseconds)")
      Span(1.1, Nanoseconds).toString should be ("Span(1.1, Nanoseconds)")
      Span(2.0, Nanoseconds).toString should be ("Span(2.0, Nanoseconds)")

      Span(1, Microsecond).toString should be ("Span(1, Microsecond)")
      Span(1, Microseconds).toString should be ("Span(1, Microseconds)")
      Span(2, Microseconds).toString should be ("Span(2, Microseconds)")
      Span(1.0, Microsecond).toString should be ("Span(1.0, Microsecond)")
      Span(1.0, Microseconds).toString should be ("Span(1.0, Microseconds)")
      Span(1.1, Microseconds).toString should be ("Span(1.1, Microseconds)")
      Span(2.0, Microseconds).toString should be ("Span(2.0, Microseconds)")

      Span(1, Millisecond).toString should be ("Span(1, Millisecond)")
      Span(1, Milliseconds).toString should be ("Span(1, Milliseconds)")
      Span(2, Milliseconds).toString should be ("Span(2, Milliseconds)")
      Span(1.0, Millisecond).toString should be ("Span(1.0, Millisecond)")
      Span(1.0, Milliseconds).toString should be ("Span(1.0, Milliseconds)")
      Span(1.1, Milliseconds).toString should be ("Span(1.1, Milliseconds)")
      Span(2.0, Milliseconds).toString should be ("Span(2.0, Milliseconds)")

      Span(1, Millis).toString should be ("Span(1, Millis)")
      Span(2, Millis).toString should be ("Span(2, Millis)")
      Span(1.0, Millis).toString should be ("Span(1.0, Millis)")
      Span(1.1, Millis).toString should be ("Span(1.1, Millis)")
      Span(2.0, Millis).toString should be ("Span(2.0, Millis)")

      Span(1, Second).toString should be ("Span(1, Second)")
      Span(1, Seconds).toString should be ("Span(1, Seconds)")
      Span(2, Seconds).toString should be ("Span(2, Seconds)")
      Span(1.0, Second).toString should be ("Span(1.0, Second)")
      Span(1.0, Seconds).toString should be ("Span(1.0, Seconds)")
      Span(1.1, Seconds).toString should be ("Span(1.1, Seconds)")
      Span(2.0, Seconds).toString should be ("Span(2.0, Seconds)")

      Span(1, Minute).toString should be ("Span(1, Minute)")
      Span(1, Minutes).toString should be ("Span(1, Minutes)")
      Span(2, Minutes).toString should be ("Span(2, Minutes)")
      Span(1.0, Minute).toString should be ("Span(1.0, Minute)")
      Span(1.0, Minutes).toString should be ("Span(1.0, Minutes)")
      Span(1.1, Minutes).toString should be ("Span(1.1, Minutes)")
      Span(2.0, Minutes).toString should be ("Span(2.0, Minutes)")

      Span(1, Hour).toString should be ("Span(1, Hour)")
      Span(1, Hours).toString should be ("Span(1, Hours)")
      Span(2, Hours).toString should be ("Span(2, Hours)")
      Span(1.0, Hour).toString should be ("Span(1.0, Hour)")
      Span(1.0, Hours).toString should be ("Span(1.0, Hours)")
      Span(1.1, Hours).toString should be ("Span(1.1, Hours)")
      Span(2.0, Hours).toString should be ("Span(2.0, Hours)")

      Span(1, Day).toString should be ("Span(1, Day)")
      Span(1, Days).toString should be ("Span(1, Days)")
      Span(2, Days).toString should be ("Span(2, Days)")
      Span(1.0, Day).toString should be ("Span(1.0, Day)")
      Span(1.0, Days).toString should be ("Span(1.0, Days)")
      Span(1.1, Days).toString should be ("Span(1.1, Days)")
      Span(2.0, Days).toString should be ("Span(2.0, Days)")
    }
    // TODO: write tests for equals and hashcode
  }
}
