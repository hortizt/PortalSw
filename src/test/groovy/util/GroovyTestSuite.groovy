package test.groovy.util

def suite = AllTestSuite.suite(".", "*Test.groovy")
junit.textui.TestRunner.run(suite)