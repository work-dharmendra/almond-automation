define(['backbone',
        'underscore',
        'base/collection/BaseCollection',
        'testcase/model/TestCaseModel'
    ],
    function(Backbone, _, BaseCollection, TestCaseModel) {

        var TestCaseCollection = BaseCollection.extend({

            model: TestCaseModel,
            
            url: function(){
                return 'project/' + this.projectId + '/testcase.service';
            },

            initialize: function(options) {
                this.projectId = options.projectId;
            },

            parse: function(response){
                return response.list;
            }

        });

        return TestCaseCollection;
    });
