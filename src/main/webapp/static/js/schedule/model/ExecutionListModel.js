define([
        "backbone",
        "epoxy",
        "base/models/BaseModel",
        "base/module/CommonModule",
        'base/module/Util'
    ],
    function(Backbone, Epoxy, BaseModel, CommonModule, Util) {
        var ExecutionListModel = BaseModel.extend({

            defaults: function() {
                return {
                    id: null,
                    name: null,
                    status: null,
                    testCaseId: null,
                    testCaseName: null,
                    testSuiteId: null,
                    testSuiteName: null,
                    whenCreated: null,
                    whenComplete: null,
                    whenModified: null
                };
            },

            /*initialize: function(options){
                
            }*/
            
        });

        return ExecutionListModel;
    });
