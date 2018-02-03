define([
        "jquery",
        "underscore",
        "base/view/BaseView",
        "schedule/model/TestCaseExecutionModel",
        "text!status/tpl/status-item-view.html",
        "text!status/tpl/status-screenshot.html",
        "text!status/tpl/status-item.html",
        "base/module/CommonModule",
        "base/module/Util",
        "i18next",
    ],
    function($, _, BaseView, TestCaseExecutionModel, StatusItemViewTpl, StatusScreenshotTpl, StatusItemTpl, CommonModule, Util, i18next) {
        var TestCaseStatusItemView = BaseView.extend({

            model: TestCaseExecutionModel,

            initialize: function(options) {
                console.log("TestCaseStatusItemView.initialize : starts, options = ", options);
                if (Util.isNull(options) || Util.isNull(this.model.get('status'))) {
                    return;
                }
                this.render();

                setTimeout(function() {
                    _.each(self.model.get('steps'), function(item) {
                        var template = _.template(StatusItemTpl)({
                            screenshot: item.screenshot,
                            command_name: item.command_name,
                            command_element: item.command_element,
                            command_value: item.command_value,
                            command_params: item.command_params,
                            timeTaken: item.timeTaken,
                            status: item.status,
                            comment: item.comment,
                            i18next: i18next
                        });
                        
                        self.$('.schedule_status').append(template);
                        /*//if (Util.isNotNull(item.screenshot)) {
                        if (true) {
                            screenshots.push({
                                command_name: item.command_name,
                                command_element: item.command_element,
                                command_value: item.command_value,
                                command_params: item.command_params,
                                comment: item.comment,
                                status: item.status,
                                screenshot: item.screenshot
                            });
                        }*/

                    });
                }, 200);
                var self = this;
                /*setTimeout(function() {
                    self.$('.schedule_status').dataTable({
                        bServerSide: false,
                        bPaginate: false,
                        bSort: false,
                        aaData: self.model.get('steps'),
                        "autoWidth": false,
                        aoColumns: [{
                            "mRender": self.renderCommandName,
                            "mDataProp": "command_name",
                            "sTitle":  i18next.t('app.testcase.command_name'),
                            "bVisible": true,
                            "bSortable": false,
                            "sDefaultContent": "",
                            "sWidth": "15%"
                        }, {
                            "mDataProp": "command_element",
                            "sTitle": i18next.t('app.testcase.command_element'),
                            "bVisible": true,
                            "bSortable": false,
                            "sDefaultContent": "",
                            "sWidth": "10%"
                        }, {
                            "mDataProp": "command_value",
                            "sTitle": i18next.t('app.testcase.command_value'),
                            "bVisible": true,
                            "bSortable": false,
                            "sDefaultContent": "",
                            "sWidth": "20%"
                        }, {
                            "mDataProp": "command_params",
                            "sTitle": i18next.t('app.testcase.command_param'),
                            "bVisible": true,
                            "bSortable": false,
                            "sDefaultContent": "",
                            "sWidth": "20%"
                        }, {
                            "mDataProp": "comment",
                            "sTitle": i18next.t('app.comment'),
                            "bVisible": true,
                            "bSortable": false,
                            "sDefaultContent": "",
                            "sWidth": "18%"
                        }, {
                            "mDataProp": "timeTaken",
                            "sTitle": i18next.t('app.timeTaken'),
                            "bVisible": true,
                            "bSortable": false,
                            "sDefaultContent": "",
                            "sWidth": "5%"
                        }, {
                            "mRender": self.renderStatus,
                            "mDataProp": "status",
                            "sTitle": i18next.t('app.status'),
                            "bVisible": true,
                            "bSortable": false,
                            "sDefaultContent": "",
                            "sWidth": "10%"
                        }]
                    });

                    var screenshots = [];

                    _.each(self.model.get('steps'), function(item) {
                        //if (Util.isNotNull(item.screenshot)) {
                        if (true) {
                            screenshots.push({
                                command_name: item.command_name,
                                command_element: item.command_element,
                                command_value: item.command_value,
                                command_params: item.command_params,
                                comment: item.comment,
                                status: item.status,
                                screenshot: item.screenshot
                            });
                        }

                    });

                    self.$('.schedule_screenshot').dataTable({
                        bServerSide: false,
                        bPaginate: false,
                        bSort: false,
                        aaData: screenshots,
                        bSearch: false,
                        aoColumns: [{
                            "mDataProp": "screenshot",
                            "sTitle": i18next.t('app.screenshot'),
                            "bVisible": true,
                            "bSortable": false,
                            "sDefaultContent": "",
                            "sWidth": "600",
                            "mRender": self.renderScreenshot
                        }]
                    });

                }, 200);*/

            },

            renderCommandName: function(data, type, full) {
                return '<span class="status_command_name">' + data + " </span>";
            },

            renderScreenshot: function(data, type, full) {
                var status = '';
                if(full.status === 'PASS'){
                    status = '<span class="label label-success">' + i18next.t('app.pass') + '</span>';
                } else if(full.status === 'FAIL'){
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
                if(data === 'PASS'){
                    return '<span class="label label-success">' + i18next.t('app.pass') + '</span>';
                } else if(data === 'FAIL'){
                    return '<span class="label label-danger">' + i18next.t('app.fail') + '</span>';
                } else {
                    return '<span class="label">' + data + '</span>';
                }

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

            getTotalTime: function(steps) {
                var total = 0;

                _.each(steps, function(step) {
                    if (step.timeTaken) {
                        total = parseInt(total) + parseInt(step.timeTaken);
                    }

                });
                console.log('total time ' + total);
                return Util.convertMilliSec(total);
            }

        });
        return TestCaseStatusItemView;
    });
