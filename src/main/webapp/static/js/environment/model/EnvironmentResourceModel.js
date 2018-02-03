define([
        "backbone",
        "epoxy"
    ],
    function(Backbone, Epoxy) {
        var EnvironmentResourceModel = Backbone.Epoxy.Model.extend({

            defaults: function() {
                return {
                    id: null,
                    projectId: null,
                    environmentId: null,
                    variables: null
                };
            }

        });

        return EnvironmentModel;
    });
