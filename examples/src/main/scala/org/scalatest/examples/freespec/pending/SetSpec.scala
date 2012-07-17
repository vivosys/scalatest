package org.scalatest.examples.freespec.pending

import org.scalatest._

class SetSpec extends FreeSpec {

  "An empty Set" - {
    "should have size 0" in (pending)
    
    "should produce NoSuchElementException when head is invoked" in {
      intercept[NoSuchElementException] {
        Set.empty.head
      }
    }
  }
}