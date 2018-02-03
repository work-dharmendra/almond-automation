define([
        'jquery',
        'underscore',
        'base/models/BaseModel',
        'base/view/BaseView',
        'base/collection/BaseCollection',
        'testcase/collection/TestCaseCollection',
        'testsuite/model/TestSuiteModel',
        'testsuite/view/TestSuiteView',
        'text!testsuite/tpl/testsuite-info.html',
        'base/module/CommonModule',
        'base/module/Util',
        'i18next',
        'select2'
    ],
    function($, _, BaseModel, BaseView, BaseCollection, TestCaseCollection, TestSuiteModel, TestSuiteView, TestSuiteInfoTpl, CommonModule, Util, i18next) {
        var View = BaseView.extend({

            events: {
                "click #create_testsuite": "createTestSuite",
                "click .edit_testsuite": "openTestSuite",
                "click #schedule_testsuite": "scheduleTestSuite",
                "change #projectId": "loadTestSuite"
            },

            initialize: function(options) {
                console.log('TestSuiteInfoView.initialize : starts, options = ', options);
                this.projects = options.projects;
                this.viewModel = new BaseModel({
                    projects: CommonModule.getProjectArray(this.projects),
                    projectId: -1
                });
                this.render();
            },

            render: function() {
                this.$el.html(_.template(TestSuiteInfoTpl)({
                    i18next: i18next
                }));
                var self = this;
                setTimeout(function() {
                    self.testsuites = $('#testsuites').dataTable({
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
                            "mDataProp": "name",
                            "mRender": self.renderTestSuiteName,
                            "sTitle": i18next.t('app.testsuite.testsuite'),
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
                    //self.loadTestSuite();
                    self.$('#projectId').select2({
                        width: '150px'
                    });
                }, 100);

                return this;
            },

            loadTestSuite: function() {
                var projectId = this.viewModel.get('projectId');
                if (Util.isNull(projectId) || projectId == '-1') {
                    console.log('TestSuiteInfoView.loadSuiteCases : NOT fetching testsuite for projectId = ' + projectId);
                    return;
                }
                console.log('TestSuiteInfoView.loadTestSuite : fetching testsuite for projectId = ' + projectId);
                CommonModule.showLoader();
                var self = this;
                require(['testsuite/collection/TestSuiteCollection'], function(TestSuiteCollection) {
                    var testSuiteCollection = new TestSuiteCollection({
                        projectId: projectId
                    });
                    testSuiteCollection.fetch({
                        success: function(collection, response) {
                            console.log('TestSuiteInfoView.loadTestSuite : after successfull fetch of testsuite for projectId = %o, response = %o', projectId, response);
                            self.testsuites.fnClearTable();
                            self.testsuites.fnAddData(collection.toJSON());
                            self.testSuiteJson = collection.toJSON();
                            CommonModule.hideLoader();
                        }
                    });
                });
            },

            renderTestSuiteName: function(data, type, full) {
                return '<a class="edit_testsuite link" id=' + full.id + '>' + full.name + '</a>';
            },

            renderCheckbox: function(data, type, full) {
                return '<input type="checkbox" name="testsuite_id" class="ck_testsuite" value="' + full.id + '"/>';
            },

            createTestSuite: function() {
                var model = new TestSuiteModel({
                    projectId: -1
                });
                var testSuiteView = new TestSuiteView({
                    model: model,
                    projects: this.projects
                });
                $('.global-popup').html(testSuiteView.$el);
                $('#showTestSuite').modal('show');
            },

            openTestSuite: function(event) {
                var testSuiteId = $(event.target).attr('id');

                var model = new TestSuiteModel({
                    id: testSuiteId
                });
                var self = this;
                var projectId = this.testSuiteJson.filter(function(item){
                    return item.id == testSuiteId;
                })[0].projectId;
                console.log('TestSuiteInfoView.openTestSuite : projectId = ', projectId);
                var testCaseCollection = new TestCaseCollection({
                    projectId: projectId
                });
                $.when(model.fetch(), testCaseCollection.fetch()).done(function(modelResponse, testCaseResponse){
                    CommonModule.hideLoader();
                    console.log('TestSuiteInfoView.openTestSuite : successfull : after fetching testsuite collection , modelResponse = %o, testCaseResponse = %o', modelResponse, testCaseResponse);
                    testCaseCollection.each(function(item){
                        var isChecked = _.findWhere(model.get('testCases'), {id: item.get('id')});
                        if(Util.isNotNull(isChecked)){
                            item.set('checked', true);
                        }
                        console.log(isChecked);
                    });
                    var testSuiteView = new TestSuiteView({
                        model: model,
                        projects: self.projects,
                        testCases: testCaseCollection
                    });
                    $('.global-popup').html(testSuiteView.$el);
                    $('#showTestSuite').modal('show');
                });
                
            },

            scheduleTestSuite: function() {
                var ids = [];
                $('input[name="testsuite_id"]:checked').each(function() {
                    ids.push($(this).val());
                });
                if (ids.length === 0) {
                    console.log('TestSuiteInfoView.scheduleTestSuite : no testsuite is selected.');
                    return;
                }
                console.log('TestSuiteInfoView.scheduleTestSuite : scheduling testsuites = ', ids);
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

                            console.log('TestSuiteInfoView.scheduleTestSuite : successfull : after fetching project collection , response = ', response);
                            var scheduleModel = new ScheduleModel({
                                testSuites: ids
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
                            console.log('TestSuiteInfoView.scheduleTestSuite : error : after fetching project collection , response = ', response);
                            alert(response);
                        }
                    });

                });
            }

        });
        return View;
    });
