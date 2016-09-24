/*
* Copyright (C) 2014-2015 Juergen Pfundt
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.chelona

trait TurtleReturnValue extends RDFReturnType {

  case class TurtleString(s: TurtleElement) extends TurtleReturnValue

  case class TurtleTriple(s: TurtleElement, p: TurtleElement, o: TurtleElement) extends TurtleReturnValue

  case class TurtleTriples(values: List[TurtleTriple]) extends TurtleReturnValue

  case class TurtleComment(value: TurtleElement) extends TurtleReturnValue

}

object TurtleReturnValue extends TurtleReturnValue