define([
        "backbone",
        "epoxy",
        "base/models/BaseModel",
        "testcase/collection/CommandCollection",
        'base/module/Util'
    ],
    function(Backbone, Epoxy, BaseModel, CommandCollection, Util) {
        var TestCaseModel = BaseModel.extend({

            defaults: function() {
                return {
                    id: null,
                    projectId: null,
                    name: null,
                    description: null,
                    isNeedResource: null,
                    commands: []
                };
            },

            /*initialize: function(){
                if(Util.isNull(this.get('commands'))){
                    commands = new CommandCollection();
                } else {
                    this.set("commands", new CommandCollection(this.get('commands')));
                }
            },*/

            url: function(){
                var url;
                if(Util.isNull(this.get('id'))){
                    url = 'project/{projectId}/testcase'.replace('{projectId}', this.get('projectId'));
                } else {
                    url = 'testcase/{id}'.replace('{id}', this.get('id'));
                }
                return this.getServiceUrl(url);
            },

            validate: function(){
                var errors = [];
                if (Util.isNull(this.get("name"))) {
                    errors.push({ error: "testcase_required_command_name", params: { } });
                    return errors;
                }

                if (Util.isNull(this.get("projectId")) || this.get("projectId") < 1 || this.get("projectId") === "0") {
                    errors.push({ error: "testcase_required_project", params: { } });
                    return errors;
                }

                if(Util.isNotNull(this.get("commands")) ){
                    var commandCollection = new CommandCollection(this.get("commands"));
                    if(commandCollection.isValid() === false){
                        return commandCollection.validationError;
                    }    
                }
            }

            /*parse: function(response){
                if(response.commands){
                    response.commands = new CommandCollection(response.commands);
                }

                return response;
            }*/

        });

        return TestCaseModel;
    });
