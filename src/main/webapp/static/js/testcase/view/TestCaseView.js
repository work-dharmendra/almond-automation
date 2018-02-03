/**
 * View to represent all project in paginated form and buttons to add/view/edit projects
 */
define([
        'jquery',
        'underscore',
        'base/models/BaseModel',
        'base/view/BaseView',
        'base/collection/BaseCollection',
        'testcase/model/TestCaseModel',
        'text!testcase/tpl/testcase.html',
        'testcase/model/CommandModel',
        'testcase/collection/CommandCollection',
        'testcase/view/CommandItemView',
        'base/module/Util',
        'base/module/CommonModule',
        'i18next',
        'backgrid', 'select2', 'jquerySlimScroll'
    ],
    function ($, _, BaseModel, BaseView, BaseCollection, TestCaseModel, TestCaseTpl, CommandModel, CommandCollection, CommandItemView, Util, CommonModule, i18next) {
        var View = BaseView.extend({

            model: TestCaseModel,
            CommandItemView: CommandItemView,
            events: {
                'click #saveTestCase': 'saveTestCase',
                'click #add_command': 'addCommand',
                'change #projectId': 'loadEnvironments',
                'click #closeDebug': 'closeDebug',
                'click #expand': 'expand',
                'click .up': 'addCommandAbove',
                "click .delete": "delete"
            },

            initialize: function (options) {
                console.log('TestCaseView.initialize : starts, options = ', options);
                this.projects = options.projects;
                var self = this;
                this.model.on('change projectId', function () {
                    self.loadEnvironments();
                });
                this.height = $(window).height;
                this.viewModel = new BaseModel({
                    projects: CommonModule.getProjectArray(this.projects),
                    environments: CommonModule.getEnvironmentArrayFromProject(this.projects, -1),
                    new_command_name: 'select_command',
                    new_command_element: '',
                    new_command_value: '',
                    new_command_select_value: 'select_testcase',
                    new_command_param: '',
                    commands: CommonModule.getCommands(options.commands),
                    environmentId: -1,
                    custom_parameters: '',
                    commandCollection: new CommandCollection(this.model.get('commands'))
                });
                this.viewModel.on('change new_command_name', function () {
                    if (_.contains(['include', 'invoke'], self.viewModel.get('new_command_name'))) {
                        self.$('#newCommandText').hide();
                        self.$('#newCommandValue').siblings('.select2').css('display', 'block');
                    } else {
                        self.$('#newCommandText').show();
                        self.$('#newCommandValue').siblings('.select2').css('display', 'none');
                    }
                });
                this.commands = CommonModule.getCommands(options.commands);
                this.render();
                $(document).on('show.bs.modal', '.modal', function () {
                    var zIndex = 1040 + (10 * $('.modal:visible').length);
                    $(this).css('z-index', zIndex);
                    setTimeout(function () {
                        $('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
                    }, 0);
                });
            },

            render: function () {
                var self = this;
                this.$el.html(_.template(TestCaseTpl)({
                    heading: Util.isNotNull(this.model.get('id')) ? i18next.t('app.testcase.edit_testcase') : i18next.t('app.testcase.create_testcase'),
                    i18next: i18next
                }));
                setTimeout(function () {
                    var collection = new BaseCollection(self.model.get('commands'));
                    self.$('#projectId').select2({
                        width: '200px'
                    });
                    self.$('select').select2({
                        width: '200px'
                    });
                    self.$('#environmentId').select2({
                        width: '200px'
                    });
                    self.$('#newCommandName').select2();
                    var methodType;

                    if (Util.isNotNull(self.model.get('id'))) {
                        methodType = 'POST';
                    } else {
                        methodType = 'GET';
                    }
                    self.$('#newCommandValue').select2({
                        width: '200px',
                        ajax: {
                            url: function () {
                                if (Util.isNotNull(self.model.get('id'))) {
                                    return 'testcases/' + self.model.get('id') + '/include.service';
                                } else {
                                    return 'projects/' + self.model.get('projectId') + '/testcases.service';
                                }
                            },
                            type: methodType,
                            dataType: 'json',
                            delay: 250,
                            contentType: "application/json",
                            /*data: function (search, page) {

                                return
                                    if (Util.isNotNull(search) && Util.isNotNull(search.term)) {
                                        JSON.stringify({
                                            searchTerm: search.term
                                        });
                                    } else {
                                        "";
                                };
                            },*/
                            cache: false,
                            processResults: function (data, page) {
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
                    self.$('#newCommandValue').siblings('.select2').css('display', 'none');
                    this.$('select').select2({
                        width: '200px'
                    });
                }, 200);

                return this;
            },

            saveTestCase: function () {
                console.log('TestCaseView.saveTestCase : saving testcase ', this.model);
                var self = this;
                this.model.set('commands', this.viewModel.get("commandCollection").toJSON());
                this.resetMessage();
                if (this.model.isValid()) {
                    CommonModule.showLoader();
                    this.model.save(null, {
                        success: function (model, response) {
                            CommonModule.hideLoader();
                            console.log('TestCaseView.saveTestCase : save successfull, response ', response);
                            self.$('.sys-message').addClass('success').html(i18next.t('app.testcase.success_save_testcase'));
                        },

                        error: function (model, response) {
                            CommonModule.hideLoader();
                            console.log('TestCaseView.saveTestCase : save UNsuccessfull, response ', response);
                            self.$('.sys-message').addClass('error').html(i18next.t('app.testcase.error_save_testcase'));
                            var detailMessage = CommonModule.getDetailExceptionMsg(response.responseJSON);
                            if(Util.isNotNull(detailMessage)){
                                self.$('#exception-message').addClass('error').html(i18next.t('app.exceptionMessage', {EXCEPTION_MESSAGE: detailMessage}) );
                            }
                            
                        }
                    });
                } else {
                    console.log("TestCaseView.saveTestCase : invalid model, error = ", this.model.validationError[0]);
                    self.$('.sys-message').addClass('error').html(CommonModule.getErrorMsg(this.model.validationError[0]));
                }
            },

            resetMessage: function () {
                self.$('.sys-message').removeClass('success').removeClass('error').html('');
            },

            loadEnvironments: function () {
                var projectId = this.model.get('projectId');

                if (projectId === -1) {
                    console.log('TestCaseView.loadEnvironments : do not environments for project = ', projectId);
                }
                console.log('TestCaseView.loadEnvironments1 : loading environments for project = ', projectId);
                this.viewModel.set('environments', CommonModule.getEnvironmentArrayFromProject(this.projects, projectId));
            },

            addCommand: function () {
                var value;
                if (this.viewModel.get('new_command_name') === 'include' || this.viewModel.get('new_command_name') === 'invoke') {
                    value = this.viewModel.get('new_command_select_value');
                } else {
                    value = this.viewModel.get('new_command_value');
                }
                this.viewModel.get("commandCollection").add(new CommandModel({
                    name: this.viewModel.get('new_command_name'),
                    element: this.viewModel.get('new_command_element'),
                    value: value,
                    params: this.viewModel.get('new_command_param')
                }));

                this.viewModel.set({
                    new_command_name: 'select_command',
                    new_command_element: '',
                    new_command_value: '',
                    new_command_param: '',
                    new_command_select_value: 'select_testcase'
                });
                this.applyBindings();
                this.$('.testcase_commands, #newCommandName').select2();
                //this.$('#showTestCase').width('90%');
            },

            runCommand: function (model) {
                var environmentId = this.viewModel.get('environmentId');
                if (environmentId === -1) {
                    console.log('TestCaseView.runCommand : no environment selected');
                    return;
                }
                console.log('TestCaseView.runCommand : running command for environment = ', environmentId);
                var commands = [];
                var self = this;
                commands.push({
                    name: model.get('name'),
                    element: model.get('element'),
                    value: model.get('value'),
                    params: model.get('params')
                });
                var params = {};
                var custom_parameters = this.viewModel.get('custom_parameters').split(';');

                _.each(custom_parameters, function (item) {
                    var parameter = item.split('=');
                    var value = {};
                    params[parameter[0]] = parameter[1];
                });
                console.log(params);
                var data = {
                    uuid: this.viewModel.get('uuid'),
                    config: {
                        environment: {
                            id: environmentId
                        },
                        params: params
                    },
                    commands: commands
                };
                this.$('#sys-message').removeClass('success').removeClass('error').html('');
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: "POST",
                    url: 'debug.service',
                    data: JSON.stringify(data),
                    success: function (response) {
                        console.log('TestCaseView.runCommand : response = ', response);
                        self.viewModel.set('uuid', response.uuid);
                        //TODO error handling
                        var steps = $.parseJSON(response.steps);

                        console.log(steps.status);
                        if (steps.status === 'PASS') {
                            self.$('#sys-message').addClass('success').html(i18next.t('app.success_command'));
                        } else {
                            if (Util.isNull(steps.comment)) {
                                self.$('#sys-message').addClass('error').html(i18next.t('app.success_error'));
                            } else {
                                self.$('#sys-message').addClass('error').html(steps.comment);
                            }

                        }

                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        console.log('TestCaseView.runCommand : error = ', errorThrown);
                        self.$('#sys-message').addClass('error').html(errorThrown);
                    },
                    dataType: 'json'
                });
            },

            closeDebug: function () {
                //return;

                var uuid = this.viewModel.get('uuid');
                if (Util.isNull(uuid)) {
                    console.log('TestCaseView.closeDebug : no need to close debug session.');
                    return;
                }
                var self = this;
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: "POST",
                    url: 'closedebug.service',
                    data: uuid,
                    success: function (response) {
                        console.log('TestCaseView.closeDebug : response = ', response);
                        self.viewModel.set('uuid', null);
                        self.$('#sys-message').addClass('success').html(i18next.t('app.success_close_debug'));
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        console.log('TestCaseView.closeDebug : error = ', errorThrown);
                        self.$('#sys-message').addClass('error').html(errorThrown);
                    },
                    dataType: 'json'
                });

            },

            addCommandAbove: function (e) {
                console.log("addCommandAbove command starts");
                var cid = $(e.currentTarget).attr("cid");
                var clickedModel = this.viewModel.get("commandCollection").get(cid);
                var index = this.viewModel.get("commandCollection").indexOf(clickedModel);
                console.log(index);
                this.viewModel.get("commandCollection").add(new CommandModel({
                    name: "",
                    element: "",
                    value: "",
                    params: ""
                }), {
                    at: index
                });
                this.applyBindings();
                this.$(".testcase_commands").select2();
            },

            expand: function () {
                console.log('TestCaseView.expand starts');
                var height = this.getExapandSize().height;
                console.log('current height of window = ', height);
                var self = this;
                if (this.$('#expand').hasClass('up')) {
                    this.$('#testcase_metadata').hide();
                    this.$('#command_div').height(height * 0.8 + 'px');
                    this.$('.backgrid-container').height(height * 0.7 + 'px');
                    this.$('#expand').removeClass('up').addClass('down');
                    setTimeout(function () {
                        //self.$('.slimScrollDiv').height(height + 'px');
                        self.$('.backgrid-container').slimScroll({
                            height: height * 0.7 + 'px',
                            alwaysVisible: true,
                            size: '40px'
                        });
                    }, 300);
                } else {
                    this.$('#testcase_metadata').show();
                    this.$('#command_div').height(height * 0.7 + 'px');
                    this.$('.backgrid-container').height(height * 0.6 + 'px');
                    this.$('#expand').removeClass('down').addClass('up');

                    setTimeout(function () {
                        //self.$('.slimScrollDiv').height(height + 'px');
                        self.$('.backgrid-container').slimScroll({
                            height: height * 0.7 + 'px',
                            alwaysVisible: true,
                            size: '40px'
                        });
                    }, 300);
                }


            },

            getExapandSize: function () {
                var height = $(window).height();
                height = height;
                return {
                    height: height
                };
            },

            delete: function (e) {
                console.log("delete command starts");
                var cid = $(e.currentTarget).attr("cid");
                this.viewModel.get("commandCollection").remove(cid);
                this.applyBindings();
                this.$(".testcase_commands").select2();
            }

        });
        return View;
    });