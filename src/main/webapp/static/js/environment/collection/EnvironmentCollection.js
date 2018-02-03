define(['backbone',
        'underscore',
        'base/collection/BaseCollection',
        'environment/model/EnvironmentModel'
    ],
    function(Backbone, _, BaseCollection, EnvironmentModel) {

        var EnvironmentCollection = BaseCollection.extend({

            model: EnvironmentModel,
            
            url: 'environment.service',

        });

        return EnvironmentCollection;
    });
