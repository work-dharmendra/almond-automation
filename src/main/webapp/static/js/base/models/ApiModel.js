define([
        "backbone",
        "epoxy",
        "LocalStorage",
        "base/module/Util",
    ],
    function(Backbone, Epoxy, LocalStorage, Util) {
        var ApiModel = Backbone.Epoxy.Model.extend({

            localStorageKey: "api",
            defaults: function() {
                return {
                    user_id: null,
                    access_token: null,
                    expire: null,
                    refresh_token: null,
                    url: null
                };
            },

            setLocalStorage: function() {
                LocalStorage.set(this.localStorageKey, this.attributes);
            },

            /**
             * This method initialize apimodel from values in localstorage specified by key {localStorageKey}
             * @return {[type]} [description]
             */
            getLocalStorage: function() {
                if (Util.isNotNull(LocalStorage.get(this.localStorageKey))) {
                    this.attributes = LocalStorage.get(this.localStorageKey);
                }
                return LocalStorage.get(this.localStorageKey);
            },

            /**
             * This method check whether token is valid or not.
             * This method uses async call to validate token.
             * Use this method only when necessary.
             * Since this model is not derived from BaseModel, i am using jquery get ajax call.
             * @return {[boolean]} true if token is valid otherwise false
             */
            healthCheck: function() {
                console.log('ApiModel.healthCheck : checking token');
                var url = this.getApiUrl() + '?health_check=1&random=' + Math.random();
                var valid = false;
                $.ajax({
                    url: url,
                    dataType: 'json',
                    async: false,
                    success: function(response) {
                        console.log('ApiModel.healthCheck : success : response = ', response);
                        valid = true;
                    },

                    error: function(response){
                        console.log('ApiModel.healthCheck : error : response = ', response);
                        valid = false;
                    }
                });

                return valid;
            },

            getApiUrl: function(){
                var api = new ApiModel();
                api.getLocalStorage();
                return api.get("url");
            }

        });

        return ApiModel;
    });
