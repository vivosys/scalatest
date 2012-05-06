/*
 * Copyright 2001-2011 Artima, Inc.
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

import org.scalatest._
import org.scalatest.exceptions.StackDepthExceptionHelper.getStackDepthFun
import org.scalatest.Suite.anErrorThatShouldCauseAnAbort
import scala.annotation.tailrec
import org.scalatest.exceptions.TestFailedException
import org.scalatest.exceptions.TestPendingException
import org.scalatest.time.Span
import org.scalatest.time.Nanoseconds

// TODO describe backoff algo

/**
 * Trait that provides the <code>eventually</code> construct, which periodically retries executing
 * a passed by-name parameter, until it either succeeds or the configured timeout has been surpassed.
 *
 * <p>
 * The by-name parameter "succeeds" if it returns a result. It "fails" if it throws any exception that
 * would normally cause a test to fail. (These are any exceptions except <a href="TestPendingException"><code>TestPendingException</code></a> and
 * <code>Error</code>s listed in the
 * <a href="Suite.html#errorHandling">Treatment of <code>java.lang.Error</code>s</a> section of the
 * documentation of trait <code>Suite</code>.)
 * </p>
 *
 * <p>
 * For example, the following invocation of <code>eventually</code> would succeed (not throw an exception):
 * </p>
 *
 * <pre class="stHighlight">
 * val xs = 1 to 125
 * val it = xs.iterator
 * eventually { it.next should be (3) }
 * </pre>
 *
 * <p>
 * However, because the default timeout is 150 milliseconds, the following invocation of
 * <code>eventually</code> would ultimately produce a <code>TestFailedException</code>:
 * </p>
 *
 * <pre class="stHighlight">
 * val xs = 1 to 125
 * val it = xs.iterator
 * eventually { Thread.sleep(50); it.next should be (110) }
 * </pre>
 *
 * <p>
 * Assuming the default configuration parameters, a <code>timeout</code> of 150 milliseconds and an <code>interval</code> of 15 milliseconds,
 * were passed implicitly to <code>eventually</code>, the detail message of the thrown
 * <code>TestFailedException</code> would look like:
 * </p>
 *
 * <p>
 * <code>The code passed to eventually never returned normally. Attempted 1 times, sleeping 15 milliseconds between each attempt. Last failure message:
 * 1 was not equal to 110.</code>
 * </p>
 *
 * <a name="patienceConfig"></a><h2>Configuration of <code>eventually</code></h2>
 *
 * <p>
 * The <code>eventually</code> methods of this trait can be flexibly configured.
 * The two configuration parameters for <code>eventually</code> along with their 
 * default values and meanings are described in the following table:
 * </p>
 *
 * <table style="border-collapse: collapse; border: 1px solid black">
 * <tr>
 * <th style="background-color: #CCCCCC; border-width: 1px; padding: 3px; text-align: center; border: 1px solid black">
 * <strong>Configuration Parameter</strong>
 * </th>
 * <th style="background-color: #CCCCCC; border-width: 1px; padding: 3px; text-align: center; border: 1px solid black">
 * <strong>Default Value</strong>
 * </th>
 * <th style="background-color: #CCCCCC; border-width: 1px; padding: 3px; text-align: center; border: 1px solid black">
 * <strong>Meaning</strong>
 * </th>
 * </tr>
 * <tr>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * <code>timeout</code>
 * </td>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * <code>scaled(150 milliseconds)</code>
 * </td>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: left">
 * the maximum amount of time to allow unsuccessful attempts before giving up and throwing <code>TestFailedException</code>
 * </td>
 * </tr>
 * <tr>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * <code>interval</code>
 * </td>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
 * <code>scaled(15 milliseconds)</code>
 * </td>
 * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: left">
 * the amount of time to sleep between each attempt
 * </td>
 * </tr>
 * </table>
 *
 * <p>
 * The <code>eventually</code> methods of trait <code>Eventually</code> each take an <code>PatienceConfig</code>
 * object as an implicit parameter. This object provides values for the two configuration parameters. Trait
 * <code>Eventually</code> provides an implicit <code>val</code> named <code>patienceConfig</code> with each
 * configuration parameter set to its default value. 
 * If you want to set one or more configuration parameters to a different value for all invocations of
 * <code>eventually</code> in a suite you can override this
 * val (or hide it, for example, if you are importing the members of the <code>Eventually</code> companion object rather
 * than mixing in the trait). For example, if
 * you always want the default <code>timeout</code> to be 2 seconds and the default <code>interval</code> to be 5 milliseconds, you
 * can override <code>patienceConfig</code>, like this:
 *
 * <pre class="stHighlight">
 * implicit override val patienceConfig =
 *   PatienceConfig(timeout = scaled(Span(2, Seconds)), interval = scaled(Span(5, Millis)))
 * </pre>
 *
 * <p>
 * Or, hide it by declaring a variable of the same name in whatever scope you want the changed values to be in effect:
 * </p>
 *
 * <pre class="stHighlight">
 * implicit val patienceConfig =
 *   PatienceConfig(timeout = scaled(Span(2, Seconds)), interval = scaled(Span(5, Millis)))
 * </pre>
 *
 * <p>
 * Passing your new default values to <code>scaled</code> is optional, but a good idea because it allows them to 
 * be easily scaled if run on a slower or faster system.
 * </p>
 *
 * <p>
 * In addition to taking a <code>PatienceConfig</code> object as an implicit parameter, the <code>eventually</code> methods of trait
 * <code>Eventually</code> include overloaded forms that take one or two <code>PatienceConfigParam</code>
 * objects that you can use to override the values provided by the implicit <code>PatienceConfig</code> for a single <code>eventually</code>
 * invocation. For example, if you want to set <code>timeout</code> to 5000 for just one particular <code>eventually</code> invocation,
 * you can do so like this:
 * </p>
 *
 * <pre class="stHighlight">
 * eventually (timeout(Span(5, Seconds))) { Thread.sleep(10); it.next should be (110) }
 * </pre>
 *
 * <p>
 * This invocation of <code>eventually</code> will use 5 seconds for the <code>timeout</code> and whatever value is specified by the
 * implicitly passed <code>PatienceConfig</code> object for the <code>interval</code> configuration parameter.
 * If you want to set both configuration parameters in this way, just list them separated by commas:
 * </p>
 * 
 * <pre class="stHighlight">
 * eventually (timeout(Span(5, Seconds)), interval(Span(5, Millis))) { it.next should be (110) }
 * </pre>
 *
 * <p>
 * You can also import or mix in the members of <a href="../time/SpanSugar.html"><code>SpanSugar</code></a> if
 * you want a more concise DSL for expressing time spans:
 * </p>
 *
 * <pre class="stHighlight">
 * eventually (timeout(5 seconds), interval(5 millis)) { it.next should be (110) }
 * </pre>
 *
 * <p>
 * Note that ScalaTest will not scale any time span that is not explicitly passed to <code>scaled</code> to make
 * the meaning of the code as obvious as possible. Thus
 * if you ask for "<code>timeout(5 seconds)</code>" you will get exactly that: a timeout of five seconds. If you want such explicitly
 * given values to be scaled, you must say pass them to <code>scale</code> explicitly like this:
 * </p>
 *
 * <pre class="stHighlight">
 * eventually (timeout(scaled(5 seconds))) { it.next should be (110) }
 * </pre>
 *
 * <p>
 * The previous code says more clearly that the timeout will be five seconds, unless scaled higher or lower by the <code>scaled</code> method.
 * </p>
 *
 * <a name="patienceConfig"></a><h2>Usage note: <code>Eventually</code> intended primarily for integration testing</h2>
 *
 * <p>
 * Although the default timeouts of trait <code>Eventually</code> are tuned for unit testing, the use of <code>Eventually</code> in unit tests is
 * a choice you should question. Usually during unit testing you'll want to mock out subsystems that would require <code>Eventually</code>, such as
 * network services with varying and unpredictable response times. This will allow your unit tests to run as fast as possible while still testing
 * the focused bits of behavior they are designed to test.
 *
 * <p>
 * Nevertheless, because sometimes it will make sense to use <code>Eventually</code> in unit tests (and 
 * because it is destined to happen anyway even when it isn't the best choice), <code>Eventually</code> by default uses
 * timeouts tuned for unit tests: Calls to <code>eventually</code> are more likely to succeed on fast development machines, and if a call does time out, 
 * it will do so quickly so the unit tests can keep moving. 
 * </p>
 *
 * <p>
 * When you are using <code>Eventually</code> for integration testing, on the other hand, the default timeout and interval may be too small. A
 * good way to override them is by mixing in trait <a href="IntegrationPatience.html"><code>IntegrationPatience</code></a> or a similar trait of your
 * own making. Here's an example:
 * </p>
 *
 * <pre class="stHighlight">
 * class ExampleSpec extends FeatureSpec with Eventually with IntegrationPatience {
 *   // Your integration tests here...
 * }
 * </pre>
 *
 * <p>
 * Trait <code>IntegrationPatience</code> increases the default timeout from 150 milliseconds to 15 seconds, and increases the default
 * interval from 15 millisecones to 150 milliseconds. If need be, you can do fine tuning of the timeout and interval by
 * specifying a <a href="../tools/Runner$#timeSpanScaleFactor">time span scale factor</a> when you
 * run your tests.
 * </p>
 *
 * @author Bill Venners
 * @author Chua Chee Seng
 */
trait Eventually extends PatienceConfiguration {

  /**
   * Invokes the passed by-name parameter repeatedly until it either succeeds, or a configured maximum
   * amount of time has passed, sleeping a configured interval between attempts.
   *
   * <p>
   * The by-name parameter "succeeds" if it returns a result. It "fails" if it throws any exception that
   * would normally cause a test to fail. (These are any exceptions except <a href="TestPendingException"><code>TestPendingException</code></a> and
   * <code>Error</code>s listed in the
   * <a href="Suite.html#errorHandling">Treatment of <code>java.lang.Error</code>s</a> section of the
   * documentation of trait <code>Suite</code>.)
   * </p>
   *
   * <p>
   * The maximum amount of time in milliseconds to tolerate unsuccessful attempts before giving up and throwing
   * <code>TestFailedException</code> is configured by the value contained in the passed
   * <code>timeout</code> parameter.
   * The interval to sleep between attempts is configured by the value contained in the passed
   * <code>interval</code> parameter.
   * </p>
   *
   * @param timeout the <code>Timeout</code> configuration parameter
   * @param interval the <code>Interval</code> configuration parameter
   * @param fun the by-name parameter to repeatedly invoke
   * @return the result of invoking the <code>fun</code> by-name parameter, the first time it succeeds
   */
  def eventually[T](timeout: Timeout, interval: Interval)(fun: => T): T =
    eventually(fun)(PatienceConfig(timeout.value, interval.value))

  /**
   * Invokes the passed by-name parameter repeatedly until it either succeeds, or a configured maximum
   * amount of time has passed, sleeping a configured interval between attempts.
   *
   * <p>
   * The by-name parameter "succeeds" if it returns a result. It "fails" if it throws any exception that
   * would normally cause a test to fail. (These are any exceptions except <a href="TestPendingException"><code>TestPendingException</code></a> and
   * <code>Error</code>s listed in the
   * <a href="Suite.html#errorHandling">Treatment of <code>java.lang.Error</code>s</a> section of the
   * documentation of trait <code>Suite</code>.)
   * </p>
   *
   * <p>
   * The maximum amount of time in milliseconds to tolerate unsuccessful attempts before giving up and throwing
   * <code>TestFailedException</code> is configured by the value contained in the passed
   * <code>timeout</code> parameter.
   * The interval to sleep between attempts is configured by the <code>interval</code> field of
   * the <code>PatienceConfig</code> passed implicitly as the last parameter.
   * </p>
   *
   * @param timeout the <code>Timeout</code> configuration parameter
   * @param fun the by-name parameter to repeatedly invoke
   * @param config the <code>PatienceConfig</code> object containing the (unused) <code>timeout</code> and
   *          (used) <code>interval</code> parameters
   * @return the result of invoking the <code>fun</code> by-name parameter, the first time it succeeds
   */
  def eventually[T](timeout: Timeout)(fun: => T)(implicit config: PatienceConfig): T =
    eventually(fun)(PatienceConfig(timeout.value, config.interval))

  /**
   * Invokes the passed by-name parameter repeatedly until it either succeeds, or a configured maximum
   * amount of time has passed, sleeping a configured interval between attempts.
   *
   * <p>
   * The by-name parameter "succeeds" if it returns a result. It "fails" if it throws any exception that
   * would normally cause a test to fail. (These are any exceptions except <a href="TestPendingException"><code>TestPendingException</code></a> and
   * <code>Error</code>s listed in the
   * <a href="Suite.html#errorHandling">Treatment of <code>java.lang.Error</code>s</a> section of the
   * documentation of trait <code>Suite</code>.)
   * </p>
   *
   * <p>
   * The maximum amount of time in milliseconds to tolerate unsuccessful attempts before giving up is configured by the <code>timeout</code> field of
   * the <code>PatienceConfig</code> passed implicitly as the last parameter.
   * The interval to sleep between attempts is configured by the value contained in the passed
   * <code>interval</code> parameter.
   * </p>
   *
   * @param interval the <code>Interval</code> configuration parameter
   * @param fun the by-name parameter to repeatedly invoke
   * @param config the <code>PatienceConfig</code> object containing the (used) <code>timeout</code> and
   *          (unused) <code>interval</code> parameters
   * @return the result of invoking the <code>fun</code> by-name parameter, the first time it succeeds
   */
  def eventually[T](interval: Interval)(fun: => T)(implicit config: PatienceConfig): T =
    eventually(fun)(PatienceConfig(config.timeout, interval.value))

  /**
   * Invokes the passed by-name parameter repeatedly until it either succeeds, or a configured maximum
   * amount of time has passed, sleeping a configured interval between attempts.
   *
   * <p>
   * The by-name parameter "succeeds" if it returns a result. It "fails" if it throws any exception that
   * would normally cause a test to fail. (These are any exceptions except <a href="TestPendingException"><code>TestPendingException</code></a> and
   * <code>Error</code>s listed in the
   * <a href="Suite.html#errorHandling">Treatment of <code>java.lang.Error</code>s</a> section of the
   * documentation of trait <code>Suite</code>.)
   * </p>
   *
   * <p>
   * The maximum amount of time in milliseconds to tolerate unsuccessful attempts before giving up is configured by the <code>timeout</code> field of
   * the <code>PatienceConfig</code> passed implicitly as the last parameter.
   * The interval to sleep between attempts is configured by the <code>interval</code> field of
   * the <code>PatienceConfig</code> passed implicitly as the last parameter.
   * </p>
   *
   * @param fun the by-name parameter to repeatedly invoke
   * @param config the <code>PatienceConfig</code> object containing the <code>timeout</code> and
   *          <code>interval</code> parameters
   * @return the result of invoking the <code>fun</code> by-name parameter, the first time it succeeds
   */
  def eventually[T](fun: => T)(implicit config: PatienceConfig): T = {
    val startNanos = System.nanoTime
    def makeAValiantAttempt(): Either[Throwable, T] = {
      try {
        Right(fun)
      }
      catch {
        case tpe: TestPendingException => throw tpe
        case e: Throwable if !anErrorThatShouldCauseAnAbort(e) => Left(e)
      }
    }

    val initialInterval = Span(config.interval.totalNanos * 0.1, Nanoseconds) // config.interval scaledBy 0.1

    @tailrec
    def tryTryAgain(attempt: Int): T = {
      val timeout = config.timeout
      val interval = config.interval
      makeAValiantAttempt() match {
        case Right(result) => result
        case Left(e) => 
          val duration = System.nanoTime - startNanos
          if (duration < timeout.totalNanos) {
            if (duration < interval.totalNanos) // For first interval, we wake up every 1/10 of the interval.  This is mainly for optimization purpose. 
              Thread.sleep(initialInterval.millisPart, initialInterval.nanosPart)
            else
              Thread.sleep(interval.millisPart, interval.nanosPart)
          }
          else {
            def msg =
              if (e.getMessage == null)
                Resources("didNotEventuallySucceed", attempt.toString, interval.prettyString)
              else
                Resources("didNotEventuallySucceedBecause", attempt.toString, interval.prettyString, e.getMessage)
            throw new TestFailedException(
              sde => Some(msg),
              Some(e),
              getStackDepthFun("Eventually.scala", "eventually")
            )
          }

          tryTryAgain(attempt + 1)
      }
    }
    tryTryAgain(1)
  }
}

/**
 * Companion object that facilitates the importing of <code>Eventually</code> members as 
 * an alternative to mixing in the trait. One use case is to import <code>Eventually</code>'s members so you can use
 * them in the Scala interpreter:
 *
 * <pre class="stREPL">
 * $ scala -cp scalatest-1.8.jar
 * Welcome to Scala version 2.9.1.final (Java HotSpot(TM) 64-Bit Server VM, Java 1.6.0_29).
 * Type in expressions to have them evaluated.
 * Type :help for more information.
 *
 * scala&gt; import org.scalatest._
 * import org.scalatest._
 *
 * scala&gt; import matchers.ShouldMatchers._
 * import matchers.ShouldMatchers._
 *
 * scala&gt; import concurrent.Eventually._
 * import concurrent.Eventually._
 *
 * scala&gt; val xs = 1 to 125
 * xs: scala.collection.immutable.Range.Inclusive = Range(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, ..., 125)
 * 
 * scala&gt; val it = xs.iterator
 * it: Iterator[Int] = non-empty iterator
 *
 * scala&gt; eventually { it.next should be (3) }
 *
 * scala&gt; eventually { Thread.sleep(999); it.next should be (3) }
 * org.scalatest.TestFailedException: The code passed to eventually never returned normally.
 *     Attempted 2 times, sleeping 10 milliseconds between each attempt.
 *   at org.scalatest.Eventually$class.tryTryAgain$1(Eventually.scala:313)
 *   at org.scalatest.Eventually$class.eventually(Eventually.scala:322)
 *   ...
 * </pre>
 */
object Eventually extends Eventually
