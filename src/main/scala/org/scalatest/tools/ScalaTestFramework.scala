package org.scalatest.tools

import org.scalatools.testing._
import org.scalatest.tools.Runner.parsePropertiesArgsIntoMap
import org.scalatest.tools.Runner.parseCompoundArgIntoSet
import org.scalatest.tools.Runner.parseChosenStylesIntoChosenStyleSet
import org.scalatest.tools.Runner.parseSpanScaleFactor
import org.scalatest.tools.Runner.parseConcurrentNumArg
import SuiteDiscoveryHelper._
import StringReporter.colorizeLinesIndividually
import org.scalatest.Suite.formatterForSuiteStarting
import org.scalatest.Suite.formatterForSuiteCompleted
import org.scalatest.Suite.formatterForSuiteAborted
import org.scalatest.events.SuiteStarting
import org.scalatest.events.SuiteCompleted
import org.scalatest.events.SuiteAborted
import org.scalatest.Reporter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Class that makes ScalaTest tests visible to sbt.
 *
 * <p>
 * To use ScalaTest from within sbt, simply add a line like this to your project file, replacing 1.5 with whatever version you desire:
 * </p>
 *
 * <pre class="stExamples">
 * val scalatest = "org.scalatest" % "scalatest_2.8.1" % "1.5"
 * </pre>
 *
 * <p>
 * You can configure the output shown when running with sbt in four ways: 1) turn off color, 2) show
 * short stack traces, 3) full stack traces, and 4) show durations for everything. To do that
 * you need to add test options, like this:
 * </p>
 *
 * <pre class="stExamples">
 * override def testOptions = super.testOptions ++
 *   Seq(TestArgument(TestFrameworks.ScalaTest, "-oD"))
 * </pre>
 *
 * <p>
 * After the -o, place any combination of:
 * </p>
 *
 * <ul>
 * <li>D - show durations</li>
 * <li>S - show short stack traces</li>
 * <li>F - show full stack traces</li>
 * <li>W - without color</li>
 * </ul>
 *
 * <p>
 * For example, "-oDF" would show full stack traces and durations (the amount
 * of time spent in each test).
 * </p>
 *
 * @author Bill Venners
 * @author Josh Cough
 */
class ScalaTestFramework extends Framework {

  /**
   * Returns <code>"ScalaTest"</code>, the human readable name for this test framework.
   */
  def name = "ScalaTest"

  /**
   * Returns an array containing fingerprint for ScalaTest's test, which are classes  
   * whose superclass name is <code>org.scalatest.Suite</code>
   * or is annotated with <code>org.scalatest.WrapWith</code>.
   */
  def tests =
    Array(
      new org.scalatools.testing.TestFingerprint {
        def superClassName = "org.scalatest.Suite"
        def isModule = false
      },
      new org.scalatools.testing.AnnotatedFingerprint {
        def annotationName = "org.scalatest.WrapWith"
        def isModule = false
      }
    )

  /**
   * Returns an <code>org.scalatools.testing.Runner</code> that will load test classes via the passed <code>testLoader</code>
   * and direct output from running the tests to the passed array of <code>Logger</code>s.
   */
  def testRunner(testLoader: ClassLoader, loggers: Array[Logger]) = {
    new ScalaTestRunner(testLoader, loggers)
  }

  /**The test runner for ScalaTest suites. It is compiled in a second step after the rest of sbt.*/
  private[tools] class ScalaTestRunner(val testLoader: ClassLoader, val loggers: Array[Logger]) extends org.scalatools.testing.Runner2 {

    import org.scalatest._
    
    private class SbtLogInfoReporter(presentAllDurations: Boolean, presentInColor: Boolean, presentShortStackTraces: Boolean, presentFullStackTraces: Boolean) 
      extends StringReporter(presentAllDurations, presentInColor, presentShortStackTraces, presentFullStackTraces) {
    
      protected def printPossiblyInColor(text: String, ansiColor: String) {
          loggers.foreach { logger =>
            logger.info(if (logger.ansiCodesSupported && presentInColor) colorizeLinesIndividually(text, ansiColor) else text)
          }
      }

      def dispose() = ()
    }
    
    /* 
      test-only FredSuite -- -A -B -C -d  all things to right of == come in as a separate string in the array
 the other way is to set up the options and when I say test it always comes in that way

 new wqay, if one framework

testOptions in Test += Tests.Arguments("-d", "-g")

so each of those would come in as one separate string in the aray

testOptions in Test += Tests.Arguments(TestFrameworks.ScalaTest, "-d", "-g")

Remember:

maybe add a distributor like thing to run
maybe add some event things like pending, ignored as well skipped
maybe a call back for the summary

st look at wiki on xsbt

tasks & commands. commands have full control over everything.
tasks are more integrated, don't need to know as much.
write a sbt plugin to deploy the task.

     */
    def run(testClassName: String, fingerprint: Fingerprint, eventHandler: EventHandler, args: Array[String]) {
      val testClass = Class.forName(testClassName, true, testLoader)
      // println("sbt args: " + args.toList)
      if (isAccessibleSuite(testClass) || isRunnable(testClass)) {

        val (propertiesArgsList, includesArgsList, excludesArgsList, repoArg, chosenStyles, spanScaleFactors, concurrentList) 
          = parsePropsAndTags(args.filter(!_.equals("")))
        val propertiesMap: Map[String, String] = parsePropertiesArgsIntoMap(propertiesArgsList)
        val tagsToInclude: Set[String] = parseCompoundArgIntoSet(includesArgsList, "-n")
        val tagsToExclude: Set[String] = parseCompoundArgIntoSet(excludesArgsList, "-l")
        val chosenStyleSet: Set[String] = parseChosenStylesIntoChosenStyleSet(chosenStyles, "-y")
        Runner.spanScaleFactor = parseSpanScaleFactor(spanScaleFactors, "-F")
        val concurrent: Boolean = !concurrentList.isEmpty
        val numThreads: Int = parseConcurrentNumArg(concurrentList)
        
        if (propertiesMap.isDefinedAt("org.scalatest.ChosenStyles"))
          throw new IllegalArgumentException("Property name 'org.scalatest.ChosenStyles' is used by ScalaTest, please choose other property name.")
        val configMap = 
          if (chosenStyleSet.isEmpty)
            propertiesMap
          else
            propertiesMap + ("org.scalatest.ChosenStyles" -> chosenStyleSet)
        
        val filter = org.scalatest.Filter(if (tagsToInclude.isEmpty) None else Some(tagsToInclude), tagsToExclude)
        
        val (presentAllDurations, presentInColor, presentShortStackTraces, presentFullStackTraces) =
          repoArg match {
            case Some(arg) => (
              arg contains 'D',
              !(arg contains 'W'),
              arg contains 'S',
              arg contains 'F'
             )
             case None => (false, true, false, false)
          }

        
        val logInfoReporter = new SbtLogInfoReporter(presentAllDurations, presentInColor, presentShortStackTraces, presentFullStackTraces)
        val report = new SbtReporter(eventHandler, Some(logInfoReporter))
        
        val tracker = new Tracker
        val suiteStartTime = System.currentTimeMillis

        val wrapWithAnnotation = testClass.getAnnotation(classOf[WrapWith])
        val suite = 
        if (wrapWithAnnotation == null)
          testClass.newInstance.asInstanceOf[Suite]
        else {
          val suiteClazz = wrapWithAnnotation.value
          val constructorList = suiteClazz.getDeclaredConstructors()
          val constructor = constructorList.find { c => 
              val types = c.getParameterTypes
              types.length == 1 && types(0) == classOf[java.lang.Class[_]]
            }
            constructor.get.newInstance(testClass).asInstanceOf[Suite]
        }

        val formatter = formatterForSuiteStarting(suite)

        report(SuiteStarting(tracker.nextOrdinal(), suite.suiteName, Some(testClass.getName), formatter, None))

        try {
          if (concurrent) {
            val poolSize =
              if (numThreads > 0) numThreads
              else Runtime.getRuntime.availableProcessors * 2

            val execSvc: ExecutorService = Executors.newFixedThreadPool(poolSize)
            try {
              val distributor = new ConcurrentDistributor(report, new Stopper {}, filter, configMap, execSvc)
              distributor.apply(suite, tracker.nextTracker())
              distributor.waitUntilDone()
            }
            finally {
              execSvc.shutdown()
            }
          }
          else
            suite.run(None, report, new Stopper {}, filter, configMap, None, tracker)

          val formatter = formatterForSuiteCompleted(suite)

          val duration = System.currentTimeMillis - suiteStartTime
          report(SuiteCompleted(tracker.nextOrdinal(), suite.suiteName, Some(testClass.getName), Some(duration), formatter, None))
        }
        catch {       
          case e: Exception => {

            // TODO: Could not get this from Resources. Got:
            // java.util.MissingResourceException: Can't find bundle for base name org.scalatest.ScalaTestBundle, locale en_US
            val rawString = "Exception encountered when attempting to run suite " + testClass.getName + 
                            (if (e.getMessage != null) 
                              ": " + e.getMessage 
                            else 
                              ".")
                
            val formatter = formatterForSuiteAborted(suite, rawString)

            val duration = System.currentTimeMillis - suiteStartTime
            report(SuiteAborted(tracker.nextOrdinal(), rawString, suite.suiteName, Some(testClass.getName), Some(e), Some(duration), formatter, None))
          }
        }
      }
      else throw new IllegalArgumentException("Class is not an accessible org.scalatest.Suite: " + testClassName)
    }

    private val emptyClassArray = new Array[java.lang.Class[T] forSome {type T}](0)
    
    private class SbtReporter(eventHandler: EventHandler, report: Option[Reporter]) extends Reporter {
      
      import org.scalatest.events._

      def fireEvent(tn: String, r: Result, e: Option[Throwable]) = {
        eventHandler.handle(
          new org.scalatools.testing.Event {
            def testName = tn
            def description = tn
            def result = r
            def error = e getOrElse null
          }
        )
      }
      
      override def apply(event: Event) {
        report match {
          case Some(report) => report(event)
          case None =>
        }
        event match {
          // the results of running an actual test
          case t: TestPending => fireEvent(t.testName, Result.Skipped, None)
          case t: TestFailed => fireEvent(t.testName, Result.Failure, t.throwable)
          case t: TestSucceeded => fireEvent(t.testName, Result.Success, None)
          case t: TestIgnored => fireEvent(t.testName, Result.Skipped, None)
          case t: SuiteAborted => fireEvent("!!! Suite Aborted !!!", Result.Failure, t.throwable)
          case _ => 
        }
      }
    }

    private[scalatest] def parsePropsAndTags(args: Array[String]) = {

      import collection.mutable.ListBuffer

      val props = new ListBuffer[String]()
      val includes = new ListBuffer[String]()
      val excludes = new ListBuffer[String]()
      var repoArg: Option[String] = None
      val chosenStyles = new ListBuffer[String]()
      val spanScaleFactors = new ListBuffer[String]()
      val concurrent = new ListBuffer[String]()

      val it = args.iterator
      while (it.hasNext) {

        val s = it.next

        if (s.startsWith("-D")) {
          props += s
        }
        else if (s.startsWith("-n")) {
          includes += s
          if (it.hasNext)
            includes += it.next
        }
        else if (s.startsWith("-l")) {
          excludes += s
          if (it.hasNext)
            excludes += it.next
        }
        else if (s.startsWith("-o")) {
          if (repoArg.isEmpty) // Just use first one. Ignore any others.
            repoArg = Some(s)
        }
        else if (s.startsWith("-y")) {
          chosenStyles += s
          if (it.hasNext)
            chosenStyles += it.next()
        }
        else if (s.startsWith("-F")) {
          spanScaleFactors += s
          if (it.hasNext)
            spanScaleFactors += it.next()
        }
        else if (s.startsWith("-P")) {
          concurrent += s
        }
        else if (s == "sequential") {
          // To skip as it is passed in from Play 2.0 as arg to specs2.
          println("Warning: \"sequential\" is ignored by ScalaTest. To get rid of this warning, please add \"testOptions in Test := Nil\" in main definition of your project build file.")
        }
        //      else if (s.startsWith("-b")) {
        //
        //        testNGXMLFiles += s
        //        if (it.hasNext)
        //          testNGXMLFiles += it.next
        //      }
        else {
          throw new IllegalArgumentException("Unrecognized argument: " + s)
        }
      }
      (props.toList, includes.toList, excludes.toList, repoArg, chosenStyles.toList, spanScaleFactors.toList, concurrent.toList)
    }
  }
}
