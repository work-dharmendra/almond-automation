/**
 * This is BaseModel from which all models in this application derived.
 * Advantage of deriving from common model is that we can easily add methods which
 * is common to all models.
 */
define(["underscore",
        "backbone",
        "epoxy",
        "LocalStorage",
        "base/module/Util"
    ],
    function(_, Backbone, Epoxy, LocalStorage, Util) {

        var BaseModel = Backbone.Epoxy.Model.extend({

            /**
             * Override default save method of backbone so that we can remove attributes from model
             * which is not expected by api. It expect list of removable attributes in variable
             * transientList
             * @param  {[type]} attrs   original list of attributes defined in model
             */
            save: function(attrs, options) {
                console.log("BaseModel.save : attrs = %o, options = %o", attrs, options);
                if (typeof this.beforeSend === "function") {
                    this.beforeSend();
                }
                options = options || {};
                attrs = _.extend({}, _.clone(this.attributes), attrs);

                // Filter the data to send to the server
                if (this.transientList !== null) {
                    options.attrs = _.omit(attrs, this.transientList);
                }

                // Proxy the call to the original save function
                return Backbone.Model.prototype.save.call(this, attrs, options);
            },

            setLocalStorage: function() {
                LocalStorage.set(this.localStorageKey, this.attributes);
            },

            getLocalStorage: function() {
                if (Util.isNotNull(LocalStorage.get(this.localStorageKey))) {
                    this.attributes = LocalStorage.get(this.localStorageKey);
                }
                return LocalStorage.get(this.localStorageKey);
            },

            getServiceUrl: function(url){
                return url + '.service';
            }

        });

        BaseModel.extend = Backbone.Epoxy.Model.extend;

        return BaseModel;

    });
