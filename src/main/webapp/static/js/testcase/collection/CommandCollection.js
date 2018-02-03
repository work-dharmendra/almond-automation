define([
        'base/collection/BaseCollection',
        'testcase/model/CommandModel',
    ],
    function(BaseCollection, CommandModel) {

        var CommandCollection = BaseCollection.extend({

            model: CommandModel

        });

        return CommandCollection;
    });
