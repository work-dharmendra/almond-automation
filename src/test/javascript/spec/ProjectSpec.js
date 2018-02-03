define([
        "jquery",
        "backbone",
        "underscore",
        "base/module/CommonModule",
        "base/module/Constants",
        "project/model/ProjectModel"
    ],
    function($, Backbone, _, CommonModule, Constants, ProjectModel) {

        beforeEach(function() {});

        describe("ProjectModel validations", function() {

            it("should return error when name is empty", function() {
                var project = new ProjectModel();
                expect(project.isValid()).toBe(false);
            });

        });

    });
