define(['backbone',
        'underscore',
        'base/collection/BaseCollection',
        'schedule/model/TestCaseExecutionModel'
    ],
    function(Backbone, _, BaseCollection, TestCaseExecutionModel) {

        var TestCaseExecutionCollection = BaseCollection.extend({

            model: TestCaseExecutionModel,

        });

        return TestCaseExecutionCollection;
    });
