/**
 * Router
 *
 * This router manages all the routes of the application.
 * It sets the different pages with the specific patterns throught the "routes"
 */
define([
    "jquery",
    "backbone",
    'header/view/HeaderView',
    'project/view/ProjectInfoView',
    'base/module/CommonModule'
], function($, Backbone, HeaderView, ProjectInfoView, CommonModule) {

    /**
     * Main Router of the application
     *
     * @type {[type]}
     */
    var AppRouter = Backbone.Router.extend({
        /**
         * The different routes of the application
         * pattern: callback
         * @type {Object}
         */
        routes: {
            "": "homePage",
            "project": "homePage",
            "environment": "environment",
            "testcase": "testcase",
            "testsuite": "testsuite",
            "status": "status",
            "database": "showDatabase",
            "settings": "settings",
            "*notFound": "homePage"
        },

        /*before:  {

            '*any': function(){
                console.log('route1111', route);
            // Do something with every route before it's routed. "route" is a string
            // containing the route fragment just like regular Backbone route
            // handlers. If the url has more fragments, the before callback will
            // also get them, eg: before: function( frag1, frag2, frag3 )
            // (just like regular Backbone route handlers).    
            }
            
        },*/

        homePage: function() {
            console.log('router.homePage : showing home page');
            var ProjectCollection = require('project/collection/ProjectCollection');
            var projectCollection = new ProjectCollection();
            CommonModule.showLoader();
            projectCollection.fetch({
                success: function(response) {
                    CommonModule.hideLoader();
                    var headerView = new HeaderView();
                    $('#top-nav-container').html(headerView.$el);
                    $('#project').addClass('active');
                    var projectInfoView = new ProjectInfoView({
                        projects: projectCollection.toJSON()
                    });
                    $('#content').html(projectInfoView.$el);
                }
            });


        },

        environment: function() {
            console.log('router.environment : showing environment page');
            require(['environment/collection/EnvironmentCollection', 'environment/view/EnvironmentInfoView'], function(EnvironmentCollection, EnvironmentInfoView) {
                var environmentCollection = new EnvironmentCollection();
                CommonModule.showLoader();
                environmentCollection.fetch({
                    success: function(response) {
                        CommonModule.hideLoader();
                        var headerView = new HeaderView();

                        $('#top-nav-container').html(headerView.$el);
                        $('#environment').addClass('active');
                        var infoView = new EnvironmentInfoView({
                            environments: environmentCollection.toJSON()
                        });
                        $('#content').html(infoView.$el);
                    }
                });
            });
        },

        testcase: function() {
            console.log('router.testcase : showing testcase page');
            require(['project/collection/ProjectCollection', 'testcase/view/TestCaseInfoView'], function(ProjectCollection, TestCaseInfoView) {
                var projectCollection = new ProjectCollection();
                CommonModule.showLoader();
                projectCollection.fetch({
                    success: function(response) {
                        CommonModule.hideLoader();
                        var headerView = new HeaderView();

                        $('#top-nav-container').html(headerView.$el);
                        $('#testcase').addClass('active');
                        var infoView = new TestCaseInfoView({
                            projects: projectCollection
                        });
                        $('#content').html(infoView.$el);
                    }
                });
            });
        },

        testsuite: function() {
            console.log('router.testsuite : showing testsuite page');
            require(['project/collection/ProjectCollection', 'testsuite/view/TestSuiteInfoView'], function(ProjectCollection, TestSuiteInfoView) {
                var projectCollection = new ProjectCollection();
                CommonModule.showLoader();
                projectCollection.fetch({
                    success: function(response) {
                        CommonModule.hideLoader();
                        var headerView = new HeaderView();

                        $('#top-nav-container').html(headerView.$el);
                        $('#testsuite').addClass('active');
                        var infoView = new TestSuiteInfoView({
                            projects: projectCollection
                        });
                        $('#content').html(infoView.$el);
                    }
                });
            });
        },

        status: function() {
            console.log('router.status : showing status page');
            require(['project/collection/ProjectCollection', 'status/view/StatusView'], function(ProjectCollection, StatusView) {
                var projectCollection = new ProjectCollection();
                CommonModule.showLoader();
                projectCollection.fetch({
                    success: function(response) {
                        CommonModule.hideLoader();
                        console.log('router.status : project collection success : response = ', response);
                        var headerView = new HeaderView();

                        $('#top-nav-container').html(headerView.$el);
                        $('#status').addClass('active');
                        var statusView = new StatusView({
                            projects: projectCollection
                        });
                        $('#content').html(statusView.$el);
                    }
                });
            });
        },

        showDatabase: function() {
            console.log('router.showDatabase : starts');
            require(['database/view/DatabaseView'], function(DatabaseView) {
                var databaseView = new DatabaseView();
                $('#content').html(databaseView.$el);
            });

        },

        settings: function() {
            console.log('router.settings : showing settings page');
            require(['settings/model/SettingsModel', 'settings/view/SettingsView'], function(SettingsModel, SettingsView) {
                var model = new SettingsModel();
                model.fetch({
                    success: function(model, response){
                        console.log('router.settings: success fetch settings model , response = ', response);
                        var view = new SettingsView({
                            model: model
                        });
                        var headerView = new HeaderView();

                        $('#top-nav-container').html(headerView.$el);
                        $('#settings').addClass('active');
                        $('#content').html(view.$el);
                    },

                    error: function(response){
                        console.log('router.settings: error in fetch settings model , response = ', response);
                    }
                });
                
            });
        },

        /**
         * This route is invoked when matching route found.
         * It redirect user to login page
         */
        notFound: function() {
            console.log("router.notFound : starts : showing home page");
            var headerView = new HeaderView();

            $('#top-nav-container').html(headerView.$el);
        }

    });

    return AppRouter;
});
