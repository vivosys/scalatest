package org.scalatest.examples.wordspec.ignore

import org.scalatest.WordSpec

class SetSpec extends WordSpec {
  
  "An empty Set" should {
    "have size 0" ignore {
      assert(Set.empty.size === 0)
    }
    
    "produce NoSuchElementException when head is invoked" in {
      intercept[NoSuchElementException] {
        Set.empty.head
      }
    }
  }
}