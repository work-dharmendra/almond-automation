/**
 * It contains constants used by all application
 */
define("base/module/Constants", function() {

    var constants = {};

    //contains list of commands which require element
    constants.nonEmptyElementCommands = ["click", "type", "typeAndStore", "verifyTextPresent", "waitForElementAndClick", "waitForElementNotVisible",
                               "waitForElementPresent", "dragAndDropByOffset", "dragAndDrop", "select", "store", "text",
                               "untilVerifyTextPresent", "verifyElementNotPresent", "verifyElementPresent" ];
    //contains list of command which require value
    constants.nonEmptyValueCommands = ["dragAndDropByOffset", "dragAndDrop", "javascript", "open", "select", "store", "text",
                               "typeAndStore", "type", "dragAndDrop", "text",
                               "untilVerifyTextPresent", "wait", "verifyElementPresent", "include", "invoke" ];

    //contains list of command which require both element and value
    constants.nonEmptyElementValueCommands = ["dragAndDropByOffset", "dragAndDrop", "select", "store", "text",
                               "typeAndStore", "type", "dragAndDrop", "text", "untilVerifyTextPresent"];
    

    constants.availableSelectorType = ["id", "name", "class", "xpath", "script", "css"];

    constants.availableSelectType = ["value", "index", "text"];

    return constants;
});
