package org.scalatest.concurrent

import org.scalatest.time.{Span, Millis}


/**
 * Trait that defines an abstract <code>patienceConfig</code> method that is implemented in <code>PatienceConfiguration</code> and can
 * be overriden in stackable modification traits such as <code>IntegrationPatience</code>.
 *
 * <p>
 * The main purpose of <code>AbstractPatienceConfiguration</code> is to differentiate core <code>PatienceConfiguration</code>
 * traits, such as <code>Eventually</code> and <code>AsyncAssertions</code>, from stackable
 * modification traits for <code>PatienceConfiguration</code>s such as <code>IntegrationPatience</code>.
 * Because these stackable traits extend <code>AbstractPatienceConfiguration</code> 
 * instead of <code>Suite</code>, you can't simply mix in a stackable trait:
 * </p>
 *
 * <pre class="stHighlight">
 * class ExampleSpec extends FunSpec with IntegrationPatience // Won't compile
 * </pre>
 *
 * <p>
 * The previous code is undesirable because <code>IntegrationPatience</code> would have no affect on the class. Instead, you need to mix
 * in a core <code>PatienceConfiguration</code> trait and mix the stackable <code>IntegrationPatience</code> trait
 * into that, like this:
 * </p>
 *
 * <pre class="stHighlight">
 * class ExampleSpec extends FunSpec with Eventually with IntegrationPatience // Compiles fine
 * </pre>
 *
 * <p>
 * The previous code is better because <code>IntegrationPatience</code> does have an effect: it modifies the behavior
 * of <code>Eventually</code>.
 * </p>
 *
 * @author Bill Venners
 */
trait AbstractPatienceConfiguration extends ScaledTimeSpans {

  /**
   * Configuration object for traits <code>Eventually</code> and <code>AsyncAssertions</code>.
   *
   * <p>
   * The default values for the parameters are:
   * </p>
   *
   * <table style="border-collapse: collapse; border: 1px solid black">
   * <tr>
   * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
   * <code>timeout</code>
   * </td>
   * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
   * <code>scaled(150 milliseconds)</code>
   * </td>
   * </tr>
   * <tr>
   * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
   * <code>interval</code>
   * </td>
   * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
   * <code>scaled(15 milliseconds)</code>
   * </td>
   * </tr>
   * </table>
   *
   * @param timeout the maximum amount of time to retry before giving up and throwing
   *   <code>TestFailedException</code>.
   * @param interval the amount of time to sleep between each attempt
   *
   * @author Bill Venners
   * @author Chua Chee Seng
   */
  final case class PatienceConfig(timeout: Span = scaled(Span(150, Millis)), interval: Span = scaled(Span(15, Millis)))

  /**
   * Returns a <code>PatienceConfig</code> value providing default configuration values if implemented and made implicit in subtraits.
   */
  def patienceConfig: PatienceConfig
}
