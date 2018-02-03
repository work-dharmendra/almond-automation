/**
 * View to represent all project in paginated form and buttons to add/view/edit projects
 */
define([
        'jquery',
        'underscore',
        'base/models/BaseModel',
        'base/view/BaseView',
        'base/collection/BaseCollection',
        'schedule/model/ScheduleModel',
        'text!schedule/tpl/schedule-testcase.html',
        'base/module/Util',
        'base/module/CommonModule',
        'i18next',
        'backgrid'
    ],
    function($, _, BaseModel, BaseView, BaseCollection, ScheduleModel, ScheduleTestCaseTpl, Util, CommonModule, i18next) {
        var View = BaseView.extend({

            model: ScheduleModel,

            events: {
                'click #scheduleTestCaseBtn': 'schedule'
            },

            initialize: function(options) {
                console.log('ScheduleView.initialize : starts, options = ', options);
                this.environments = options.environments;
                this.viewModel = new BaseModel({
                    environments: CommonModule.getEnvironmentArray(this.environments)
                });
                this.model.set('environmentId', -1);
                this.render();
            },

            render: function() {
                var self = this;
                this.$el.html(_.template(ScheduleTestCaseTpl)({
                    i18next: i18next
                }));

                setTimeout(function(){
                    self.$('#envId').select2({
                        width: '200px'
                    });
                }, 200);
                

                return this;
            },

            schedule: function() {
                console.log('ScheduleView.schedule : scheduling testcase ', this.model);
                var self = this;
                if(Util.isNull(this.model.get('environmentId')) || this.model.get('environmentId') === -1){
                    return;
                }
                if (this.model.isValid()) {
                    this.model.save(null, {
                        success: function(model, response) {
                            console.log('ScheduleView.schedule : schedule successfull, response ', response);
                            self.$('#sys-message').addClass('success').html(i18next.t('app.testcase.success_schedule_testcase', { SCHEDULEID: response.scheduleId }));
                        },

                        error: function(model, response) {
                            console.log('ScheduleView.schedule : schedule UNsuccessfull, response ', response);
                            self.$('#sys-message').addClass('error').html(i18next.t('app.testcase.error_schedule'));
                        }
                    });
                }
            }

        });
        return View;
    });
