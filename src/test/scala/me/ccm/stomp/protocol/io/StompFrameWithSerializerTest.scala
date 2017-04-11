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

package me.ccm.stomp.protocol.io

import me.ccm.stomp.protocol.frames.client.Ack
import me.ccm.stomp.protocol.frames.server.Message
import me.ccm.stomp.util.ByteUtils.asHexDump
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

/**
  *
  */
class StompFrameWithSerializerTest extends FlatSpec with TableDrivenPropertyChecks with Matchers {
  val data =
    Table(
      ("frame", "binary representation"),
      (Ack("123"),
        """00000000  41 43 4b 0a 69 64 3a 31 32 33 0a 0a 00           |ACK.id:123...|
          #""".stripMargin('#')),
      (Message("dest-1", "message-id", "sub-1", Some("ack-1"), Some("application/text"), Array(1, 2, 3, 4, 5).map(_.toByte)),
        """00000000  4d 45 53 53 41 47 45 0a 6d 65 73 73 61 67 65 2d  |MESSAGE.message-|
          #00000010  69 64 3a 6d 65 73 73 61 67 65 2d 69 64 0a 73 75  |id:message-id.su|
          #00000020  62 73 63 72 69 70 74 69 6f 6e 3a 73 75 62 2d 31  |bscription:sub-1|
          #00000030  0a 63 6f 6e 74 65 6e 74 2d 6c 65 6e 67 74 68 3a  |.content-length:|
          #00000040  35 0a 63 6f 6e 74 65 6e 74 2d 74 79 70 65 3a 61  |5.content-type:a|
          #00000050  70 70 6c 69 63 61 74 69 6f 6e 2f 74 65 78 74 0a  |pplication/text.|
          #00000060  61 63 6b 3a 61 63 6b 2d 31 0a 64 65 73 74 69 6e  |ack:ack-1.destin|
          #00000070  61 74 69 6f 6e 3a 64 65 73 74 2d 31 0a 0a 01 02  |ation:dest-1....|
          #00000080  03 04 05 00                                      |....|
          #""".stripMargin('#'))
    )

  "A frame " should "be correctly serialized" in {
    forAll(data) { (frame, result) =>
      asHexDump(frame.asStompBytes) should be(result)
    }
  }
}
