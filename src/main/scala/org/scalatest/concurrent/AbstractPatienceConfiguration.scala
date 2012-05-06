package org.scalatest.concurrent

import org.scalatest.time.{Span, Millis}


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
   * timeout
   * </td>
   * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
   * 150 milliseconds
   * </td>
   * </tr>
   * <tr>
   * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
   * interval
   * </td>
   * <td style="border-width: 1px; padding: 3px; border: 1px solid black; text-align: center">
   * 15 milliseconds
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

  def patienceConfig: PatienceConfig
}
