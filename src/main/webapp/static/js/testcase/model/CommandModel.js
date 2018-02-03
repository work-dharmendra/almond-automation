define(
    [
        "backbone",
        "epoxy",
        "LocalStorage",
        "base/module/Util",
        "base/module/Constants"
    ],
    function(Backbone, Epoxy, LocalStorage, Util, Constants) {
        var CommandModel = Backbone.Epoxy.Model.extend({

            defaults: function() {
                return {
                    name: null,
                    element: null,
                    value: null,
                    params: null,
                    testCaseName: null
                };
            },

            computeds: {
                getValue: {
                    get: function() {
                        if ($.inArray(this.get('name'), ["include", "invoke"]) !== -1) {
                            return this.get("testCaseName");
                        }
                        return this.get("value");
                    }
                },

                cid: {
                    get: function() {
                        return this.cid;
                    }
                }
            },

            validate: function() {
                var errors = [];

                if (Util.isNull(this.get("name"))) {
                    errors.push({ error: "command_required_command_name", params: { } });
                    return errors;
                }

                if($.inArray(this.get("name"), Constants.nonEmptyElementCommands) !== -1){
                    if(Util.isNull(this.get("element"))){
                        errors.push({ error: "command_required_element", params: { name: this.get("name") } });       
                        return errors;
                    }
                    if(this.get("element").indexOf("=") === -1){
                        errors.push({ error: "command_invalid_element", params: { name: this.get("name") } });       
                        return errors;   
                    }
                    var selectorType = this.get("element").split("=")[0];
                    if($.inArray(selectorType, Constants.availableSelectorType) === -1){
                        errors.push({ error: "command_invalid_element_selector", params: { name: this.get("name") } });       
                        return errors;      
                    }

                }

                if($.inArray(this.get("name"), Constants.nonEmptyValueCommands) !== -1){
                    if(Util.isNull(this.get("value"))){
                        errors.push({ error: "command_required_value", params: { name: this.get("name") } });       
                        return errors;
                    }
                }

                //select command validation
                if(this.get("name") === "select"){
                    var selectType = this.get("value").split("=");
                    if(selectType.length === 1 && selectType !== "value"){
                        errors.push({ error: "command_invalid_select_type_value", params: { name: this.get("name") } });       
                        return errors;
                    }
                    //index type must have positive number
                    if(selectType.length > 1 &&  selectType[0] === "index" && (Util.isNumber(selectType[1]) === false || selectType[1] <= 0)){
                        errors.push({ error: "command_invalid_select_type_index_value", params: { name: this.get("name") } });       
                        return errors;
                    }
                    if(selectType.length > 1 && $.inArray(selectType[0], Constants.availableSelectType)  === -1){
                        errors.push({ error: "command_invalid_select_type_value", params: { name: this.get("name") } });       
                        return errors;
                    }
                }

                //wait command validation
                if(this.get("name") === "wait"){
                    if(Util.isNumber(this.get("value")) === false || this.get("value") < 0){
                        errors.push({ error: "command_invalid_wait_value", params: { name: this.get("name") } });       
                        return errors;
                    }
                }

                //wait command validation
                if(this.get("name") === "dragAndDropByOffset"){
                    var offsets = this.get("value").split(";");

                    if(offsets < 1){
                        errors.push({ error: "command_invalid_dragAndDropByOffset_value", params: { name: this.get("name") } });       
                        return errors;
                    }

                    var xOffset = offsets[0];
                    var xValue = xOffset.split("=");

                    if(xValue.length < 1){
                        errors.push({ error: "command_invalid_dragAndDropByOffset_x_value", params: { name: this.get("name") } });       
                        return errors;   
                    }

                    if(xValue[0] !== "x"){
                        errors.push({ error: "command_invalid_dragAndDropByOffset_x_value", params: { name: this.get("name") } });       
                        return errors;
                    }

                    if(Util.isNumber(xValue[1]) === false){
                        errors.push({ error: "command_invalid_dragAndDropByOffset_x_value", params: { name: this.get("name") } });       
                        return errors;
                    }

                    var yOffset = offsets[0];
                    var yValue = yOffset.split("=");

                    if(yValue[0] !== "y"){
                        errors.push({ error: "command_invalid_dragAndDropByOffset_y_value", params: { name: this.get("name") } });       
                        return errors;
                    }
                    if(xValue.length < 1){
                        errors.push({ error: "command_invalid_dragAndDropByOffset_y_value", params: { name: this.get("name") } });       
                        return errors;   
                    }

                    if(Util.isNumber(yValue[1]) === false){
                        errors.push({ error: "command_invalid_dragAndDropByOffset_y_value", params: { name: this.get("name") } });       
                        return errors;
                    }

                }

                console.log("CommandModel.validate errors", errors);
                return errors.length === 0 ? null : errors;
            }
        });

        return CommandModel;
    });
