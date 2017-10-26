#!/usr/bin/env bash

rm -rf classes
mkdir classes
scalac -classpath classes/ -d classes/ Actor.scala 
scalac -classpath classes/ -d classes/ exercise2.scala -feature

scala -cp classes Chapter7.Main
