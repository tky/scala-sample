package com.tky.sample.fileio

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import java.io.File
import com.tky.sample.fileio.DefaultSettings._

case class Person(name: String, age: Int)

class FileIOSuite extends FunSuite {
  test("write and read") {
    val file = new File("src/test/resources/dest.txt")
    FileIO.write(file, "test")
    assert(FileIO.read(file).toList == List("test"))
    file.delete()
  }

  test("write strings and read") {
    val file = new File("src/test/resources/dest.txt")
    FileIO.writeStrings(file, List("test1", "test2", "test3"))
    assert(FileIO.read(file).toList == List("test1", "test2", "test3"))
    file.delete()
  }

  test("write string and read") {
    val file = new File("src/test/resources/dest.txt")
    FileIO.writeObjects(file, List("test1", "test2", "test3"))
    assert(FileIO.read(file).toList == List("test1", "test2", "test3"))
    file.delete()
  }

  test("write int and read") {
    val file = new File("src/test/resources/dest.txt")
    FileIO.writeObjects(file, List(1, 2, 3))
    assert(FileIO.read(file).toList == List("1", "2", "3"))
    file.delete()
  }

  test("write int and read int") {
    val file = new File("src/test/resources/dest.txt")
    FileIO.writeObjects(file, List(1, 2, 3))
    assert(FileIO.readObjects(file).toList == List(1, 2, 3))
    file.delete()
  }

  implicit object PersonWritable extends FileWritable[Person] {
    def format(p: Person): String = p.name + "," + p.age
  }

  implicit object PersonReadable extends FileReadable[Person] {
    def parse(v: String): Person = {
      v.split(",") match {
        case Array(name, age) => Person(name, age.toInt)
        case _ => throw new IllegalArgumentException
      }
    }
  }

  test("write and read person object") {
    val file = new File("src/test/resources/dest.txt")
    FileIO.writeObjects(file, List(Person("tky", 32)))
    assert(FileIO.readObjects(file)(PersonReadable).toList == List(Person("tky", 32)))
    // this code can't compile...
    // assert(FileIO.readObjects(file).toList == List(Person("tky", 32)))
    file.delete()
  }
}



