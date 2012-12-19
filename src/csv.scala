/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.github.takoi

import scala.io.Source
import scala.collection.Iterator

package object csv {
  implicit def any2string(any: Any) = augmentString(any.asInstanceOf[String])
  
  /** Returns an iterator over the rows of given csv file. Each row is a tuple
    * of the same size as the number of columns.
    * 
    * @param filename
    * @param delimiter character separating fields
    * @param quotechar character that wraps a field
    * @param hasHeader if true the first row in the csv file will be excluded
    */
  def read(
    filename: String,
    delimiter: String = ",",
    quoteChar: Char = '"',
    hasHeader: Boolean = false): Iterator[Product] = {
    
    val fieldSplitter = "%s(?=([^%s]*%s[^%s]*%s)*[^%s]*$)"
    		.format(delimiter, quoteChar, quoteChar, quoteChar, quoteChar, quoteChar)
    val numOfFields = Source.fromFile(filename).getLines().next().split(fieldSplitter).length
    val tupleClass = Class.forName("scala.Tuple" + numOfFields).getConstructors.apply(0)
    
    val file = Source.fromFile(filename)
    val lines = file.getLines()
      .drop(if (hasHeader) 1 else 0)
      .filterNot(_.isEmpty())
    
    val lists = lines.map { line =>
      line.split(fieldSplitter, numOfFields).map(trim(_, quoteChar))
    }
    val tuples = lists.map { t => tupleClass.newInstance(t: _*).asInstanceOf[Product] }
    tuples
  }
  
  private def trim(str: String, character: Char) = {
    val left = if (str.head == character) 1 else 0
    val right = if (str.last == character) 1 else 0
    str.drop(left).dropRight(right)
  }
}
