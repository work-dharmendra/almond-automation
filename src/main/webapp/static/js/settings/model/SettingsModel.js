define([
        "backbone",
        "epoxy",
        "base/models/BaseModel",
        "base/module/Util"
    ],
    function(Backbone, Epoxy, BaseModel, Util) {
        var SettingsModel = BaseModel.extend({

            defaults: function() {
                return {
                    gridUrl: null,
                };
            },

            isNew: function(){
                return true;
            },

            url: function(){
                return this.getServiceUrl('settings');
            }
            
        });

        return SettingsModel;
    });
