define([
        "jquery",
        "backbone",
        "underscore",
        "base/module/CommonModule",
        "base/module/Constants",
        "environment/model/EnvironmentModel"
    ],
    function($, Backbone, _, CommonModule, Constants, EnvironmentModel) {

        beforeEach(function() {});

        describe("EnvironmentModel validations", function() {

            it("should return error when name is empty", function() {
                var environment = new EnvironmentModel();
                expect(environment.isValid()).toBe(false);
            });

            it("should return error when projectId is empty", function() {
                var environment = new EnvironmentModel({ name : "foo"});
                expect(environment.isValid()).toBe(false);
            });

            it("should return error when any variables contains empty object", function() {
                var environment = new EnvironmentModel({ name : "foo", projectId : 1 });
                environment.set("variables", JSON.stringify([{}]));
                expect(environment.isValid()).toBe(false);
            });

            it("should return error when key and value is not present in variables object", function() {
                var environment = new EnvironmentModel({ name : "foo", projectId : 1 });
                environment.set("variables", JSON.stringify( [{ foo: "bar"}]) );
                expect(environment.isValid()).toBe(false);
            });

            it("should return error when key is not empty but value is empty in variables object", function() {
                var environment = new EnvironmentModel({ name : "foo", projectId : 1 });
                environment.set("variables", JSON.stringify([{ key: "bar"}]));
                expect(environment.isValid()).toBe(false);
            });

            it("should return error when key and value are not null but contains empty text", function() {
                var environment = new EnvironmentModel({ name : "foo", projectId : 1 });
                environment.set("variables", JSON.stringify([{ key: "", value: ""}]));
                expect(environment.isValid()).toBe(false);
            });

            it("should NOT return error when key and value in variables are not empty", function() {
                var environment = new EnvironmentModel({ name : "foo", projectId : 1 });
                environment.set("variables", JSON.stringify([{ key: "bar", value: "bar"}]));
                expect(environment.isValid()).toBe(true);
            });

        });

    });
