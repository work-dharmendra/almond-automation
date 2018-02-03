define([
        'jquery',
        'underscore',
        'base/view/BaseView',
        'base/collection/BaseCollection',
        'environment/model/EnvironmentModel',
        'environment/view/EnvironmentView',
        'text!environment/tpl/environment-info.html',
        'i18next'
    ],
    function($, _, BaseView, BaseCollection, EnvironmentModel, EnvironmentView, EnvironmentInfoTpl, i18next) {
        var View = BaseView.extend({

            events: {
                "click #create_env": "createEnvironment",
                "click .edit_env": "openEnvironment"
            },

            initialize: function(options) {
                console.log('EnvironmentInfoView.initialize : starts, options = ', options);
                this.environments = options.environments;
                this.render();
            },

            render: function() {
                this.$el.html(_.template(EnvironmentInfoTpl)({
                    i18next: i18next
                }));
                var self = this;
                setTimeout(function() {
                    $('#environments').dataTable({
                        bServerSide: false,
                        aaSorting: [[ 2, "desc" ]],
                        aaData: self.environments[0].list,
                        bPaginate: false,
                        aoColumns: [{
                            "mData": "id",
                            "sTitle": i18next.t('app.id'),
                            "bVisible": true,
                            "bSortable": true,
                            "sWidth": "50"
                        }, {
                            "mDataProp": "name",
                            mRender: self.editEnvironment,
                            "sTitle": i18next.t('app.env.name'),
                            "bVisible": true,
                            "bSortable": true,
                            "sWidth": "300"
                        }, {
                            "mData": "projectName",
                            "sTitle": i18next.t('app.project.name'),
                            "bVisible": true,
                            "bSortable": true,
                            "sWidth": "300"
                        }, {
                            "mData": "description",
                            "sTitle": i18next.t('app.env.description'),
                            "bVisible": true,
                            "bSortable": true,
                            "sWidth": "300",
                            "sDefaultContent": "",
                        }]
                    });
                }, 100);
                
                return this;
            },

            editEnvironment: function(data, type, full) {
                return '<a class="edit_env link" id=' + full.id + '>' + full.name + '</a>';
            },

            createEnvironment: function() {
                var model = new EnvironmentModel();
                require(['project/collection/ProjectCollection'], function(ProjectCollection) {
                    var projectCollection = new ProjectCollection();
                    projectCollection.fetch({
                        success: function(response) {
                            console.log('EnvironmentInfoView.showEnvironment : successfull : after fetching project collection , response = ', response);
                            var envView = new EnvironmentView({
                                model: model,
                                projects: projectCollection
                            });
                            $('.global-popup').html(envView.$el);
                            $('#showEnv').modal('show');
                        },
                        error: function(response) {
                            console.log('EnvironmentInfoView.showEnvironment : error : after fetching project collection , response = ', response);
                            alert(response);
                        }
                    });

                });

            },

            openEnvironment: function(event) {
                var envId = $(event.target).attr('id');
                var environment = new EnvironmentModel({
                    id: envId
                });
                require(['project/collection/ProjectCollection'], function(ProjectCollection) {
                    var projectCollection = new ProjectCollection();
                    $.when(environment.fetch(), projectCollection.fetch()).done(function(environmentResponse, projectsResponse){
                        console.log('EnvironmentInfoView.openEnvironment : after : environment fetching and projects fetching, response = ', environmentResponse, projectsResponse);
                        var environmentView = new EnvironmentView({
                            model: environment,
                            projects: projectCollection
                        });
                        $('.global-popup').html(environmentView.$el);
                        $('#showEnv').modal('show');
                    });
                });
            }

        });
        return View;
    });
