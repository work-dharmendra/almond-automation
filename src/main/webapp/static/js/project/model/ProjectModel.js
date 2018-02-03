define([
        "backbone",
        "epoxy",
        "base/models/BaseModel",
        "base/module/Util"
    ],
    function(Backbone, Epoxy, BaseModel, Util) {
        var ProjectModel = BaseModel.extend({

            defaults: function() {
                return {
                    id: null,
                    name: null,
                    description: null
                };
            },

            url: function(){
                var url;
                if(Util.isNotNull(this.get('id'))){
                    url = 'project/' + this.get('id');
                } else {
                    url = 'project';
                }
                return this.getServiceUrl(url);
            },
            
            validate: function(){
                var errors = [];
                if(Util.isNull(this.get("name"))){
                    errors.push({ error: "project_required_name", params: { } });
                    return errors;
                }
            }            

        });

        return ProjectModel;
    });
