var exec = require('cordova/exec');

exports.storage = function(arg0, success, error) {
    exec(success, error, "achievejurisdiction", "storage", [arg0]);
};
exports.exitApp = function(arg0, success, error) {
    exec(success, error, "achievejurisdiction", "exitApp", [arg0]);
};
exports.installApp = function(arg0, success, error) {
    exec(success, error, "achievejurisdiction", "installApp", [arg0]);
};
exports.isPermission = function(arg0, success, error) {
    exec(success, error, "achievejurisdiction", "isPermission", [arg0]);
};
