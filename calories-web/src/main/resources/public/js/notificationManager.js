var NotificationManager = function (scope) {

    // notification queue
    scope.notifications = [];  // WARN:  Don't change this variable name, it's coupled to scope and outside of this function.

    // add notification to model
    this.notify = function (type, text) {
        scope.notifications.push({"type": type, "text": text});
    }
};