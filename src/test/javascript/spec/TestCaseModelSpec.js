/**
 * This spec validate validation of command.
 */
define([
        "jquery",
        "backbone",
        "underscore",
        "base/module/CommonModule",
        "base/module/Constants",
        "testcase/model/CommandModel",
        "testcase/model/TestCaseModel",
        "testcase/collection/CommandCollection",
        "Router",
        "i18next"
    ],
    function($, Backbone, _, CommonModule, Constants, CommandModel, TestCaseModel, CommandCollection, Router, i18next) {

        beforeEach(function() {});

        describe("TestCaseModel validations", function() {

            it("should return error when name is empty", function() {
                var testcase = new TestCaseModel();
                expect(testcase.isValid()).toBe(false);
            });

            it("should return error when projectId is null or projectId is 0", function() {
                var testcase = new TestCaseModel({ name : "test" });
                expect(testcase.isValid()).toBe(false);
            });

            it("should return error when any command is invalid", function() {
                var testcase = new TestCaseModel({name : "test"});
                testcase.set("commands", [{ name : "wait"}]);
                expect(testcase.isValid()).toBe(false);
            });

        });

    });
