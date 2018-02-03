define([
        "backbone",
        "epoxy",
        "base/models/BaseModel",
        "testcase/collection/TestCaseCollection",
        'base/module/Util'
    ],
    function(Backbone, Epoxy, BaseModel, TestCaseCollection, Util) {
        var TestSuiteModel = BaseModel.extend({

            defaults: function() {
                return {
                    id: null,
                    projectId: null,
                    name: null,
                    description: null,
                    testCases: new TestCaseCollection([])
                };
            },

            url: function(){
                var url;
                if(Util.isNull(this.get('id'))){
                    url = 'project/{projectId}/testsuite'.replace('{projectId}', this.get('projectId'));
                } else {
                    url = 'testsuite/{id}'.replace('{id}', this.get('id'));
                }
                return this.getServiceUrl(url);
            },

            validate: function(){
                var errors = [];
                if (Util.isNull(this.get("name"))) {
                    errors.push({ error: "testsuite_required_name", params: { } });
                    return errors;
                }

                if (Util.isNull(this.get("projectId")) || this.get("projectId") < 1 || this.get("projectId") === "0") {
                    errors.push({ error: "testsuite_required_project", params: { } });
                    return errors;
                }

                if (Util.isNull(this.get("testCases")) || this.get("testCases").length < 1) {
                    errors.push({ error: "testsuite_required_testcases", params: { } });
                    return errors;
                }

            }

        });

        return TestSuiteModel;
    });
