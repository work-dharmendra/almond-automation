define([
        "jquery",
        "underscore",
        "base/view/BaseView",
        'text!database/tpl/database-tpl.html',
        "base/module/Util",
        "i18next"
    ],
    function($, _, BaseView, DatabaseTpl, Util, i18next) {
        var DatabaseView = BaseView.extend({

            events: {
                "click #prepareDatabase": "prepareDatabase"
            },

            initialize: function(options) {
                console.log("DatabaseView.initialize : starts");
                this.render();
            },

            render: function() {
                var template = _.template(DatabaseTpl);
                this.$el.html(template({
                    i18next: i18next
                }));
                return this;
            },

            prepareDatabase: function() {
                var host = $('#host').val();
                var username = $('#username').val();
                var password = $('#password').val();
                var database = $('#database').val();
                var data = {
                    "host": host,
                    "database": database,
                    "username": username,
                    "password": password
                };
                self.$('#sys-message').removeClass('error').removeClass('success').html('');
                $.ajax({
                    type: "POST",
                    url: 'preparedatabase.service',
                    data: JSON.stringify( data ),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function(response){
                        self.$('#sys-message').addClass('success').html(i18next.t('app.database.success'));
                        console.log('DatabaseView.prepareDatabase : success, response = ', response);
                        setTimeout(function(){
                            window.location.href = 'index.html';
                        }, 3000);
                    },
                    error: function(response){
                        self.$('#sys-message').addClass('error').html(response.responseJSON.defaultMessage);
                        console.log('DatabaseView.prepareDatabase : error, response = ', response);
                    }
                });
            }


        });

        return DatabaseView;
    });
