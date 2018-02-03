/**
 * View to represent all project in paginated form and buttons to add/view/edit projects
 */
define([
        'jquery',
        'underscore',
        'base/view/BaseView',
        'project/model/ProjectModel',
        'text!project/tpl/project.html',
        'base/module/Util',
        'base/module/CommonModule',
        'i18next'
    ],
    function($, _, BaseView, ProjectModel, ProjectTpl, Util, CommonModule, i18next) {
        var View = BaseView.extend({

            model: ProjectModel,

            events: {
                'click #saveProject' : 'saveProject'
            },

            initialize: function(options) {
                console.log('ProjectView.initialize : starts, options = ', options);

                this.render();
            },

            render: function() {
                this.$el.html(_.template(ProjectTpl)({
                    heading: Util.isNotNull(this.model.get('id')) ? i18next.t('app.project.edit_project') : i18next.t('app.project.create_project'),
                    i18next: i18next
                }));
                return this;
            },

            saveProject: function(){
                console.log('ProjectView.saveProject : saving project ', this.model);
                this.resetMessage();
                var self = this;
                
                if(this.model.isValid()){
                    CommonModule.showLoader();
                    this.model.save(null, {
                        success: function(model, response) {
                            CommonModule.hideLoader();
                            console.log('ProjectView.saveProject : save successfull, response ', response);
                            self.$('.sys-message').addClass('success').html(i18next.t('app.project.success_save_project'));
                        },

                        error: function(model, response) {
                            CommonModule.hideLoader();
                            console.log('ProjectView.saveProject : save UNsuccessfull, response ', response);
                            self.$('.sys-message').addClass('error').html(i18next.t('app.project.success_save_project'));
                        }
                    });
                } else {
                    console.log("ProjectView.saveProject : invalid model, error = ", this.model.validationError[0]);
                    self.$('.sys-message').addClass('error').html(CommonModule.getErrorMsg(this.model.validationError[0]));
                }
            }

        });
        return View;
    });
