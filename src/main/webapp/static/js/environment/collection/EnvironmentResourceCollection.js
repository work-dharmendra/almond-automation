define(['backbone',
        'underscore',
        'base/collection/BaseCollection',
        'environment/model/EnvironmentResourceModel'
    ],
    function(Backbone, _, BaseCollection, EnvironmentResourceModel) {

        var Collection = BaseCollection.extend({

            model: EnvironmentResourceModel

        });

        return Collection;
    });
