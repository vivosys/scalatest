/*
 * Copyright 2001-2008 Artima, Inc.
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
package org.scalatest

/**
 * Trait whose instances can rerun tests or other entities (such as suites). An object extending
 * this trait can be passed to a <code>Reporter</code> as part of a <code>Report</code>. The
 * test or other entity about which the report is made can then be rerun by invoking the
 * <code>rerun</code> method on the <code>Rerunnable</code>.
 *
 * @author Bill Venners
 */
trait Rerunner extends ((Reporter, Stopper, Set[String], Set[String], Map[String, Any], Option[Distributor], Tracker, ClassLoader) => Unit) {

  /**
   * Rerun a test or other entity (such as a suite), reporting results to the specified <code>Reporter</code>.
   *
   * @param reporter the <code>Reporter</code> to which results will be reported
   * @param stopper the <code>Stopper</code> that will be consulted to determine whether to stop execution early.
   * @param groupsToInclude a <code>Set</code> of <code>String</code> group names to include during this rerun
   * @param groupsToExclude a <code>Set</code> of <code>String</code> group names to exclude during this rerun
   * @param goodies a <code>Map</code> of key-value pairs that can be used by the suite or test being rerun
   * @param distributor an optional <code>Distributor</code>, into which to put nested <code>Suite</code>s, if any, to be executed
   *              by another entity, such as concurrently by a pool of threads. If <code>None</code>, nested <code>Suite</code>s will be executed sequentially.
   * @param loader the <code>ClassLoader</code> from which to load classes needed to rerun
   *     the test or suite.
   *
   * @throws NullPointerException if any of the passed values are <code>null</code>.
   */
  def apply(reporter: Reporter, stopper: Stopper, groupsToInclude: Set[String], groupsToExclude: Set[String],
            goodies: Map[String, Any], distributor: Option[Distributor], tracker: Tracker, loader: ClassLoader): Unit
}
