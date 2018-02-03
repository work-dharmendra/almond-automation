define(['backbone',
        'underscore',
        'base/collection/BaseCollection',
        'schedule/model/ScheduleModel'
    ],
    function(Backbone, _, BaseCollection, ScheduleModel) {

        var ScheduleCollection = BaseCollection.extend({

            model: ScheduleModel,

        });

        return ScheduleCollection;
    });
