/**
 * This is BaseCollections from which all collections in this application derived.
 */
define(["underscore",
        "backbone",
        "LocalStorage"
    ],
    function(_, Backbone, LocalStorage) {

        var BaseCollection = Backbone.Collection.extend({

            setLocalStorage: function() {
                LocalStorage.set(this.localStorageKey, this.toJSON());
            },

            getLocalStorage: function() {
                //empty collection
                this.reset();
                //initialize collection from local storage json
                this.reset(LocalStorage.get(this.localStorageKey));
                return LocalStorage.get(this.localStorageKey);
            },

            isValid: function(){
                if(this.models !== null && this.models.length > 0){
                    for(var i = 0; i < this.models.length; i++){
                        var model = this.models[i];

                        if(model.isValid() === false){
                            this.validationError = model.validationError;
                            this.validationError.index = i;
                            return false;
                        }
                    }
                }

                return true;
            }

        });

        BaseCollection.extend = Backbone.Collection.extend;

        return BaseCollection;

    });
