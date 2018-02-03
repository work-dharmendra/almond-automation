/**
 * It defines common method which can be used by all components of automation.
 * E.g. get all events associated with given account id and event type.
 * Consider this as Common class in Java which has static methods.
 * Do not put any method in this class which needs to use this operator.
 */
define("base/module/CommonModule", ["jquery",
    "underscore",
    "base/module/Util",
    "i18next",
    "jqueryBlockUI"
], function($, _, Util, i18next) {

    function CommonModule() {}

    /**
     * This method shows the loader.
     */
    CommonModule.showLoader = function() {
//        return;
        console.log("CommonModule.showLoader : Show loader");
        //$.blockUI({ message: $('#loader') }); 
        $("body").block({
            message: "<div class=\"spinner\"></div>",
            overlayCSS: {
                width: "100%",
                height: "100%"
            },
            css: {
                width: "128px",
                cursor: "default",
                backgroundColor: "",
                border: "0px solid #aaa",
                top: "250px",
                zIndex: "1250"
            },
            centerX: true,
            centerY: false
        });
    };

    /**
     * This method shows the loader.
     */
    CommonModule.hideLoader = function() {
        console.log("CommonModule.hideLoader : Hide loader");
        $("body").unblock();
        $.unblockUI();
    };

    CommonModule.getProjectArray = function(projects) {
        console.log("CommonModule.getProjectArray : projects = ", projects);
        var projectsSelectArray = [];
        _.each(projects.toJSON(), function(item) {
            projectsSelectArray.push({
                label: item.name,
                value: item.id
            });
        });
        return projectsSelectArray;
    };

    CommonModule.getEnvironmentArray = function(environments) {
        console.log("CommonModule.getEnvironmentArray : environments = ", environments);
        var environmentsSelectArray = [];
        _.each(environments, function(item) {
            environmentsSelectArray.push({
                label: item.name,
                value: item.id
            });
        });
        return environmentsSelectArray;
    };

    CommonModule.getEnvironmentArrayFromProject = function(projects, projectId) {
        if(projectId === -1 ){
            return [];
        }
        console.log("CommonModule.getEnvironmentArrayFromProject : projects = %o, projectId = %o", projects, projectId);
        var environments = _.filter(projects.toJSON(), function(project) {
            if (project.id == projectId) {
                return project.environments;
            }
        })[0].environments;
        var environmentsSelectArray = [];
        _.each(environments, function(item) {
            environmentsSelectArray.push({
                label: item.name,
                value: item.id
            });
        });
        return environmentsSelectArray;
    };

    CommonModule.getLocalizedComment = function(comment) {
        if (Util.isNull(comment)) {
            return comment;
        }
        return i18next.t('app.' + comment, { defaultValue: comment });
    };

    CommonModule.getCommands = function(commands) {
        var result = [];
        _.each(commands, function(item) {
            result.push({
                id: item,
                text: i18next.t('app.commands.' + item + '.text', { defaultValue: item }), //id/text is for backgrid
                label: i18next.t('app.commands.' + item + '.text', { defaultValue: item }), //label/value is for epoxy
                value: item
            });
        });
        return result;
    };

    CommonModule.getCommands = function() {
        var result = [];
        var LocalStorage = require("LocalStorage");
        var commands = LocalStorage.get('commands');
        if(Util.isNull(commands)){
            $.ajax({
                url: 'commandlist.service',
                dataType: 'json',
                async: false,
                success: function(response) {
                    console.log('CommonModule.getCommands : success : response = ', response);
                    commands = response.commands;
                    LocalStorage.set('commands', commands);
                },

                error: function(response){
                    console.log('CommonModule.getCommands : error : response = ', response);
                }
            });    
        }

        _.each(commands, function(item) {
            result.push({
                id: item,
                text: i18next.t('app.commands.' + item + '.text', { defaultValue: item }), //id/text is for backgrid
                label: i18next.t('app.commands.' + item + '.text', { defaultValue: item }), //label/value is for epoxy
                value: item
            });
        });

        return result;
    };

    /**
     * It return error message which needs to be displayed to ui. It check for token in i18next after converting
     * dot with underscore for error code. If token doesn"t exist, then it return error message from api.
     */
    CommonModule.getErrorMsg = function(error) {
        var errorMsg = i18next.t("app.error.unknown");
        try {
            var i18Params = {
                defaultValue: error.error_message ? error.error_message : error.error
            };
            if (error.params) {
                i18Params = $.extend(i18Params, error.params);
            }
            if (error.error) {
                errorMsg = i18next.t("app.error." + error.error, i18Params);
            } else {
                errorMsg = error.error_message;
            }
        } catch (err) {
            console.log("CommonModule.getErrorMsg : exception = ", err);
        }

        return errorMsg;
    };

    /**
     * It return detailed error message which needs to be displayed to ui.
     */
    CommonModule.getDetailExceptionMsg = function(response) {
        try {
            if(response.sqlException && response.sqlException.detailMessage){
                return response.sqlException.detailMessage;
            } else if(response.detailMessage){
                return response.detailMessage;
            } else if(response.sqlException){
                return response.sqlException.detailMessage;
            }
            
        } catch (err) {
            console.log("CommonModule.getDetailExceptionMsg : exception = ", err);
        }

        return null;
    };

    return CommonModule;
});
