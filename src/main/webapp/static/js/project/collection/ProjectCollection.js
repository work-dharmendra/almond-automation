define(['backbone',
        'underscore',
        'base/collection/BaseCollection',
        'project/model/ProjectModel'
    ],
    function(Backbone, _, BaseCollection, ProjectModel) {

        var ProjectCollection = BaseCollection.extend({

            model: ProjectModel,
            
            initialize: function(options) {
                
            },

            url: 'project.service',


        });

        return ProjectCollection;
    });
