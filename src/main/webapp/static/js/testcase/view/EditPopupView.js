define([
        'jquery',
        'underscore',
        'base/models/BaseModel',
        'base/view/BaseView',
        'text!testcase/tpl/edit-popup.html',
        'base/module/Util',
        'base/module/CommonModule',
        'i18next',
        'select2'
    ],
    function ($, _, BaseModel, BaseView, EditPopTpl, Util, CommonModule, i18next) {
        var EditPopupView = BaseView.extend({

            events: {
                "click .close_popup": "closePopup",
                "click #save": "save"
            },

            computeds: {
                commandType: {
                    get: function () {
                        return this.isSelectBox() === true ? "noninclude" : "include";
                    }
                },

                showSelectBox: {
                    get: function () {
                        return this.isSelectBox() === true ? "hide" : "show";
                    }
                }
            },

            initialize: function (options) {
                console.log('EditPopupView.initialize : starts, options = ', this.model);
                this.edit = options.edit;
                this.render();
            },

            render: function () {
                this.$el.html(_.template(EditPopTpl)({
                    i18next: i18next
                }));
                this.viewModel = new BaseModel({
                    new_command_select_value: ""
                });
                var self = this;
                if ($.inArray(this.model.get('name'), ["include", "invoke"]) !== -1) {
                    if (Util.isNotNull(self.model.get('id'))) {
                        methodType = 'POST';
                    } else {
                        methodType = 'GET';
                    }
                    methodType = 'GET';
                    this.$('.selectTestCase').select2({
                        width: '200px',
                        ajax: {
                            url: function () {
                                return 'include.service?projectId=' + self.model.get('projectId');
                                /*if (Util.isNotNull(self.model.get('id'))) {
                                    return 'testcases/' + self.model.get('id') + '/include.service';
                                } else {
                                    return 'projects/' + self.model.get('projectId') + '/testcases.service';
                                }*/
                            },
                            type: methodType,
                            dataType: 'json',
                            delay: 250,
                            contentType: "application/json",
                            data: function (search, page) {
                                if (Util.isNotNull(search) && Util.isNotNull(search.term)) {
                                    return 'searchTerm=' + search.term;
                                } else {
                                    return "";
                                }
                            },
                            cache: false,
                            processResults: function (data, pageage) {
                                var results = [];
                                var list;
                                if (Util.isNotNull(data.list)) {
                                    list = data.list;
                                } else {
                                    list = data;
                                }
                                _.each(list, function (item) {
                                    results.push({
                                        text: item.name,
                                        value: item.id + '',
                                        id: item.id
                                    });
                                });
                                return {
                                    results: results,
                                    pagination: {
                                        more: false
                                    }
                                };
                            }
                        }
                    });
                }
                return this;
            },

            save: function () {
                $('#editElementPopup').hide();
                console.info(this.model.attributes);
                if ($.inArray(this.model.get('name'), ["include", "invoke"]) !== -1) {
                    this.model.set("testCaseName", this.$(".selectTestCase").text());
                }
                this.trigger("save", this.model.attributes);
            },

            closePopup: function () {
                $('#editElementPopup').hide();
            },

            isSelectBox: function () {
                return $.inArray(this.model.get('name'), ["include", "invoke"]) !== -1 && this.edit === "value" ? true : false;
            }


        });
        return EditPopupView;
    });