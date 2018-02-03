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
        "testcase/collection/CommandCollection",
        "Router",
        "i18next"
    ],
    function($, Backbone, _, CommonModule, Constants, CommandModel, CommandCollection, Router, i18next) {

        beforeEach(function() {});

        describe("Command validations", function() {

            it("should return error when command name is empty", function() {
                var command = new CommandModel();
                expect(command.isValid()).toBe(false);
            });

            it("should return error when command element is empty for some commands", function() {

                _.each(Constants.nonEmptyElementCommands, function(val) {
                    var command = new CommandModel({
                        name: val
                    });
                    expect(command.isValid()).toBe(false);
                });

            });

            it("should return error when element is not empty, command value is empty for some commands", function() {

                _.each(Constants.nonEmptyValueCommands, function(val) {
                    var command = new CommandModel({
                        name: val,
                        element: "name=abc"
                    });
                    expect(command.isValid()).toBe(false);
                });

            });

            it("should return error when either element or value is empty for some commands", function() {

                _.each(Constants.nonEmptyElementValueCommands, function(val) {
                    var command = new CommandModel({
                        name: val,
                        element: "name=abc"
                    });
                    expect(command.isValid()).toBe(false);
                    command = new CommandModel({
                        name: val,
                        value: "value"
                    });
                    expect(command.isValid()).toBe(false);
                });

            });

            it("should return error when command element is not empty but invalid", function() {

                _.each(Constants.nonEmptyElementCommands, function(val) {
                    var command = new CommandModel({
                        name: "click",
                        element: "element"
                    });
                    expect(command.isValid()).toBe(false);
                });

            });

            it("should return error when command element is not empty, valid but incorrect selector type", function() {

                _.each(Constants.nonEmptyElementCommands, function(val) {
                    var command = new CommandModel({
                        name: "click",
                        element: "abc=123"
                    });
                    expect(command.isValid()).toBe(false);
                });

            });

        });

        describe("Wait Command validations", function() {

            it("should return error when command name is wait, value is non empty and has invalid number", function() {
                var command = new CommandModel({
                    name: "wait",
                    value: "abc"
                });
                expect(command.isValid()).toBe(false);

            });

            it("should return error when command name is wait, value is non empty and has negative number", function() {
                var command = new CommandModel({
                    name: "wait",
                    value: "-10"
                });
                expect(command.isValid()).toBe(false);
            });

            it("should NOT return error when command name is wait and value is positive number", function() {
                var command = new CommandModel({
                    name: "wait",
                    value: "10"
                });
                expect(command.isValid()).toBe(true);
            });
        });


        describe("dragAndDropByOffset Command validations", function() {

            it("should return error when command name is dragAndDropByOffset, value is non empty and is not in format x=20;y=20", function() {
                var command = new CommandModel({
                    name: "dragAndDropByOffset",
                    element: "name=abc",
                    value: "abc"
                });
                expect(command.isValid()).toBe(false);

            });

            it("should return error when command name is dragAndDropByOffset, value doesn't contains x", function() {
                var command = new CommandModel({
                    name: "dragAndDropByOffset",
                    element: "name=abc",
                    value: "y=2"
                });
                expect(command.isValid()).toBe(false);

            });

            it("should NOT return error when command name is dragAndDropByOffset, value is valid", function() {
                var command = new CommandModel({
                    name: "dragAndDropByOffset",
                    element: "name=abc",
                    value: "x=2;y=2"
                });
                expect(command.isValid()).toBe(false);

            });
        });

        describe("Select Command validations", function() {

            it("should return error when command name is select, value is non empty and has incorrect value", function() {
                var command = new CommandModel({
                    name: "select",
                    element: "name=abc",
                    value: "abc"
                });
                expect(command.isValid()).toBe(false);

            });

            it("should return error when command name is select and non positive number provided for select type index", function() {
                var command = new CommandModel({
                    name: "select",
                    element: "name=abc",
                    value: "index="
                });
                expect(command.isValid()).toBe(false);

            });

            it("should return error when command name is select and type is provided other than value, index, text", function() {
                var command = new CommandModel({
                    name: "select",
                    element: "name=abc",
                    value: "index1=0"
                });
                expect(command.isValid()).toBe(false);

            });

            it("should NOT return error when command name is select, value is valid", function() {

                _.each(["value", "text"], function(val) {
                    var command = new CommandModel({
                        name: "select",
                        element: "name=abc",
                        value: val + "=value"
                    });
                    expect(command.isValid()).toBe(true);
                });

                command = new CommandModel({
                    name: "select",
                    element: "name=abc",
                    value: "index=1"
                });
                expect(command.isValid()).toBe(true);
            });

        });

        describe("CommandCollection validations", function() {

            it("should return error when any command is invalid", function() {
                var command = new CommandModel();
                var collection = new CommandCollection();
                collection.add(command);
                expect(collection.isValid()).toBe(false);
            });

            it("should NOT return error when collection contains no command", function() {
                var collection = new CommandCollection();
                expect(collection.isValid()).toBe(true);
            });

            it("should NOT return error when contains valid commands", function() {
                var collection = new CommandCollection();
                collection.add({ name : "wait", value: "10"});
                collection.add({ name : "open", value: "url"});
                expect(collection.isValid()).toBe(true);
            });
        });

    });
