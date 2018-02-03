define([
        "jquery",
        "underscore",
        "base/view/BaseView",
        "schedule/model/TestCaseExecutionModel",
        "schedule/model/ExecutionListModel",
        "text!status/tpl/status-item-view.html",
        "text!status/tpl/status-screenshot.html",
        "text!status/tpl/status-item.html",
        "base/module/CommonModule",
        "base/module/Util",
        "i18next",
    ],
    function($, _, BaseView, TestCaseExecutionModel, ExecutionListModel, StatusItemViewTpl, StatusScreenshotTpl, StatusItemTpl, CommonModule, Util, i18next) {
        var ExecutionListStatusView = BaseView.extend({

            //model: ExecutionListModel,

            initialize: function(options) {
                console.log("ExecutionListStatusView.initialize : starts, options = ", options);
                var self = this;
                
                if (Util.isNotNull(options) && Util.isNotNull(options.executionList)) {
                    this.executionList = options.executionList;
                    _.each(this.executionList, function(item) {
                        self.renderExecutionList(self.$el,item);
                    });
                }

                return this;

            },

            renderCommandName: function(data, type, full) {
                return '<span class="status_command_name">' + data + " </span>";
            },

            renderScreenshot: function(data, type, full) {
                var status = '';
                if (full.status === 'PASS') {
                    status = '<span class="label label-success">' + i18next.t('app.pass') + '</span>';
                } else if (full.status === 'FAIL') {
                    status = '<span class="label label-danger">' + i18next.t('app.fail') + '</span>';
                } else {
                    status = '<span class="label">' + data + '</span>';
                }
                var template = _.template(StatusScreenshotTpl)({
                    data: data,
                    command_name: full.command_name,
                    command_element: full.command_element,
                    command_value: full.command_value,
                    command_params: full.command_params,
                    status: status,
                    comment: full.comment,
                    i18next: i18next
                });
                //var img = '<img src="data:image/gif;base64,' + data + '" alt="Base64 encoded image" width="800" height="400"/>';
                return template;
            },

            renderStatus: function(data, type, full) {
                if (data === 'PASS') {
                    return '<span class="label label-success">' + i18next.t('app.pass') + '</span>';
                } else if (data === 'FAIL') {
                    return '<span class="label label-danger">' + i18next.t('app.fail') + '</span>';
                } else {
                    return '<span class="label">' + data + '</span>';
                }

            },

            renderExecutionList: function(parentEl, executionList) {
                console.log("rendering executionList", executionList, parentEl);
                var status = Util.isNotNull(executionList.status) ? executionList.status.toLowerCase() : "NA";
                
                var panelClass;
                if (status === 'fail' || status === 'exception') {
                    panelClass = 'panel-danger';
                } else if (status === 'pass') {
                    panelClass = 'panel-success';
                } else if (status === 'inprogress') {
                    panelClass = '';//TODO not sure which style to use for inprogress
                }

                var self = this;
                parentEl.append(_.template(StatusItemViewTpl)({
                    i18next: i18next,
                    testCaseName: Util.isNotNull(executionList.testCase) ? executionList.testCase.name: null,
                    testSuiteName: Util.isNotNull(executionList.testSuite) ? executionList.testSuite.name: null,
                    id: executionList.id,
                    status: i18next.t('app.' + status, {defaultValue: 'NA'}),
                    panelClass: panelClass,
                    totalTime: self.getTotalTime(executionList, 0)
                }));

                
                if(Util.isNotNull(executionList.executionListDTOList) && executionList.executionListDTOList.length > 0){
                    _.each(executionList.executionListDTOList, function(item){
                        self.renderExecutionList(self.$('#schedule_testcase_child_' + executionList.id), item);
                    });
                }

                setTimeout(function(){
                    _.each(executionList.testCaseExecutionDTOList, function(item) {
                        var command = {
                            name: "",
                            element: "",
                            value:"",
                            params:""
                        };
                        if(Util.isNotNull(item.command)){
                            command = $.parseJSON(item.command);
                        }
                        var template = _.template(StatusItemTpl)({
                            screenshot: item.screenshot,
                            command_name: command.name,
                            command_element: command.element,
                            command_value: command.value,
                            command_params: command.params,
                            timeTaken: item.timeTaken,
                            status: item.status,
                            comment: item.comment,
                            i18next: i18next
                        });
                        
                        self.$('#test_case_execution_' + executionList.id).append(template);
                    });
                }, 200);
            },

            
            getTestSuiteStatus: function(){

            },

            render: function() {
                var steps = this.model.get('steps');
                var testCaseStatus = this.model.get('status');
                //console.log('TestCaseStatusItemView.render : testCaseStatus = ', testCaseStatus);
                var status = 'unknown';
                var panelClass = '';
                if (Util.isNotNull(testCaseStatus) && testCaseStatus.length > 1) {
                    var step = $.parseJSON(testCaseStatus[testCaseStatus.length - 1]);
                    console.log('TestCaseStatusItemView.render : model = %o, step = %o', this.model, step);
                    console.log('TestCaseStatusItemView.render : step = %o, hasCommand = %o', step, step.hasOwnProperty('command'));
                    if (step.hasOwnProperty('command') === true) {
                        status = 'inprogress';
                    } else {
                        status = step.status.toLowerCase();
                    }
                } else {
                    if (Util.isNotNull(steps) && steps.length === 1) {
                        if (steps[0].status === 'EXCEPTION') {
                            status = "exception";
                        } else {
                            status = "inprogress";
                        }

                    } else {
                        status = "notstarted";
                    }
                }

                if (status === 'fail' || status === 'exception') {
                    panelClass = 'panel-danger';
                } else if (status === 'pass') {
                    panelClass = 'panel-success';
                }

                this.$el.html(_.template(StatusItemViewTpl)({
                    i18next: i18next,
                    testCaseName: this.model.get('name'),
                    cid: this.cid,
                    status: i18next.t('app.' + status),
                    panelClass: panelClass,
                    totalTime: this.getTotalTime(steps)
                }));

                return this;
            },

            getTotalTime: function(executionList, totalTime) {
                /*var self = this;

                if(Util.isNotNull(executionList.executionListDTOList) && executionList.executionListDTOList.length > 0){
                    _.each(executionList.executionListDTOList, function(item){
                        self.getTotalTime(item, totalTime);
                    });
                }

                _.each(executionList.testCaseExecutionDTOList, function(item) {
                    if (item.timeTaken) {
                        totalTime = parseInt(totalTime) + parseInt(item.timeTaken);
                    }
                });*/

                return Util.convertMilliSec(executionList.timeTaken);
            }

        });
        return ExecutionListStatusView;
    });
