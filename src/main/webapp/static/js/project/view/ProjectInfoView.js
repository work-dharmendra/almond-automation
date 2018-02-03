/**
 * View to represent all project in paginated form and buttons to add/view/edit projects
 */
define([
        'jquery',
        'underscore',
        'base/view/BaseView',
        'project/model/ProjectModel',
        'project/view/ProjectView',
        'text!project/tpl/project-info.html',
        'project/collection/ProjectCollection',
        "base/module/CommonModule",
        'i18next',
        "jquery.datatables"
    ],
    function($, _, BaseView, ProjectModel, ProjectView, ProjectInfoTpl, ProjectCollection, CommonModule, i18next) {
        var View = BaseView.extend({

            events: {
                "click #create_prj": "showProject",
                "click .edit_project": "openProject"
            },

            initialize: function(options) {
                console.log('ProjectInfoView.initialize : starts, options = ', options);
                this.projects = options.projects;
                this.render();
            },

            render: function() {
                this.$el.html(_.template(ProjectInfoTpl)({
                    i18next: i18next
                }));
                var self = this;
                setTimeout(function() {
                    $('#projects').dataTable({
                        bServerSide: false,
                        bJQueryUI: true,
                        bPaginate: false,
                        aaData: self.projects,
                        data: self.projects,
                        aoColumns: [{
                            "mData": "id",
                            "sTitle": i18next.t('app.id'),
                            "bVisible": true,
                            "bSortable": true,
                            "sWidth": "50"
                        }, {
                            "mData": "name",
                            "mRender": self.renderName,
                            "sTitle": i18next.t('app.name'),
                            "bVisible": true,
                            "bSortable": true,
                            "sWidth": "300"
                        }, {
                            "mData": "description",
                            "sTitle": i18next.t('app.description'),
                            "bVisible": true,
                            "bSortable": true,
                            "sWidth": "300"
                        }]
                    });
                }, 100);

                return this;
            },

            renderName: function(data, type, full) {
                return '<a class="edit_project link" id=' + full.id + '>' + full.name + '</a>';
            },

            /**
             * open modal to create new project
             */
            showProject: function() {
                var model = new ProjectModel();
                var projectView = new ProjectView({
                    model: model
                });
                $('.global-popup').html(projectView.$el);
                $('#showProject').modal('show');
            },

            openProject: function(event) {
                var projectId = $(event.target).attr('id');
                var projectModel = new ProjectModel({
                    id: projectId
                });
                CommonModule.showLoader();
                projectModel.fetch({
                    success: function(model, response) {
                        CommonModule.hideLoader();
                        console.log('ProjectInfoView.openProject : success project fetch, response = ', response);
                        var projectView = new ProjectView({
                            model: model
                        });
                        $('.global-popup').html(projectView.$el);
                        $('#showProject').modal('show');
                    },

                    error: function(response) {
                        CommonModule.hideLoader();
                        console.log('ProjectInfoView.openProject : error in project fetch, response = ', response);
                    }
                });
            },

        });
        return View;
    });
