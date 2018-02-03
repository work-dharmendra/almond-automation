define([
        "jquery",
        "underscore",
        "backbone",
        "epoxy",
        "base/module/Util",
        "i18next",
        "base/module/CommonModule",
        "jqueryUI"
    ],
    function($, _, Backbone, Epoxy, Util, i18next, CommonModule) {
        var BaseView = Backbone.Epoxy.View.extend({

            bindingHandlers: {
                classesCustom: {
                    set: function($element, opts) {
                        var keyClass = Object.keys(opts)[0];
                        var value = opts[keyClass];
                        //the value which needs to be matched with model attribute
                        if (opts.value == value) {
                            $element.addClass(keyClass);
                        } else {
                            $element.removeClass(keyClass);
                        }

                    }
                },

                /**
                 * This is custom handler to show values in array as comma separated list
                 */
                valueArray: {
                    set: function($element, opts) {
                        var value = opts.value;
                        var delimiter = opts.delimiter;
                        if (Util.isNotEmptyArray(value)) {
                            $element.val(value.join(delimiter));
                            return value.join(delimiter);
                        }
                        return "";
                    },

                    get: function($element, opts) {
                        var delimiter = opts.delimiter;
                        if ($element.val().trim().length === 0) {
                            return null;
                        }
                        return $element.val().split(delimiter);
                    }
                },

                /**
                 * This binding is modified version of existing collection binding.
                 * Instead of collection, this binding iterate through array and append
                 * each object into view.
                 * usage : data-bind="array:modelArray, arrayItemView: arrayItemView"
                 * //TODO this binding is not working, DO NOT USE IT
                 * @type {Object}
                 */
                array: {
                    init: function($element, array, context, bindings) {
                        this.itemView = bindings.arrayItemView ? this.view[bindings.arrayItemView] : this.view.arrayItemView;
                        this.v = {};
                    },
                    set: function($element, array) {
                        console.log('BaseView.bindingHandlers.array : $element = %o, value = %o', $element, array);
                        console.log(this.itemView);
                        var self = this;
                        _.each(array, function(value){
                            var model = new Backbone.Epoxy.Model({name: 'nnn'});
                            var view = new self.itemView({model: model});
                            $element.append(view.$el);
                        });
                        /*var ArrayView = this.view.arrrayView;
                        var model = new Backbone.Epoxy.Model(value[0]);
                        var view = new BaseView({
                            model: model
                        });*/


                    }
                },

                /**
                 * This handler bind json property to element.
                 * It needs type(value,checked), name of json property in model and
                 * key inside json object.
                 * usage : data-bind="json:json, jsonKey: key1, jsonType: value"
                 * //TODO This binding is not tested thorougly
                 * //TODO need to update for nested json property
                 */
                json: {
                    init: function($element, json, context, bindings, delimiter ) {
                        this.type = bindings.jsonType;
                        this.key = bindings.jsonKey;
                        this.delimiter = bindings.delimiter;
                    },
                    set: function($element, json){
                        console.log('BaseView.bindingHandlers : json : opts = ', json);
                        if(this.type === 'value'){
                            $element.val(_.get(json, this.key));
                        }
                        if(this.type === 'array'){
                            if(Util.isNull(this.delimiter)){
                                $element.val(json[this.key]);
                            }else{
                                $element.val(json[this.key].join(this.delimiter));
                            }
                        }
                    },

                    get: function($element, json, event){
                        if(this.type === 'value'){
                            val = $element.val();
                            _.set(json, this.key, val);
                        }
                         if(this.type === 'array'){
                            val = $element.val();
                            if(Util.isNull(this.delimiter)){
                                json[this.key] = val.split(",");
                            }else{
                                json[this.key] = val.split(this.delimiter);
                            }
                        }
                        return json;
                    }
                },

                /**
                 * This is empty handler created for json handler, epoxy will throw if we don't provide this handler
                 * @return {[type]} [description]
                 */
                jsonKey: function(){

                },

                /**
                 * This is empty handler created for json handler, epoxy will throw if we don't provide this handler
                 * @return {[type]} [description]
                 */
                jsonType: function(){

                },

                /**
                 * This is empty handler created for json handler, epoxy will throw if we don't provide this handler
                 * @return {[type]} [description]
                 */
                delimiter: function(){

                }
            },

            resetMessage: function() {
                this.$('.sys-message').removeClass('success').removeClass('error').html('');
            },

        });
        BaseView.extend = Backbone.Epoxy.View.extend;

        return BaseView;
    });
