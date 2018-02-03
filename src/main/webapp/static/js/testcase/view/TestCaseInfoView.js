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
        'testcase/view/TestCaseView',
        'text!testcase/tpl/testcase-info.html',
        'base/module/CommonModule',
        'base/module/Util',
        'i18next',
        'select2'

    ],
    function($, _, BaseModel, BaseView, BaseCollection, TestCaseModel, TestCaseView, TestCaseInfoTpl, CommonModule, Util, i18next) {
        var View = BaseView.extend({

            events: {
                "click #create_testcase": "createTestCase",
                "click .edit_testcase": "openTestCase",
                "click #schedule_testcase": "scheduleTestCase",
                "click #copy_testcase": "copyTestCase",
                "change #projectId": "loadTestCases"
            },

            initialize: function(options) {
                console.log('TestCaseInfoView.initialize : starts, options = ', options);
                this.projects = options.projects;
                this.viewModel = new BaseModel({
                    projects: CommonModule.getProjectArray(this.projects),
                    projectId: -1
                });
                this.render();

            },

            render: function() {
                this.$el.html(_.template(TestCaseInfoTpl)({
                    i18next: i18next
                }));
                var self = this;
                setTimeout(function() {
                    self.testcases = $('#testcases').dataTable({
                        bServerSide: false,
                        aaData: [],
                        aoColumns: [{
                            "mDataProp": "id",
                            "mRender": self.renderCheckbox,
                            "sTitle": '',
                            "bVisible": true,
                            "bSortable": false,
                            "sWidth": "40"
                        }, {
                            "mDataProp": "id",
                            "sTitle": i18next.t('app.id'),
                            "bVisible": true,
                            "bSortable": false,
                            "sWidth": "40"
                        }, {
                            "mDataProp": "name",
                            "mRender": self.renderTestCaseName,
                            "sTitle": i18next.t('app.testcase.name'),
                            "bVisible": true,
                            "bSortable": true,
                            "sWidth": "400"
                        }, {
                            "mData": "description",
                            "sTitle": i18next.t('app.description'),
                            "bVisible": true,
                            "bSortable": true,
                            "sWidth": "500",
                            "sDefaultContent": "",
                        }]
                    });
                    //temporary update projectId to trigger loading of testcase to avoid selecting it in ui.ONLY FOR TESTING
                    //self.viewModel.set('projectId', 1);
                    //self.loadTestCases();
                    self.$('#projectId').select2({
                        width: '150px'
                    });
                    self.$('.backgrid-container .backgrid').addClass('table table-striped table-bordered');
                }, 100);

                return this;
            },

            loadTestCases: function() {
                var projectId = this.viewModel.get('projectId');
                if (Util.isNull(projectId) || projectId == '-1') {
                    console.log('TestCaseInfoView.loadTestCases : NOT fetching testcases for projectId = ' + projectId);
                    return;
                }
                console.log('TestCaseInfoView.loadTestCases : fetching testcases for projectId = ' + projectId);
                CommonModule.showLoader();
                var self = this;
                require(['testcase/collection/TestCaseCollection'], function(TestCaseCollection) {
                    var testCaseCollection = new TestCaseCollection({
                        projectId: projectId
                    });
                    testCaseCollection.fetch({
                        success: function(collection, response) {
                            console.log('TestCaseInfoView.loadTestCases : after successfull fetch of testcases for projectId = %o, response = %o', projectId, response);
                            self.testcases.fnClearTable();
                            self.testcases.fnAddData(collection.toJSON());
                            CommonModule.hideLoader();
                            //open testcase, to speed up development, only for testing
                            $('#28').click();                            
                        }
                    });
                });
            },

            renderTestCaseName: function(data, type, full) {
                return '<a class="edit_testcase link" id=' + full.id + '>' + full.name + '</a>';
            },

            renderCheckbox: function(data, type, full) {
                return '<input type="checkbox" name="testcases_id" class="ck_testcase" value="' + full.id + '"/>';
            },

            createTestCase: function(model) {
                //checl typeof also, in case of no input model is of type Event
                //this function always receive not null model object.
                if(Util.isNull(model) || typeof model != 'object' || Util.isNull(model.cid)){
                    model = new TestCaseModel();
                }

                require(['project/collection/ProjectCollection'], function(ProjectCollection) {
                    var projectCollection = new ProjectCollection();
                    CommonModule.showLoader();
                    var fetchCommands = $.ajax({
                        url: "commandlist.service"
                    });
                    $.when(projectCollection.fetch(), fetchCommands).done(function(projectsResponse, commandsResponse) {
                        console.log('TestCaseInfoView.createTestCase : after : testcase fetching and projects fetching, response = ', projectsResponse, commandsResponse);
                        CommonModule.hideLoader();
                        var testCaseView = new TestCaseView({
                            model: model,
                            projects: projectCollection,
                            commands: $.parseJSON(commandsResponse[0]).commands
                        });
                        $('.global-popup').html(testCaseView.$el);
                        $('#showTestCase').modal('show');
                    });
                    /*projectCollection.fetch({
                        success: function(response) {
                            CommonModule.hideLoader();
                            console.log('TestCaseInfoView.createTestCase : successfull : after fetching project collection , response = ', response);
                            var testCaseView = new TestCaseView({
                                model: model,
                                projects: projectCollection
                            });
                            $('.global-popup').html(testCaseView.$el);
                            $('#showTestCase').modal('show');
                        },
                        error: function(response) {
                            CommonModule.hideLoader();
                            console.log('TestCaseInfoView.createTestCase : error : after fetching project collection , response = ', response);
                            alert(response);
                        }
                    });*/

                });

            },

            openTestCase: function(event) {
                var testCaseId = $(event.target).attr('id');
                var testCase = new TestCaseModel({
                    id: testCaseId
                });
                require(['project/collection/ProjectCollection'], function(ProjectCollection) {
                    var projectCollection = new ProjectCollection();
                    var fetchCommands = $.ajax({
                        url: "commandlist.service"
                    });
                    $.when(testCase.fetch(), projectCollection.fetch(), fetchCommands).done(function(testCaseResponse, projectsResponse, commandsResponse) {
                        console.log('TestCaseInfoView.openTestCase : after : testcase fetching and projects fetching, response = ', testCaseResponse, projectsResponse, commandsResponse);
                        var testCaseView = new TestCaseView({
                            model: testCase,
                            projects: projectCollection,
                            commands: $.parseJSON(commandsResponse[0]).commands
                        });
                        $('.global-popup').html(testCaseView.$el);
                        $('#showTestCase').modal('show');
                    });
                });
            },

            scheduleTestCase: function() {
                var ids = [];
                $('input[name="testcases_id"]:checked').each(function() {
                    ids.push($(this).val());
                });
                if (ids.length === 0) {
                    console.log('TestCaseInfoView.scheduleTestCase : no testcase is selected.');
                    return;
                }
                var self = this;
                require(['project/collection/ProjectCollection',
                    'schedule/model/ScheduleModel',
                    'schedule/view/ScheduleView'
                ], function(ProjectCollection, ScheduleModel, ScheduleView) {
                    var projectCollection = new ProjectCollection();
                    CommonModule.showLoader();
                    projectCollection.fetch({
                        success: function(response) {
                            CommonModule.hideLoader();
                            var environments = _.filter(projectCollection.toJSON(), function(project) {
                                if (project.id == self.viewModel.get('projectId')) {
                                    return project.environments;
                                }
                            })[0].environments;

                            console.log('TestCaseInfoView.scheduleTestCase : successfull : after fetching project collection , response = ', response);
                            var scheduleModel = new ScheduleModel({
                                testCases: ids
                            });
                            var scheduleView = new ScheduleView({
                                model: scheduleModel,
                                environments: environments
                            });
                            $('.global-popup').html(scheduleView.$el);
                            $('#scheduleTestCase').modal('show');
                        },
                        error: function(response) {
                            CommonModule.hideLoader();
                            console.log('TestCaseInfoView.scheduleTestCase : error : after fetching project collection , response = ', response);
                            alert(response);
                        }
                    });

                });
                console.log(ids);
            },

            copyTestCase: function(){
                var ids = [];
                $('input[name="testcases_id"]:checked').each(function() {
                    ids.push($(this).val());
                });
                if (ids.length === 0) {
                    console.log('TestCaseInfoView.copyTestCase : no testcase is selected.');
                    return;
                }
                if (ids.length > 1) {
                    console.log('TestCaseInfoView.copyTestCase : more than one testcase is selected.');
                    return;
                }                

                var model = new TestCaseModel({id : ids[0]});
                var self = this;
                model.fetch({
                    success: function(model, response){
                        console.log('TestCaseInfoView.copyTestCase : success fetch testcase = ' , ids[0]);
                        model.set({
                            id: null,
                            name: i18next.t('app.testcase.copy_of') + ' ' + model.get('name')
                        });
                        self.createTestCase(model);
                    },
                    error: function(response){
                        console.log('TestCaseInfoView.copyTestCase : error fetch testcase = ' , ids[0]);
                    }
                });

            }

        });
        return View;
    });
