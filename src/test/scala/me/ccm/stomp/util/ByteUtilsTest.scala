/*
 * MIT License
 *
 * Copyright (c) 2017 Chris Camel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.ccm.stomp.util

import me.ccm.stomp.util.ByteUtils.asHexDump
import org.scalatest.FlatSpec
import org.scalatest.prop.TableDrivenPropertyChecks._

import scala.Array.emptyByteArray

/**
  *
  */
class ByteUtilsTest extends FlatSpec {

  val data =
    Table(
      ("bytes", "output size", "Hex String representation"),
      (emptyByteArray, 0x10, ""),
      ("Hello world!".getBytes, 0x10,
        "00000000  48 65 6c 6c 6f 20 77 6f 72 6c 64 21              |Hello world!|\n"),
      ("Lorem ipsum dolo".getBytes, 0x10,
        """00000000  4c 6f 72 65 6d 20 69 70 73 75 6d 20 64 6f 6c 6f  |Lorem ipsum dolo|
          #""".stripMargin('#')),
      ("Lorem ipsum dolor sit amet, consectetur adipiscing elit.".getBytes, 0x10,
        """00000000  4c 6f 72 65 6d 20 69 70 73 75 6d 20 64 6f 6c 6f  |Lorem ipsum dolo|
          #00000010  72 20 73 69 74 20 61 6d 65 74 2c 20 63 6f 6e 73  |r sit amet, cons|
          #00000020  65 63 74 65 74 75 72 20 61 64 69 70 69 73 63 69  |ectetur adipisci|
          #00000030  6e 67 20 65 6c 69 74 2e                          |ng elit.|
          #""".stripMargin('#')),
      ("Lorem ipsum dolo".getBytes, 0x05,
        """00000000  4c 6f 72 65 6d  |Lorem|
          #00000005  20 69 70 73 75  | ipsu|
          #0000000a  6d 20 64 6f 6c  |m dol|
          #0000000f  6f              |o|
          #""".stripMargin('#')),
      ("hello".getBytes, 0x01,
        """00000000  68  |h|
            #00000001  65  |e|
            #00000002  6c  |l|
            #00000003  6c  |l|
            #00000004  6f  |o|
            #""".stripMargin('#'))
    )

  "asHexDump" should "return the expected Hex String representation for a given array of bytes" in {
    forAll(data) { (bytes: Array[Byte], size: Int, result: String) =>
      assert(asHexDump(bytes, size) === result)
    }
  }

  "asHexDump" should "produce an exception when size is negative" in {
    assertThrows[IllegalArgumentException] {
      asHexDump(emptyByteArray, 0)
    }
    assertThrows[IllegalArgumentException] {
      asHexDump(emptyByteArray, -5)
    }
  }
}