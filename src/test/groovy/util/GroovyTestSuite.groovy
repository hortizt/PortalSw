package test.groovy.util

def DbUtilTest = AllTestSuite.suite(".", "DbUtilTest.groovy")
def mainUITest = AllTestSuite.suite(".", "mainUITest.groovy")
junit.textui.TestRunner.run(DbUtilTest)
junit.textui.TestRunner.run(mainUITest)