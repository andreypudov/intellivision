#!/bin/sh

#
# IntelliJustice Intelligent Referee Assistant System 
#
# The MIT License
#
# Copyright 2013 Andrey Pudov.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#

#
# Builds parser Java executable file.
#
# @author    Andrey Pudov        <mail@andreypudov.com>
# @version   0.00.00
# %name      compile.sh
# %date      %date      09:00:00 PM, Jul 12, 2013
#

rm --force parser.jar

# compiles Scala source code
scalac -deprecation -feature -classpath "lib/dom4j-1.6.1.jar:lib/icu4j-51_2.jar:lib/itextpdf-5.4.2.jar:lib/jsoup-1.7.2.jar:lib/opencsv-2.3.jar:lib/poi-3.9-20121203.jar:lib/poi-ooxml-3.9-20121203.jar:lib/poi-ooxml-schemas-3.9-20121203.jar:lib/stax-api-1.0.1.jar:lib/xmlbeans-2.3.0.jar:lib/scala-library.jar" Athlete.scala Competition.scala CSVFile.scala Parser.scala Reader.scala ReaderCSV.scala Writer.scala

# creates Java executable file
jar cfm parser.jar manifest.mf flags *.class

# removes class files
rm *.class

# executes command-line parser
java -jar parser.jar --source=source.csv --report=report.xlsx --verbose --method=rus