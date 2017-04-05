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

/**
  *
  */
object ByteUtils {
  def asHexDump(bs: Array[Byte], size: Int = 0x10): String = {
    require(bs != null, "array of bytes must not be null")
    require(size > 0, "size mut be positive")

    val bufBytes = new Array[Option[Byte]](size)
    val bufChars = new Array[Option[Char]](size)
    val builder = new StringBuilder

    for (offset <- bs.indices) {
      val b = bs(offset)
      val p = offset % size

      bufBytes(p) = Some(b)
      bufChars(p) = Some(if (b >= 32 && b < 127) b.toChar else '.')

      if ((p == size - 1) || (offset + 1 == bs.length)) {
        val start = (offset / size) * size
        for (i <- p + 1 until size) {
          bufBytes(i) = None
          bufChars(i) = None
        }
        builder.append(f"$start%08x")
          .append("  ")
          .append(bufBytes.map {
            case Some(v) => "%02x".format(v.toInt & 0xff)
            case None => "  "
          }.mkString(" "))
          .append("  ")
          .append(bufChars.flatten.mkString("|", "", "|"))
          .append("\n")
      }

    }
    builder.toString
  }

}
