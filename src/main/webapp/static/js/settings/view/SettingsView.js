define([
        "jquery",
        "underscore",
        "base/view/BaseView",
        'text!settings/tpl/settings.html',
        "base/module/CommonModule",
        "base/module/Util",
        "i18next"
    ],
    function($, _, BaseView, SettingsTpl, CommonModule, Util, i18next) {
        var SettingsView = BaseView.extend({

            events: {
                "click #saveSettings": "saveSettings"
            },

            initialize: function(options) {
                console.log("SettingsView.initialize : starts");
                this.render();
            },

            render: function() {
                var template = _.template(SettingsTpl);
                this.$el.html(template({
                    i18next: i18next
                }));
                return this;
            },

            saveSettings: function() {
                this.$('#sys-message').removeClass('success').removeClass('error').html();
                var self = this;
                if(this.model.isValid()){
                    CommonModule.showLoader();
                    this.model.save(null, {
                        success: function(model, response){
                            CommonModule.hideLoader();
                            console.log('SettingsView.saveSettings : success saving settings, response = ', response );
                            self.$('#sys-message').addClass('success').html(i18next.t('app.settings.success_save_settings'));
                        },

                        error: function(response){
                            CommonModule.hideLoader();
                            console.log('SettingsView.saveSettings : error in saving settings, response = ', response ); 
                            self.$('#sys-message').addClass('error').html(i18next.t('app.settings.error_save_settings')); 
                        }
                    });
                } else {
                    console.log('SettingsView.saveSettings : model is invalid');
                }

            }

        });

        return SettingsView;
    });
