define(['backbone',
        'underscore',
        'base/collection/BaseCollection',
        'testsuite/model/TestSuiteModel'
    ],
    function(Backbone, _, BaseCollection, TestSuiteModel) {

        var TestSuiteCollection = BaseCollection.extend({

            model: TestSuiteModel,
            
            url: function(){
                return 'project/' + this.projectId + '/testsuite.service';
            },

            initialize: function(options) {
                this.projectId = options.projectId;
            },

            parse: function(response){
                return response.list;
            }

        });

        return TestSuiteCollection;
    });
