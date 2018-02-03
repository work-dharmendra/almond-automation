define(['backbone',
        'underscore',
        'base/collection/BaseCollection',
        'schedule/model/ExecutionListModel'
    ],
    function(Backbone, _, BaseCollection, ExecutionListModel) {

        var ExecutionListCollection = BaseCollection.extend({

            model: ExecutionListModel,

        });

        return ExecutionListCollection;
    });
