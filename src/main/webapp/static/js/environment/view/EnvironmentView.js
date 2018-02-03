/**
 * View to represent all project in paginated form and buttons to add/view/edit projects
 */
define([
        'jquery',
        'underscore',
        'base/models/BaseModel',
        'base/view/BaseView',
        'base/collection/BaseCollection',
        'environment/model/EnvironmentModel',
        'text!environment/tpl/environment.html',
        'base/module/Util',
        'base/module/CommonModule',
        'i18next',
        'backgrid', 'select2'
    ],
    function($, _, BaseModel, BaseView, BaseCollection, EnvironmentModel, EnvironmentTpl, Util, CommonModule, i18next) {
        var View = BaseView.extend({

            model: EnvironmentModel,

            events: {
                'click #saveEnvironment': 'saveEnvironment',
                'click #add_variable': 'addVariable'
            },

            computeds: {
                projects: {
                    get: function() {
                        var projects = [];
                        _.each(this.projects, function(item) {
                            projects.push({
                                label: item.name,
                                value: item.id
                            });
                        });
                        return projects;
                    }
                }
            },

            initialize: function(options) {
                console.log('EnvironmentView.initialize : starts, options = ', options);
                this.projects = options.projects.toJSON();
                this.viewModel = new BaseModel({
                    new_var_name: '',
                    new_var_value: ''
                });
                this.render();
            },

            render: function() {
                this.$el.html(_.template(EnvironmentTpl)({
                    heading: Util.isNotNull(this.model.get('id')) ? i18next.t('app.env.edit_env') : i18next.t('app.env.create_env'),
                    i18next: i18next
                }));
                var DeleteCell = Backgrid.Cell.extend({
                    template: _.template(" <span class='delete' /> "),
                    events: {
                        "click": "deleteRow"
                    },
                    deleteRow: function(e) {
                        e.preventDefault();
                        this.model.collection.remove(this.model);
                    },
                    render: function() {
                        this.$el.html(this.template());
                        this.delegateEvents();
                        return this;
                    }
                });
                var options = {
                    editable: true,
                    enableAddRow: true,
                    enableCellNavigation: true,
                    asyncEditorLoading: false,
                    autoEdit: true
                };
                var columns = [{
                    name: "key",
                    label: i18next.t('app.env.variable_name'),
                    cell: "string" // This is converted to "StringCell" and a corresponding class in the Backgrid package namespace is looked up
                }, {
                    name: "value",
                    label: i18next.t('app.env.variable_value'),
                    cell: "string"
                }, {
                    cell: DeleteCell,
                    editable: false
                }];
                var self = this;
                setTimeout(function() {
                    var collection = new BaseCollection($.parseJSON(self.model.get('variables')));
                    self.grid = new Backgrid.Grid({
                        columns: columns,
                        collection: collection
                    });
                    $("#variables").append(self.grid.render().el);
                    self.$('#projectId').select2();
                    self.$('.backgrid-container .backgrid').addClass('table table-striped table-bordered');
                }, 200);

                return this;
            },

            saveEnvironment: function() {
                console.log('EnvironmentView.saveEnvironment : saving environment ', this.model);
                this.resetMessage();
                var self = this;
                this.model.set('variables', JSON.stringify(this.grid.collection.toJSON()));
                if (this.model.isValid()) {
                    CommonModule.showLoader();
                    this.model.save(null, {
                        success: function(model, response) {
                            CommonModule.hideLoader();
                            console.log('EnvironmentView.saveEnvironment : save successfull, response ', response);
                            self.$('.sys-message').addClass('success').html(i18next.t('app.env.success_save_env'));
                        },

                        error: function(model, response) {
                            CommonModule.hideLoader();
                            console.log('EnvironmentView.saveEnvironment : save UNsuccessfull, response ', response);
                            self.$('.sys-message').addClass('error').html(i18next.t('app.env.error_save_env'));
                        }
                    });
                } else {
                    console.log("EnvironmentView.saveEnvironment : invalid model, error = ", this.model.validationError[0]);
                    self.$('.sys-message').addClass('error').html(CommonModule.getErrorMsg(this.model.validationError[0]));
                }
            },

            addVariable: function() {
                console.log(this.viewModel);
                this.grid.collection.add(new BaseModel({
                    key: this.viewModel.get('new_var_name'),
                    value: this.viewModel.get('new_var_value')
                }));
            }

        });
        return View;
    });
