/**
 * View to represent all project in paginated form and buttons to add/view/edit projects
 */
define([
        'jquery',
        'underscore',
        'base/models/BaseModel',
        'base/view/BaseView',
        'base/collection/BaseCollection',
        'testsuite/model/TestSuiteModel',
        'testcase/collection/TestCaseCollection',
        'text!testsuite/tpl/testsuite.html',
        'base/module/Util',
        'base/module/CommonModule',
        'i18next',
        'backgrid', 'select2'
    ],
    function($, _, BaseModel, BaseView, BaseCollection, TestSuiteModel, TestCaseCollection, TestSuiteTpl, Util, CommonModule, i18next) {
        var View = BaseView.extend({

            model: TestSuiteModel,

            events: {
                'click #saveTestSuite': 'saveTestSuite',
                'change #projectId': 'loadTestCases'
            },

            initialize: function(options) {
                console.log('TestSuiteView.initialize : starts, options = ', options);
                this.projects = options.projects;
                this.testCases = options.testCases;
                this.viewModel = new BaseModel({
                    projects: CommonModule.getProjectArray(this.projects)
                });

                this.render();
            },

            render: function() {
                this.$el.html(_.template(TestSuiteTpl)({
                    heading: Util.isNotNull(this.model.get('id')) ? i18next.t('app.testsuite.edit_testsuite') : i18next.t('app.testsuite.create_testsuite'),
                    i18next: i18next
                }));
                var self = this;
                setTimeout(function() {
                    self.testcases = $('#suite_testcases').dataTable({
                        bServerSide: false,
                        aaData: [],
                        bPaginate: false,
                        aoColumns: [{
                            "mDataProp": "id",
                            "mRender": self.renderCheckbox,
                            "sTitle": '',
                            "bVisible": true,
                            "bSortable": false,
                            "sWidth": "40"
                        }, {
                            "mDataProp": "name",
                            "sTitle": i18next.t('app.testcase.testcase'),
                            "bVisible": true,
                            "bSortable": true,
                            "sWidth": "400"
                        }]
                    });
                    if (Util.isNotNull(self.model.get('id'))) {
                        self.testcases.fnClearTable();
                        self.testcases.fnAddData(self.testCases.toJSON());
                    }
                    //self.$('#projectId').select2();
                }, 100);

                return this;
            },

            loadTestCases: function() {
                var projectId = this.model.get('projectId');

                if (Util.isNull(projectId) || projectId === -1) {
                    console.log('TestSuiteView.loadTestCases : NOT loading testcases for projectId = ', projectId);
                    return;
                }
                console.log('TestSuiteView.loadTestCases : loading testcases for projectId = ', projectId);
                var testCaseCollection = new TestCaseCollection({
                    projectId: projectId
                });
                var self = this;

                testCaseCollection.fetch({
                    success: function(model, response) {
                        console.log('TestSuiteView.loadTestCases : successful loading testcases response = ', response);
                        self.testcases.fnClearTable();
                        self.testcases.fnAddData(testCaseCollection.toJSON());
                    },
                    error: function(response) {
                        console.log('TestSuiteView.loadTestCases : error in loading testcases response = ', response);
                    }
                });
            },

            renderCheckbox: function(data, type, full) {
                if (Util.isNotNull(full.checked) && full.checked === true) {
                    return '<input type="checkbox" name="testcase_id" checked=true class="ck_testcase" value="' + full.id + '"/>';
                } else {
                    return '<input type="checkbox" name="testcase_id" class="ck_testcase" value="' + full.id + '"/>';
                }

            },

            saveTestSuite: function() {
                console.log('TestSuiteView.saveTestSuite : saving testsuite ', this.model);
                var self = this;
                this.resetMessage();
                //updated list of selected testcases
                var testCases = [];
                $('.ck_testcase:checked').each(function(index, item) {
                    testCases.push({ id: item.value });
                    console.log(item);
                });
                this.model.set('testCases', testCases);
                if (this.model.isValid()) {
                    CommonModule.showLoader();
                    this.model.save(null, {
                        success: function(model, response) {
                            CommonModule.hideLoader();
                            console.log('TestSuiteView.saveTestSuite : save successfull, response ', response);
                            $('#testsuite-sys-message').addClass('success').html(i18next.t('app.testsuite.success_save_testsuite'));
                        },

                        error: function(model, response) {
                            CommonModule.hideLoader();
                            console.log('TestSuiteView.saveTestSuite : save UNsuccessfull, response ', response);
                            $('#testsuite-sys-message').addClass('error').html(i18next.t('app.testsuite.error_save_testsuite'));
                        }
                    });
                } else {
                    console.log("TestSuiteView.saveTestSuite : invalid model, error = ", this.model.validationError[0]);
                    $('#testsuite-sys-message').addClass('error').html(CommonModule.getErrorMsg(this.model.validationError[0]));
                }
            }

        });
        return View;
    });
