define([
        "jquery",
        "backbone",
        "underscore",
        "base/module/CommonModule",
        "base/module/Constants",
        "testsuite/model/TestSuiteModel"
    ],
    function($, Backbone, _, CommonModule, Constants, TestSuiteModel) {

        beforeEach(function() {});

        describe("TestSuiteModel validations", function() {

            it("should return error when name is empty", function() {
                var testSuite = new TestSuiteModel();
                expect(testSuite.isValid()).toBe(false);
            });

            it("should return error when projectId is empty", function() {
                var testSuite = new TestSuiteModel({ name : "foo"});
                expect(testSuite.isValid()).toBe(false);
            });

            it("should return error when no testcases are selected", function() {
                var testSuite = new TestSuiteModel({ name : "foo", projectId: 1});
                expect(testSuite.isValid()).toBe(false);
            });            


        });

    });
