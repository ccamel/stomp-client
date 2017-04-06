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
package me.ccm.stomp.protocol.frames

trait StompFrame extends Immutable {
  def frameName: String

  def headers: Map[String, String]

  def body: Array[Byte]

  def hasHeaders: Boolean = headers.nonEmpty

  def hasBodyContent: Boolean = !body.isEmpty
}

trait StompFrameProps extends Immutable {
  def isClientFrame: Boolean

  def isServerFrame: Boolean = !isClientFrame

  def hasBody: Boolean = false

  def frameName: String
}

trait HeaderKeys

trait CommonHeaderKeys extends HeaderKeys {
  /** The Constant CONTENT_LENGTH. */
  val CONTENT_LENGTH: String = "content-length"
}

trait CommonWithBodyHeaderKeys extends CommonHeaderKeys {
  /** The Constant CONTENT_TYPE. */
  val CONTENT_TYPE: String = "content-type"
}
