define([
        "backbone",
        "epoxy",
        "base/models/BaseModel",
        "schedule/collection/TestCaseExecutionCollection",
        'base/module/Util'
    ],
    function(Backbone, Epoxy, BaseModel, TestCaseExecutionCollection, Util) {
        var ScheduleModel = BaseModel.extend({

            defaults: function() {
                return {
                    id: null,
                    scheduleId: null,
                    projectId: null,
                    environmentId: null,
                    testCasesIds: null,
                    testCaseSuiteId: null,//could be testcase or testsuite id
                    name: null,//could be testcase or testsuite name
                    runType: null,
                    execution: new TestCaseExecutionCollection(),
                    testCases: null,
                    testSuites: null,
                    gridUrl: null,
                    grid: null,//TODO
                    parameters: null
                };
            },

            url: function(){
                if(Util.isNull(this.get('id'))){
                    return 'schedule.service';
                } else {
                    return 'schedule/' + this.get('id') + '.service';
                }
            }

        });

        return ScheduleModel;
    });
