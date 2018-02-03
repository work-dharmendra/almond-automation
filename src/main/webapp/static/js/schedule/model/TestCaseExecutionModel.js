define([
        "backbone",
        "epoxy",
        "base/models/BaseModel",
        "base/module/CommonModule",
        'base/module/Util'
    ],
    function(Backbone, Epoxy, BaseModel, CommonModule, Util) {
        var TestCaseExecutionModel = BaseModel.extend({

            defaults: function() {
                return {
                    id: null,
                    command_name: null,
                    command_element: null,
                    command_value: null,
                    command_params: null,
                    comment: null,
                    status: null,
                    timeTaken: null,
                    screenshot: null
                };
            },

            initialize: function(options){
                console.log('TestCaseExecutionModel.initialize : options = ', options);
                /*var steps = [];
                _.each(this.get('status'), function(item) {
                    var step = $.parseJSON(item);
                    if (step.command) {
                        steps.push({
                            command_name: step.command.name,
                            command_value: step.command.value,
                            command_element: step.command.element,
                            command_params: step.command.params,
                            comment: CommonModule.getLocalizedComment(step.comment),
                            status: step.status,
                            timeTaken: step.timeTaken,
                            screenshot: step.screenshot
                        });
                    } else if(step.status === 'EXCEPTION'){
                        steps.push({
                           comment: step.comment ? step.comment : step.stackTrace,
                           status: 'EXCEPTION'  
                        });
                    }
                });
                this.set('steps', steps);*/
            }
            
        });

        return TestCaseExecutionModel;
    });
