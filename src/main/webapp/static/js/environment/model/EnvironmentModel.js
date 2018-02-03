define([
        "backbone",
        "epoxy",
        "base/models/BaseModel",
        'base/module/Util'
    ],
    function(Backbone, Epoxy, BaseModel, Util) {
        var EnvironmentModel = BaseModel.extend({

            defaults: function() {
                return {
                    id: null,
                    projectId: null,
                    projectName: null,
                    name: null,
                    description: null,
                    variables: null
                };
            },

            url: function() {
                var url;
                if (Util.isNull(this.get('id'))) {
                    url = 'project/{projectId}/environment'.replace('{projectId}', this.get('projectId'));
                } else {
                    url = 'environment/{environmentId}'.replace('{environmentId}', this.get('id'));
                }
                return this.getServiceUrl(url);
            },

            /**
             * It fetches schedule collection for given environmentId
             */
            fetchSchedules: function(options) {
                $.ajax({
                    url: 'environment/' + this.get('id') + '/schedule.service',
                    type: "GET",
                    dataType: "json",
                    success: options.success,
                    error: options.error
                });
            },

            validate: function(){
                var errors = [];
                if(Util.isNull(this.get("name"))){
                    errors.push({ error: "environment_required_name", params: { } });
                    return errors;
                }

                if (Util.isNull(this.get("projectId")) || this.get("projectId") < 1 || this.get("projectId") === "0") {
                    errors.push({ error: "environment_required_project", params: { } });
                    return errors;
                }

                if(Util.isNotNull(this.get("variables"))){
                    var variables = $.parseJSON(this.get("variables"));
                    for(var i = 0; i < variables.length; i++){
                        var obj = variables[i];

                        if(Util.isNull(obj)){
                            errors.push({ error: "environment_invalid_variable", params: { } });
                            return errors;           
                        }
                        if(Util.isEmpty(obj.key)){
                            errors.push({ error: "environment_variable_required_key", params: { } });
                            return errors;              
                        }
                        if(Util.isEmpty(obj.value)){
                            errors.push({ error: "environment_variable_required_value", params: { } });
                            return errors;              
                        }
                    }
                }
            }

        });

        return EnvironmentModel;
    });
