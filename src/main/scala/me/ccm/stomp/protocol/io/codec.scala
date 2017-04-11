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

package me.ccm.stomp.protocol

import me.ccm.stomp.protocol.frames.StompFrame

import scala.collection.mutable

package object io {
  val FrameEnd: Byte = 0x00
  val LineFeed: Byte = 0x0A
  val CarriageReturn: Byte = 0x0D
  val Colon: Byte = ':'.toByte

  implicit class StompFrameWithSerializer(f: StompFrame) {
    /**
      * @return a STOMP byte string representation of a [StompFrame], ready to be put in the wire.
      */
    def asStompBytes: Array[Byte] = {
      val builder = new mutable.ArrayBuilder.ofByte

      builder ++= f.frameName.getBytes

      f.headers.map {
        case (k, v) =>
          builder += LineFeed
          builder ++= k.getBytes
          builder += Colon
          builder ++= v.getBytes
      }

      builder += LineFeed
      builder += LineFeed
      builder ++= f.body

      builder += FrameEnd

      builder.result()
    }
  }

}
