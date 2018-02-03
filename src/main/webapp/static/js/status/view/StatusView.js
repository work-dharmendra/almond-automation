define([
        'jquery',
        'underscore',
        'base/models/BaseModel',
        'base/view/BaseView',
        'base/collection/BaseCollection',
        'environment/model/EnvironmentModel',
        'schedule/model/ScheduleModel',
        'schedule/collection/TestCaseExecutionCollection',
        'schedule/collection/ExecutionListCollection',
        'status/view/ExecutionListStatusView',
        'text!status/tpl/status.html',
        'base/module/Util',
        'base/module/CommonModule',
        'i18next',
        'select2'
    ],
    function($, _, BaseModel, BaseView, BaseCollection, EnvironmentModel, ScheduleModel, ExecutionListCollection,
        TestCaseExecutionCollection, ExecutionListStatusView, StatusTpl, Util, CommonModule, i18next) {
        var View = BaseView.extend({

            //ExecutionListItemView: ExecutionListItemView,

            events: {
                //'change #envId': 'loadSchedules',
                'change #scheduleId': 'loadScheduleStatus',
                'click #refresh': 'loadScheduleStatus'
            },

            initialize: function(options) {
                console.log('StatusView.initialize : starts, options = ', options);
                this.projects = options.projects;
                this.viewModel = new BaseModel({
                    scheduleId: -1,
                    schedules: [],
                    //executionStatus: new TestCaseExecutionCollection(),
                    countSuccess: '222',
                    countError: i18next.t('app.na'),
                    gridUrl: "",
                    totalTestCase: "",
                    totalPassTestCase: "",
                    totalFailTestCase: "",
                    totalInProgressCase: "",
                    totalNotStartedTestCase: "",
                    executionList: new ExecutionListCollection()
                });
                this.render();
            },

            populateEnvironments: function() {
                console.log('StatusView.populateEnvironments : starts');
                var $env = this.$('#envId');
                $env.empty();
                //add Select Environment option
                var $defaultOption = $("<option>", {
                    text: i18next.t('app.select_environment'),
                    value: -1
                });
                $defaultOption.appendTo($env);
                _.each(this.projects.toJSON(), function(item) {
                    var $optgroup = $("<optgroup>", {
                        label: item.name
                    });
                    $optgroup.appendTo($env);
                    _.each(item.environments, function(item) {
                        var $option = $("<option>", {
                            text: item.name,
                            value: item.id
                        });
                        $option.appendTo($optgroup);
                    });
                });
            },

            render: function() {
                this.$el.html(_.template(StatusTpl)({
                    i18next: i18next
                }));
                var self = this;
                setTimeout(function() {
                    self.populateEnvironments();
                    //TODO temporary hardcoding
                    self.$('#envId').select2({
                        width: '200px'
                    });
                    /*self.$('#scheduleId').select2({
                        width: '200px'
                    });*/
                    self.scheduleIdSelect2 = self.$('#scheduleId').select2({
                        width: '200px',
                        ajax: {
                            url: function() {
                                return 'environment/' + self.$('#envId').val() + '/schedule.service';
                            },
                            type: 'GET',
                            dataType: 'json',
                            delay: 250,
                            contentType: "application/json",
                            data: function(term, page) {
                                console.log(term);
                            },
                            cache: true,
                            processResults: function(data, page) {
                                var results = [];
                                var list;
                                if (Util.isNotNull(data.list)) {
                                    list = data.list;
                                } else {
                                    list = data;
                                }
                                _.each(list, function(item) {
                                    results.push({
                                        text: item.scheduleId,
                                        value: item.id + '',
                                        id: item.id
                                    });
                                });
                                self.viewModel.set('scheduleId', -1);
                                setTimeout(function() {
                                    self.applyBindings();
                                }, 200);
                                return {
                                    results: results,
                                    pagination: {
                                        more: false
                                    }
                                };
                            }
                        }
                    });
                    $("#scheduleId").on("change", function() {
                        self.viewModel.set('scheduleId', $(this).val());
                    });
                    //self.$('#envId').val('1');
                    //self.loadSchedules();
                    
                }, 200);
                return this;
            },

            loadSchedules: function() {
                var envId = this.$('#envId').val();
                console.log('StatusView.loadSchedules : starts for environmentId = ', envId);
                var self = this;
                if (Util.isNull(envId) || envId === -1) {
                    console.log('StatusView.loadSchedules : NOT load schedules for environmentId = ', envId);
                    return;
                }
                var environmentModel = new EnvironmentModel({
                    id: envId
                });
                console.log('StatusView.loadSchedules : load schedules for environmentId = ', envId);
                CommonModule.showLoader();
                environmentModel.fetchSchedules({
                    success: function(response) {
                        CommonModule.hideLoader();
                        console.log('StatusView.loadSchedules : successfull fetch schedules : response', response);
                        var schedules = [];
                        _.each(response.list, function(item) {
                            schedules.push({
                                label: item.scheduleId,
                                value: item.id
                            });
                        });
                        self.viewModel.set('schedules', schedules);

                        //self.viewModel.set('scheduleId', response.list[0].scheduleId);
                        self.viewModel.set('scheduleId', -1);
                        self.viewModel.set('countSuccess', '2');
                        self.viewModel.set('countError', '2');
                        //self.viewModel.get('executionStatus').reset();
                        self.applyBindings();

                        setTimeout(function(){
                            self.viewModel.set('countSuccess', '2');
                            self.viewModel.set('countError', '2');
                            $('#count_success').html('2');
                            self.viewModel.set('scheduleId', response.list[0].id);
                            self.loadScheduleStatus();
                        }, 3000);
                    },
                    error: function(response) {
                        CommonModule.hideLoader();
                        console.log('StatusView.loadSchedules : error in fetch schedules : response', response);
                    }
                });
            },

            loadScheduleStatus: function() {
                //this.applyBindings();
                var scheduleId = this.viewModel.get('scheduleId');
                var self = this;
                console.log('StatusView.loadScheduleStatus : starts for scheduleId ' + scheduleId);

                if (Util.isNull(scheduleId) || scheduleId === -1) {
                    console.log('StatusView.loadScheduleStatus : NOT load schedules status for scheduleId = ', scheduleId);
                    return;
                }

                var scheduleModel = new ScheduleModel({
                    id: scheduleId
                });
                CommonModule.showLoader();
                this.resetBeforeFetch();
                scheduleModel.fetch({
                    success: function(model, response) {
                        CommonModule.hideLoader();
                        console.log('StatusView.loadScheduleStatus : successfull fetch loadScheduleStatus, response = ', response);
                        var executionListStatusView = new ExecutionListStatusView({
                            executionList: response.executionList
                        });
                        console.log(executionListStatusView.$el);
                        self.$("#execution-list-status").html("");
                        self.$("#execution-list-status").append(executionListStatusView.$el);
                        self.viewModel.set('totalTestCase', response.totalTestCase);
                        self.viewModel.set('totalPassTestCase', response.totalPassTestCase);
                        self.viewModel.set('totalFailTestCase', response.totalFailTestCase);
                        self.viewModel.set('totalInProgressCase', response.totalInProgressCase);
                        self.viewModel.set('totalNotStartedTestCase', response.totalNotStartedTestCase);
                        self.applyBindings();
                        //self.viewModel.get('executionStatus').reset();
                        //self.viewModel.set('executionStatus', new TestCaseExecutionCollection(response.executionStatus));
                        /*console.log(response.executionList);
                        self.viewModel.get('executionList').reset(response.executionList);
                        //self.viewModel.set('executionList', new ExecutionListCollection(response.executionList));
                        self.viewModel.set('gridUrl', response.gridUrl);
                        self.applyBindings();*/
                    },

                    error: function(response) {
                        CommonModule.hideLoader();
                        console.log('StatusView.loadScheduleStatus : error in fetch loadScheduleStatus, response = ', response);
                    }
                });

            },

            resetBeforeFetch: function(){
                this.viewModel.set('gridUrl', "");
                this.applyBindings();
            }

        });
        return View;
    });
