define(
    [
        "jquery",
        "underscore",
        "base/models/BaseModel",
        "base/view/BaseView",
        "testcase/model/CommandModel",
        "text!testcase/tpl/command-item.html",
        "testcase/view/EditPopupView",
        "base/module/CommonModule",
        "base/module/Util",
        "i18next", "jqueryUI"
    ],
    function($, _, BaseModel, BaseView, CommandModel, CommandItemTpl, EditPopupView, CommonModule, Util, i18next) {
        var CommandItemView = BaseView.extend({

            model: CommandModel,

            events: {
                "click .edit_element": "editElement",
                "click .edit_value": "editValue",
                "click .edit_params": "editParams",
                "click .save-edit-element": "saveEditElement",
                "change .testcase_commands": "onChangeCommand"

            },

            initialize: function(options) {
                console.log('CommandItemView.initialize : options = ', options);
                //this.model.set("attributes", this.model.attributes);
                this.viewModel = new BaseModel({
                    //projects: CommonModule.getProjectArray(this.projects),
                    //environments: CommonModule.getEnvironmentArrayFromProject(this.projects, -1),
                    new_command_name: '',
                    new_command_element: '',
                    new_command_value: '',
                    new_command_select_value: 'select_testcase',
                    new_command_param: '',
                    commands: CommonModule.getCommands(),
                    environmentId: -1,
                    custom_parameters: '',
                    editElement: 'temp'
                });
                this.render();
            },

            render: function() {
                console.log('CommandItemView.render : model = ', this.model);
                this.$el.html(_.template(CommandItemTpl)({
                    i18next: i18next
                }));
                return this;
            },

            editElement: function() {
                console.log('CommandItemView.editElement : model = ', this.model.attributes);

                var model = new BaseModel({
                    command: this.model.type,
                    element: this.model.attributes.element,
                    testCaseName: this.model.get("testCaseName")
                });
                var self = this;
                var editView = new EditPopupView({
                    model: model,
                    edit: "element"
                });
                editView.listenTo(editView, "save", function(attributes) {
                    self.model.set("element", attributes.element);
                });
                //$('#edit-popup').html(editView.$el).dialog();
                $('#editElementPopup').show();
                $('#edit-popup').html(editView.$el).show();

            },

            editValue: function() {
                console.log('CommandItemView.editValue : model = ', this.model.attributes);

                var model = new BaseModel({
                    projectId: $("#projectId").val(),
                    name: this.model.get("name"),
                    element: this.model.attributes.value,
                    id: this.model.attributes.value,
                    testCaseName: this.model.get("testCaseName")
                });
                var self = this;
                var editView = new EditPopupView({
                    model: model,
                    edit: "value"
                });
                editView.listenTo(editView, "save", function(attributes) {
                    self.model.set({
                        value: attributes.element,
                        testCaseName: attributes.testCaseName
                    });
                });
                $('#editElementPopup').show();
                $('#edit-popup').html(editView.$el).show();

            },

            editParams: function() {
                console.log('CommandItemView.editParams : model = ', this.model.attributes);

                var model = new BaseModel({
                    command: this.model.type,
                    element: this.model.attributes.params,
                    testCaseName: this.model.get("testCaseName")
                });
                var self = this;
                var editView = new EditPopupView({
                    model: model,
                    edit: "params"
                });
                editView.listenTo(editView, "save", function(attributes) {
                    self.model.set("params", attributes.element);
                });
                $('#editElementPopup').show();
                $('#edit-popup').html(editView.$el).show();

            },

            editCommand: function(){
                console.log("CommandItemView.editCommand : model = ", this.model.attributes);

                var model = new BaseModel({
                    command: this.model.toJSON(),
                    element: this.model.attributes.params,
                    testCaseName: this.model.get("testCaseName")
                });
                var self = this;
                var editView = new EditPopupView({
                    model: this.model,
                    edit: "params"
                });
                editView.listenTo(editView, "save", function(attributes) {
                    self.model.set("params", attributes.element);
                });
                $('#editElementPopup').show();
                $('#edit-popup').html(editView.$el).show();                
            },

            saveEditElement: function() {
                console.log('CommandItemView.saveEditElement : model = ', this.model.attributes);
                this.model.set('element', $('#edit-element').val());
                $('#editElementPopup').hide();
            },

            onChangeCommand: function(){
                this.model.set({
                    element: null,
                    value: null,
                    params: null,
                    testCaseName: null
                });
                this.viewModel.set({
                    element: null,
                    value: null,
                    params: null,
                    testCaseName: null
                });
            }

        });

        return CommandItemView;
    });
